package cn.addenda.footprints.core.interceptor.baseentity;

import cn.addenda.footprints.core.convertor.DataConvertorRegistry;
import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import cn.addenda.footprints.core.util.DruidSQLUtils;
import cn.addenda.footprints.core.visitor.item.*;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author addenda
 * @since 2023/5/2 19:35
 */
@Slf4j
public class DruidBaseEntityRewriter extends AbstractBaseEntityRewriter {

    public DruidBaseEntityRewriter(List<String> included, List<String> notIncluded, BaseEntitySource baseEntitySource, DataConvertorRegistry dataConvertorRegistry) {
        super(baseEntitySource, included, notIncluded, dataConvertorRegistry);
        if (included == null) {
            log.warn("未声明需填充的基础字段的表集合，所有的表都会进行基础字段填充改写！");
        }
    }

    public DruidBaseEntityRewriter() {
        this(null, null, new DefaultBaseEntitySource(), new DefaultDataConvertorRegistry());
    }

    @Override
    public String rewriteInsertSql(String sql, InsertSelectAddItemMode insertSelectAddItemMode,
                                   boolean duplicateKeyUpdate, UpdateItemMode updateItemMode, boolean reportItemNameExists) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {

            for (int i = 0; i < INSERT_COLUMN_NAME_LIST.size(); i++) {
                String columnName = INSERT_COLUMN_NAME_LIST.get(i);
                String fieldName = INSERT_FIELD_NAME_LIST.get(i);
                Item item = new Item(columnName, baseEntitySource.get(fieldName));
                new InsertAddItemVisitor((MySqlInsertStatement) sqlStatement, included, notIncluded,
                        dataConvertorRegistry, item, reportItemNameExists, insertSelectAddItemMode,
                        duplicateKeyUpdate && UPDATE_FIELD_NAME_LIST.contains(fieldName), updateItemMode).visit();
            }

            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

    @Override
    public String rewriteSelectSql(String sql, String masterView) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {
            for (String column : INSERT_COLUMN_NAME_LIST) {
                new SelectAddItemVisitor((SQLSelectStatement) sqlStatement, included, notIncluded,
                        dataConvertorRegistry, masterView, column, false).visit();
            }
            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

    @Override
    public String rewriteUpdateSql(String sql, UpdateItemMode updateItemMode, boolean reportItemNameExists) {
        return DruidSQLUtils.statementMerge(sql, sqlStatement -> {
            for (int i = 0; i < UPDATE_COLUMN_NAME_LIST.size(); i++) {
                String columnName = UPDATE_COLUMN_NAME_LIST.get(i);
                String fieldName = UPDATE_FIELD_NAME_LIST.get(i);
                Item item = new Item(columnName, baseEntitySource.get(fieldName));
                new UpdateAddItemVisitor((MySqlUpdateStatement) sqlStatement, included,
                        notIncluded, dataConvertorRegistry, item, reportItemNameExists, updateItemMode).visit();
            }

            return DruidSQLUtils.toLowerCaseSQL(sqlStatement);
        });
    }

}
