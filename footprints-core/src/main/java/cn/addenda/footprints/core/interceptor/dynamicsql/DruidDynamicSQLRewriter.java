package cn.addenda.footprints.core.interceptor.dynamicsql;

import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.util.DruidSQLUtils;
import cn.addenda.footprints.core.visitor.condition.TableAddJoinConditionVisitor;
import cn.addenda.footprints.core.visitor.condition.TableAddWhereConditionVisitor;
import cn.addenda.footprints.core.visitor.condition.ViewAddJoinConditionVisitor;
import cn.addenda.footprints.core.visitor.condition.ViewAddWhereConditionVisitor;
import cn.addenda.footprints.core.util.ArrayUtils;
import cn.addenda.footprints.core.visitor.item.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;

/**
 * @author addenda
 * @since 2023/4/30 16:56
 */
public class DruidDynamicSQLRewriter implements DynamicSQLRewriter {

    private DataConvertorRegistry dataConvertorRegistry;

    public DruidDynamicSQLRewriter(DataConvertorRegistry dataConvertorRegistry) {
        this.dataConvertorRegistry = dataConvertorRegistry;
    }

    @Override
    public String tableAddJoinCondition(
            String sql, String tableName, String condition, boolean useSubQuery) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {
            sqlStatement.accept(new TableAddJoinConditionVisitor(tableName, condition, useSubQuery));
            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

    @Override
    public String viewAddJoinCondition(
            String sql, String tableName, String condition, boolean useSubQuery) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {
            sqlStatement.accept(new ViewAddJoinConditionVisitor(tableName, condition, useSubQuery));
            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

    @Override
    public String tableAddWhereCondition(String sql, String tableName, String condition) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {
            sqlStatement.accept(new TableAddWhereConditionVisitor(tableName, condition));
            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

    @Override
    public String viewAddWhereCondition(String sql, String tableName, String condition) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {
            sqlStatement.accept(new ViewAddWhereConditionVisitor(tableName, condition));
            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

    @Override
    public String insertAddItem(String sql, String tableName, Item item, InsertSelectAddItemMode insertSelectAddItemMode,
                                boolean duplicateKeyUpdate, UpdateItemMode updateItemMode) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {
            MySqlInsertStatement insertStatement = (MySqlInsertStatement) sqlStatement;
            new InsertAddItemVisitor(
                    insertStatement, tableName == null ? null : ArrayUtils.asArrayList(tableName), null,
                    dataConvertorRegistry, item,
                    true, insertSelectAddItemMode, duplicateKeyUpdate, updateItemMode).visit();
            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

    @Override
    public String updateAddItem(String sql, String tableName, Item item, UpdateItemMode updateItemMode) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {
            MySqlUpdateStatement updateStatement = (MySqlUpdateStatement) sqlStatement;

            new UpdateAddItemVisitor(
                    updateStatement, tableName == null ? null : ArrayUtils.asArrayList(tableName), null,
                    dataConvertorRegistry, item,
                    true, updateItemMode).visit();
            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

}
