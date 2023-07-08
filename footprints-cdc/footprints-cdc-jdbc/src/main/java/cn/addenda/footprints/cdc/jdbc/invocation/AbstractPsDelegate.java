package cn.addenda.footprints.cdc.jdbc.invocation;

import cn.addenda.footprints.cdc.jdbc.CdcConnection;
import cn.addenda.footprints.cdc.jdbc.CdcException;
import cn.addenda.footprints.cdc.jdbc.TableConfig;
import cn.addenda.footprints.cdc.jdbc.sql.SqlHelper;
import cn.addenda.footprints.cdc.jdbc.sql.StorableSQLVisitor;
import cn.addenda.footprints.core.FootprintsException;
import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.pojo.Binary;
import cn.addenda.footprints.core.util.DruidSQLUtils;
import cn.addenda.footprints.core.util.IterableUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/9/24 9:50
 */
@Slf4j
public abstract class AbstractPsDelegate implements PsDelegate {

    public static final int IN_SIZE = 2;
    public static final int EXECUTE_INSERT_STATEMENT_BATCH = 20;

    protected final CdcConnection cdcConnection;

    protected final PreparedStatement ps;

    protected final TableConfig tableConfig;

    protected final List<String> sqlList;

    protected final List<SQLStatement> sqlStatementList;

    protected final SQLStatement parameterizedSqlStatement;

    protected final String tableName;

    protected final String keyColumn;

    protected final DataConvertorRegistry dataConvertorRegistry;

    protected final SqlHelper sqlHelper;

    protected AbstractPsDelegate(CdcConnection cdcConnection, PreparedStatement ps,
                                 TableConfig tableConfig, SQLStatement parameterizedSqlStatement, List<String> sqlList) {
        this.cdcConnection = cdcConnection;
        this.ps = ps;
        this.tableConfig = tableConfig;
        this.parameterizedSqlStatement = parameterizedSqlStatement;
        this.sqlList = sqlList;
        this.sqlStatementList = sqlList.stream().map(s -> SQLUtils.parseSingleStatement(s, DbType.mysql)).collect(Collectors.toList());
        this.tableName = tableConfig.getTableName();
        this.keyColumn = tableConfig.getKeyColumn();
        this.dataConvertorRegistry = cdcConnection.getCdcDataSource().getDataConvertorRegistry();
        this.sqlHelper = cdcConnection.getCdcDataSource().getSqlHelper();
    }

    @Override
    public <T> T execute(PsInvocation<T> pi) throws SQLException {
        doAssert(pi);
        return doExecute(pi);
    }

    protected <T> void doAssert(PsInvocation<T> pi) throws SQLException {

    }

    protected abstract <T> T doExecute(PsInvocation<T> pi) throws SQLException;

    protected Map<Long, Map<String, Object>> queryKeyColumnValueMap(
            Statement statement, List<Long> keyValueList, List<String> columnList) throws SQLException {
        Map<Long, Map<String, Object>> map = new HashMap<>();
        if (columnList.isEmpty()) {
            return map;
        }
        List<String> resultColumnList = new ArrayList<>(columnList);
        resultColumnList.add(keyColumn);
        List<List<Long>> listList = IterableUtils.split(keyValueList, IN_SIZE);
        for (List<Long> item : listList) {
            int size = item.size();
            String keyInList = longListToString(item);
            String sql = "select "
                    + String.join(",", resultColumnList) + " "
                    + "from " + tableName + " "
                    + "where " + keyColumn + " "
                    + "in (" + keyInList + ")";
            ResultSet resultSet = statement.executeQuery(sql);
            int i = 0;
            while (resultSet.next()) {
                Map<String, Object> columnValueMap = new HashMap<>();
                Long keyValue = null;
                for (String column : resultColumnList) {
                    if (keyColumn.equals(column)) {
                        keyValue = resultSet.getLong(column);
                        columnValueMap.put(column, keyValue);
                    } else {
                        Object object = resultSet.getObject(column);
                        columnValueMap.put(column, object);
                    }
                }
                map.put(keyValue, columnValueMap);
                i++;
            }
            resultSet.close();
            if (size != i) {
                throw new CdcException("Cannot get enough key value from resultSet. keyValueList: " + keyInList + ".");
            }
        }
        return map;
    }

