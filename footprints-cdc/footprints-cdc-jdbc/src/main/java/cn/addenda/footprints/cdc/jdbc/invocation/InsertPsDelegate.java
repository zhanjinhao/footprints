package cn.addenda.footprints.cdc.jdbc.invocation;

import cn.addenda.footprints.cdc.jdbc.*;
import cn.addenda.footprints.cdc.jdbc.sql.StorableSQLVisitor;
import cn.addenda.footprints.core.pojo.Binary;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/9/24 9:51
 */
public class InsertPsDelegate extends AbstractPsDelegate {

    public InsertPsDelegate(CdcConnection cdcConnection, PreparedStatement ps,
                            TableConfig tableConfig, SQLStatement parameterizedSqlStatement, List<String> sqlList) {
        super(cdcConnection, ps, tableConfig, parameterizedSqlStatement, sqlList);
    }

    @Override
    protected <T> void doAssert(PsInvocation<T> pi) throws SQLException {
        if (!sqlHelper.checkValueInsertSqk((MySqlInsertStatement) parameterizedSqlStatement)) {
            throw new CdcException("insert sql only support 'insert into values' and 'insert into set' grammar. ");
        }
    }

    @Override
    public <T> T doExecute(PsInvocation<T> pi) throws SQLException {
        T invoke = pi.invoke();

        // -------------------------------------
        //  对于Statement模式来说，记录下来SQL就行了
        // -------------------------------------
        if (checkTableMode(TableConfig.CM_STATEMENT)) {
            List<String> statementCdcSqlList = new ArrayList<>(sqlList);
            executeCdcSql(TableConfig.CM_STATEMENT, statementCdcSqlList);
        }

        // ----------------------------------
        //  对于ROW模式，需要记录下来具体插入的值。
        // ----------------------------------
        if (checkTableMode(TableConfig.CM_ROW)) {

            List<Binary<List<MySqlInsertStatement>, List<Long>>> threeDSqlList = extractThreeDSqlList();

            for (Binary<List<MySqlInsertStatement>, List<Long>> twoDBinary : threeDSqlList) {
                List<MySqlInsertStatement> sqlList = twoDBinary.getF1();
                List<Long> keyList = twoDBinary.getF2();
                List<MySqlInsertStatement> cdcSqlStatementList = new ArrayList<>();

                // 每一个sqlList是一组，每组sql的结构一样。所以取0索引的sql拆分即可
                Binary<List<String>, List<Binary<String, SQLExpr>>> binary = sqlHelper.divideColumn(sqlList.get(0));
                List<String> dependentColumnList = binary.getF1();
                List<String> calculableColumnList = binary.getF2().stream().map(Binary::getF1).collect(Collectors.toList());

                // 从数据库里面取数据
                if (!dependentColumnList.isEmpty()) {
                    try (Statement statement = cdcConnection.getDelegate().createStatement()) {
                        Map<Long, Map<String, Object>> keyColumnValueMap = queryKeyColumnValueMap(statement, keyList, dependentColumnList);
                        for (int i = 0; i < keyList.size(); i++) {
                            Map<String, Object> columnValueMap = keyColumnValueMap.get(keyList.get(i));
                            cdcSqlStatementList.add(sqlHelper.setItemValue(sqlList.get(i), columnValueMap));
                        }
                    }
                }

                // 表达式计算器计算字段
                if (!calculableColumnList.isEmpty()) {
                    List<MySqlInsertStatement> tmpSqlList = new ArrayList<>(cdcSqlStatementList);
                    cdcSqlStatementList.clear();
                    for (MySqlInsertStatement sql : tmpSqlList) {
                        cdcSqlStatementList.add(sqlHelper.calculateColumnValue(sql, calculableColumnList));
                    }
                }

                List<String> cdcSqlList = cdcSqlStatementList.stream().map(this::toStorageSql).collect(Collectors.toList());
                executeCdcSql(TableConfig.CM_ROW, cdcSqlList);
            }
        }

        return invoke;
    }


    private List<Binary<List<MySqlInsertStatement>, List<Long>>> extractThreeDSqlList() throws SQLException {
        List<Binary<List<MySqlInsertStatement>, List<Long>>> threeD = new ArrayList<>();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        for (SQLStatement sqlStatement : sqlStatementList) {
            // insert A(name, age) values ('a', 1), ('b', 2 + 3) ->
            // insert A(name, age) values ('a', 1) 和 insert A(name, age) values ('b', 2 + 3) , 两组
            List<MySqlInsertStatement> list = sqlHelper.splitInsertMultipleRows((MySqlInsertStatement) sqlStatement);
            // 有几组insert
            if (threeD.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    Binary<List<MySqlInsertStatement>, List<Long>> binary = new Binary<>();
                    binary.setF1(new ArrayList<>());
                    binary.setF2(new ArrayList<>());
                    threeD.add(binary);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                MySqlInsertStatement item = list.get(i);
                Binary<List<MySqlInsertStatement>, List<Long>> listListBinary = threeD.get(i);
                Binary<MySqlInsertStatement, Long> stringLongBinary = fillKeyValueToInsertSql(item, generatedKeys);
                listListBinary.getF1().add(stringLongBinary.getF1());
                listListBinary.getF2().add(stringLongBinary.getF2());
            }
        }
        return threeD;
    }

    private Binary<MySqlInsertStatement, Long> fillKeyValueToInsertSql(
            MySqlInsertStatement mySqlInsertStatement, ResultSet generatedKeys) throws SQLException {
        Binary<MySqlInsertStatement, Long> result = new Binary<>();
        // 如果SQL中存在主键值，取SQL中的值。
        // 如果SQL中不存在主键值，取自增主键的值。
        // 不允许表没有主键。不支持联合主键。
        String value = sqlHelper.extractColumnValueFromInsertSql(mySqlInsertStatement, keyColumn);
        if (value == null) {
            if (generatedKeys.next()) {
                long keyValue = generatedKeys.getLong(1);
                result.setF1(sqlHelper.insertInjectItem(mySqlInsertStatement, keyColumn, keyValue));
                result.setF2(keyValue);
            } else {
                throw new CdcException("Cannot get key column value from sql or generatedKey, key column: " + keyColumn + ". ");
            }
        } else {
            long keyValue = Long.parseLong(value);
            result.setF1(sqlHelper.insertInjectItem(mySqlInsertStatement, keyColumn, keyValue));
            result.setF2(keyValue);
        }
        return result;
    }

}
