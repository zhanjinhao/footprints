package cn.addenda.footprints.cdc.jdbc.sql;

import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.pojo.Binary;
import cn.addenda.footprints.expression.Calculator;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author addenda
 * @since 2022/9/29 8:44
 */
@AllArgsConstructor
@NoArgsConstructor
public class SqlHelper {

    private DataConvertorRegistry dataConvertorRegistry;

    private Calculator calculator;

    /**
     * 从 Insert SQL 里面提取出来字段名。
     */
    public String extractColumnValueFromInsertSql(MySqlInsertStatement insertStatement, String keyColumn) {
        List<SQLExpr> columns = insertStatement.getColumns();
        SQLInsertStatement.ValuesClause valuesClause = insertStatement.getValuesList().get(0);
        List<SQLExpr> values = valuesClause.getValues();
        for (int i = 0; i < columns.size(); i++) {
            SQLExpr sqlExpr = columns.get(i);
            if (keyColumn.equals(sqlExpr.toString())) {
                SQLExpr value = values.get(i);
                return dataConvertorRegistry.format(value);
            }
        }

        return null;
    }


    /**
     * 1、不能修改主键值 <br/>
     * 2、不能修改条件列
     */
    public boolean checkStableUpdateSql(MySqlUpdateStatement updateStatement, String keyColumn) {
        UpdateWhereIdentifierCollectionVisitor identifierCollectionVisitor =
                new UpdateWhereIdentifierCollectionVisitor(updateStatement);
        identifierCollectionVisitor.visit();
        Set<String> conditionColumnNameList = identifierCollectionVisitor.getIdentifierSet();

        // where 里的条件列 和 key列 不能在更新列里面出现
        List<SQLUpdateSetItem> items = updateStatement.getItems();
        for (SQLUpdateSetItem sqlUpdateSetItem : items) {
            String columnName = String.valueOf(sqlUpdateSetItem.getColumn());
            if (keyColumn.equals(columnName) || conditionColumnNameList.contains(columnName)) {
                return false;
            }
        }

        return true;
    }

    public boolean checkValueInsertSqk(MySqlInsertStatement insertStatement) {
        List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        return valuesList != null && !valuesList.isEmpty();
    }

    /**
     * @return f1: dependentColumnNameList; f2: calculableColumnNameList
     */
    public Binary<List<String>, List<Binary<String, SQLExpr>>> divideColumn(MySqlUpdateStatement updateStatement) {

        List<String> dependentColumnNameList = new ArrayList<>();
        List<Binary<String, SQLExpr>> calculableColumnNameList = new ArrayList<>();

        List<SQLUpdateSetItem> items = updateStatement.getItems();
        for (SQLUpdateSetItem item : items) {
            SQLExpr column = item.getColumn();
            SQLExpr value = item.getValue();
            if (calculator.isLiteral(value)) {

            } else if (calculator.computable(value)) {
                calculableColumnNameList.add(new Binary<>(column.toString(), value));
            } else {
                dependentColumnNameList.add(column.toString());
            }
        }
        return new Binary<>(dependentColumnNameList, calculableColumnNameList);
    }

    /**
     * @return f1: dependentColumnNameList; f2: calculableColumnNameList
     */
    public Binary<List<String>, List<Binary<String, SQLExpr>>> divideColumn(MySqlInsertStatement insertStatement) {

        List<String> dependentColumnNameList = new ArrayList<>();
        List<Binary<String, SQLExpr>> calculableColumnNameList = new ArrayList<>();

        List<SQLExpr> columns = insertStatement.getColumns();
        List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        SQLInsertStatement.ValuesClause valuesClause = valuesList.get(0);

        for (int i = 0; i < columns.size(); i++) {
            SQLExpr column = columns.get(i);
            SQLExpr value = valuesClause.getValues().get(i);
            if (calculator.isLiteral(value)) {

            } else if (calculator.computable(value)) {
                calculableColumnNameList.add(new Binary<>(column.toString(), value));
            } else {
                dependentColumnNameList.add(column.toString());
            }
        }
        return new Binary<>(dependentColumnNameList, calculableColumnNameList);
    }

    public List<MySqlInsertStatement> splitInsertMultipleRows(MySqlInsertStatement insertStatement) {
        List<MySqlInsertStatement> singleRowList = new ArrayList<>();
        List<SQLInsertStatement.ValuesClause> valuesClauseList = insertStatement.getValuesList();
        for (SQLInsertStatement.ValuesClause valuesClause : valuesClauseList) {
            SQLInsertStatement clone = insertStatement.clone();
            List<SQLInsertStatement.ValuesClause> valuesList = clone.getValuesList();
            valuesList.clear();
            valuesList.add(new SQLInsertStatement.ValuesClause(valuesClause.getValues()));
            singleRowList.add((MySqlInsertStatement) clone);
        }

        return singleRowList;
    }

    public MySqlInsertStatement insertInjectItem(
            MySqlInsertStatement insertStatement, String keyColumn, long keyValue) {
        List<SQLExpr> columns = insertStatement.getColumns();
        columns.add(new SQLIdentifierExpr(keyColumn));
        List<SQLExpr> values = insertStatement.getValuesList().get(0).getValues();
        values.add(dataConvertorRegistry.parse(keyValue));
        return insertStatement;
    }

    public MySqlUpdateStatement setItemValue(
            MySqlUpdateStatement updateStatement, Map<String, Object> columnValueMap) {
        List<SQLUpdateSetItem> items = updateStatement.getItems();
        for (SQLUpdateSetItem item : items) {
            SQLExpr column = item.getColumn();
            SQLExpr value = dataConvertorRegistry.parse(columnValueMap.get(column.toString()));
            if (value != null) {
                item.setValue(value);
            }
        }

        return updateStatement;
    }

    public MySqlInsertStatement setItemValue(
            MySqlInsertStatement insertStatement, Map<String, Object> columnValueMap) {
        List<SQLExpr> columns = insertStatement.getColumns();
        SQLInsertStatement.ValuesClause valuesClause = insertStatement.getValues();
        List<SQLExpr> values = valuesClause.getValues();
        for (int i = 0; i < columns.size(); i++) {
            SQLExpr column = columns.get(i);
            SQLExpr value = dataConvertorRegistry.parse(columnValueMap.get(column.toString()));
            if (value != null) {
                values.set(i, value);
            }
        }

        return insertStatement;
    }

    public MySqlUpdateStatement calculateColumnValue(
            MySqlUpdateStatement updateStatement, List<String> calculableColumnList) {
        List<SQLUpdateSetItem> items = updateStatement.getItems();
        for (SQLUpdateSetItem item : items) {
            SQLExpr column = item.getColumn();
            if (calculableColumnList.contains(column.toString())) {
                SQLExpr value = item.getValue();
                Object calculate = calculator.calculate(value);
                item.setValue(dataConvertorRegistry.parse(calculate));
            }
        }
        return updateStatement;
    }

    public MySqlInsertStatement calculateColumnValue(
            MySqlInsertStatement insertStatement, List<String> calculableColumnList) {
        List<SQLExpr> columns = insertStatement.getColumns();
        SQLInsertStatement.ValuesClause valuesClause = insertStatement.getValues();
        List<SQLExpr> values = valuesClause.getValues();
        for (int i = 0; i < columns.size(); i++) {
            SQLExpr column = columns.get(i);
            if (calculableColumnList.contains(column.toString())) {
                SQLExpr value = values.get(i);
                Object calculate = calculator.calculate(value);
                values.set(i, dataConvertorRegistry.parse(calculate));
            }
        }
        return insertStatement;
    }

}
