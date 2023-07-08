package cn.addenda.footprints.cdc.jdbc.invocation;

import cn.addenda.footprints.cdc.jdbc.CdcConnection;
import cn.addenda.footprints.cdc.jdbc.CdcException;
import cn.addenda.footprints.cdc.jdbc.TableConfig;
import cn.addenda.footprints.core.pojo.Binary;
import cn.addenda.footprints.core.util.DruidSQLUtils;
import cn.addenda.footprints.core.util.IterableUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/9/24 9:52
 */
public class UpdatePsDelegate extends AbstractPsDelegate {

    public UpdatePsDelegate(CdcConnection cdcConnection, PreparedStatement ps,
                            TableConfig tableConfig, SQLStatement parameterizedSqlStatement, List<String> sqlList) {
        super(cdcConnection, ps, tableConfig, parameterizedSqlStatement, sqlList);
    }

    @Override
    protected <T> void doAssert(PsInvocation<T> pi) throws SQLException {
        // 需要是 stable sql
        if (!sqlHelper.checkStableUpdateSql((MySqlUpdateStatement) parameterizedSqlStatement, keyColumn)) {
            throw new CdcException("update sql cannot update column which in where-condition and primary key column when sql in batch mode. ");
        }
    }

    @Override
    public <T> T doExecute(PsInvocation<T> pi) throws SQLException {

        // -------------------------------------
        //  对于Statement模式来说，记录下来SQL就行了
        // -------------------------------------
        if (checkTableMode(TableConfig.CM_STATEMENT)) {
            List<String> statementCdcSqlList = new ArrayList<>(sqlList);
            executeCdcSql(TableConfig.CM_STATEMENT, statementCdcSqlList);
        }

        T invoke = null;

        // ----------------------------------
        //  对于ROW模式，需要记录下来具体更新的行。
        // ----------------------------------
        if (checkTableMode(TableConfig.CM_ROW)) {

            MySqlUpdateStatement parameterizedMysqlUpdateStatement = (MySqlUpdateStatement) parameterizedSqlStatement;
            Binary<List<String>, List<Binary<String, SQLExpr>>> binary = sqlHelper.divideColumn(parameterizedMysqlUpdateStatement);
            List<String> dependentColumnList = binary.getF1();
            List<String> calculableColumnList = binary.getF2().stream().map(Binary::getF1).collect(Collectors.toList());

            Binary<T, List<List<Long>>> lockKeyBinary = lockKey(pi);
            invoke = lockKeyBinary.getF1();
            List<List<Long>> keyListList = lockKeyBinary.getF2();

            List<MySqlUpdateStatement> rowCdcSqlStatementList = new ArrayList<>();
            List<String> rowCdcSqlList = new ArrayList<>();
            for (int i = 0; i < sqlStatementList.size(); i++) {
                MySqlUpdateStatement sqlStatement = (MySqlUpdateStatement) sqlStatementList.get(i);
                List<Long> keyValueList = keyListList.get(i);

                if (!calculableColumnList.isEmpty()) {
                    List<MySqlUpdateStatement> tmpSqlList = new ArrayList<>(rowCdcSqlStatementList);
                    rowCdcSqlStatementList.clear();
                    for (MySqlUpdateStatement sql : tmpSqlList) {
                        rowCdcSqlStatementList.add(sqlHelper.calculateColumnValue(sql, calculableColumnList));
                    }
                }

                // 进行 1:n -> 1:1 优化
                // update A set age = 12 where name = 'zhangsan' 的 dependentColumnList为空
                //
                if (dependentColumnList.isEmpty()) {
                    List<List<Long>> listList = IterableUtils.split(keyValueList, IN_SIZE);
                    for (List<Long> item : listList) {
                        List<String> list = assembleRowUpdateSqlList(sqlStatement.getWhere().toString(), item);
                        rowCdcSqlList.addAll(list);
                    }
                }
                // 无法进行 1:n -> 1:1 优化
                else {
                    try (Statement statement = cdcConnection.getDelegate().createStatement()) {
                        Map<Long, Map<String, Object>> keyColumnValueMap =
                                queryKeyColumnValueMap(statement, keyValueList, dependentColumnList);
                        for (Long keyValue : keyValueList) {
                            Map<String, Object> columnValueMap = keyColumnValueMap.get(keyValue);
                            MySqlUpdateStatement mySqlUpdateStatement = sqlHelper.setItemValue(sqlStatement, columnValueMap);
                            mySqlUpdateStatement = DruidSQLUtils.replaceDmlWhereCondition(mySqlUpdateStatement, keyColumn + " = " + keyValue);
                            rowCdcSqlList.add(toStorageSql(mySqlUpdateStatement));
                        }
                    }
                }
            }

            executeCdcSql(TableConfig.CM_ROW, rowCdcSqlList);
        }

        if (invoke != null) {
            return invoke;
        }
        return pi.invoke();
    }

    private List<String> assembleRowUpdateSqlList(String updateSeg, List<Long> keyValueList) {
        List<String> rowCdcSqlList = new ArrayList<>();
        List<List<Long>> listList = IterableUtils.split(keyValueList, IN_SIZE);
        listList.forEach(item -> rowCdcSqlList.add(updateSeg + " where " + keyColumn + " in (" + longListToString(item) + ")"));
        return rowCdcSqlList;
    }

}