    protected <T> Binary<T, List<List<Long>>> lockKey(PsInvocation<T> pi) throws SQLException {
        List<List<Long>> keyListList;
        Savepoint savepoint = cdcConnection.setSavepoint();
        T invoke;
        while (true) {
            keyListList = new ArrayList<>();
            long sum = 0;
            try (Statement statement = cdcConnection.getDelegate().createStatement()) {
                for (SQLStatement sqlStatement : sqlStatementList) {
                    List<Long> keyValueList = currentRead(statement, sqlStatement);
                    keyListList.add(keyValueList);
                    sum += keyValueList.size();
                }
            }
            // 配置useAffectedRows=true会导致这里出现死循环。
            // 配置后update语句返回的是affected rows。
            // 假如有一条数据是(name, age)('a', 1)，update set name = 'a' where age = 1时此记录不会被计入affected rows。
            // 如果必须要配置此参数，需要表中存在版本号字段，并且每次update时set version = version + 1
            invoke = pi.invoke();
            long count = castInvokeToLong(invoke);
            if (count <= sum) {
                break;
            } else {
                cdcConnection.rollback(savepoint);
                savepoint = cdcConnection.setSavepoint();
            }
        }
        return new Binary<>(invoke, keyListList);
    }

    private <T> long castInvokeToLong(T invoke) {
        if (invoke == null) {
            throw new FootprintsException("execute method return null that do not support. ");
        }
        long count = 0L;
        if (invoke instanceof Long || invoke instanceof Integer) {
            count = Long.parseLong(invoke.toString());
        } else if (invoke instanceof long[]) {
            long[] longs = (long[]) invoke;
            for (long item : longs) {
                count += item;
            }
        } else if (invoke instanceof int[]) {
            int[] integers = (int[]) invoke;
            for (int item : integers) {
                count += item;
            }
        } else {
            throw new FootprintsException("only support long、int、long[]、int[]. current is: " + invoke.getClass() + ".");
        }
        return count;
    }

    /**
     * @return executableSql 执行时锁住的key
     */
    private List<Long> currentRead(Statement statement, SQLStatement sqlStatement) throws SQLException {
        List<Long> keyValueList = new ArrayList<>();
        String sql = "select "
                + keyColumn + " "
                + "from "
                + tableName + " "
                + "where "
                + DruidSQLUtils.extractDmlWhereCondition(sqlStatement) + " "
                + "for update";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            keyValueList.add(resultSet.getLong(keyColumn));
        }
        resultSet.close();
        return keyValueList;
    }

    protected void executeCdcSql(String cdcMode, List<String> cdcSqlList) throws SQLException {
        if (cdcSqlList.isEmpty()) {
            return;
        }

        String sqlPreSegment = "insert into " + tableName + "_cdc_" + cdcMode + "(executable_sql) values (?)";

        Connection connection = cdcConnection.getDelegate();
        try (PreparedStatement statement = connection.prepareStatement(sqlPreSegment)) {
            for (int i = 0; i < cdcSqlList.size(); i++) {
                statement.setString(1, cdcSqlList.get(i));
                statement.addBatch();
                if (i != 0 && i % EXECUTE_INSERT_STATEMENT_BATCH == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch();
        }

        for (String sql : cdcSqlList) {
            log.debug("insert cdc sql: {}. ", sql);
            System.out.println("insert cdc sql: " + sql + ". ");
        }

    }

    protected boolean checkTableMode(String mode) {
        if (tableConfig == null) {
            return false;
        }
        List<String> cdcModeList = tableConfig.getCdcModeList();
        return cdcModeList.contains(mode);
    }

    protected String longListToString(List<Long> longList) {
        return longList.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    protected String toStorageSql(SQLStatement sqlStatement) {
        StringBuilder stringBuilder = new StringBuilder();
        StorableSQLVisitor storableSQLVisitor = new StorableSQLVisitor(stringBuilder, dataConvertorRegistry);
        storableSQLVisitor.setPrettyFormat(false);
        sqlStatement.accept(storableSQLVisitor);
        return stringBuilder.toString();
    }
}
