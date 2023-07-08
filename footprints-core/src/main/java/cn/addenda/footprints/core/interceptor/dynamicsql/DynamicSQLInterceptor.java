package cn.addenda.footprints.core.interceptor.dynamicsql;

import cn.addenda.footprints.core.convertor.DefaultDataConvertorRegistry;
import cn.addenda.footprints.core.interceptor.ConnectionPrepareStatementInterceptor;
import cn.addenda.footprints.core.util.JdbcSQLUtils;
import cn.addenda.footprints.core.visitor.item.InsertSelectAddItemMode;
import cn.addenda.footprints.core.visitor.item.Item;
import cn.addenda.footprints.core.visitor.item.UpdateItemMode;
import cn.addenda.footprints.core.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 配置了拦截器 且 DynamicSQLContext配置了条件 才会执行。
 *
 * @author addenda
 * @since 2023/4/30 16:30
 */
@Slf4j
public class DynamicSQLInterceptor extends ConnectionPrepareStatementInterceptor {


    private final DynamicSQLRewriter dynamicSQLRewriter;
    private final InsertSelectAddItemMode defaultInsertSelectAddItemMode;
    private final boolean defaultDuplicateKeyUpdate;
    private final UpdateItemMode defaultUpdateItemMode;
    private final boolean defaultJoinUseSubQuery;

    public DynamicSQLInterceptor(boolean removeEnter, DynamicSQLRewriter dynamicSQLRewriter, InsertSelectAddItemMode insertSelectAddItemMode,
                                 boolean duplicateKeyUpdate, UpdateItemMode updateItemMode, boolean joinUseSubQuery) {
        super(removeEnter);
        this.dynamicSQLRewriter = dynamicSQLRewriter;
        this.defaultInsertSelectAddItemMode = insertSelectAddItemMode == null ? InsertSelectAddItemMode.VALUE : insertSelectAddItemMode;
        this.defaultDuplicateKeyUpdate = duplicateKeyUpdate;
        this.defaultUpdateItemMode = updateItemMode == null ? UpdateItemMode.NOT_NULL : updateItemMode;
        this.defaultJoinUseSubQuery = joinUseSubQuery;
    }

    public DynamicSQLInterceptor() {
        this(true, new DruidDynamicSQLRewriter(new DefaultDataConvertorRegistry()), InsertSelectAddItemMode.VALUE, false,
                UpdateItemMode.NOT_NULL, false);
    }

    protected String process(String sql) {
        Map<String, List<Map.Entry<DynamicConditionOperation, String>>> conditionMap = DynamicSQLContext.getConditionMap();
        Map<String, List<Map.Entry<DynamicItemOperation, Item>>> itemMap = DynamicSQLContext.getItemMap();

        if (conditionMap == null && itemMap == null) {
            return sql;
        }
        log.debug("Dynamic Condition, before sql rewriting: [{}].", removeEnter(sql));
        String newSql;
        try {
            newSql = doProcess(removeEnter(sql), conditionMap, itemMap);
        } catch (Throwable throwable) {
            String msg = String.format("拼装动态条件时出错，SQL：[%s]，conditionMap: [%s]，itemMap：[%s]。", removeEnter(sql), conditionMap, itemMap);
            throw new DynamicSQLException(msg, ExceptionUtil.unwrapThrowable(throwable));
        }

        log.debug("Dynamic Condition, after sql rewriting: [{}].", newSql);
        return newSql;
    }

    private String doProcess(String sql, Map<String, List<Map.Entry<DynamicConditionOperation, String>>> conditionMap,
                             Map<String, List<Map.Entry<DynamicItemOperation, Item>>> itemMap) {
        String newSql = sql;

        // condition 过滤条件
        if (conditionMap != null && !conditionMap.isEmpty()) {
            for (Map.Entry<String, List<Map.Entry<DynamicConditionOperation, String>>> tableEntry : conditionMap.entrySet()) {
                String tableName = tableEntry.getKey();
                if (DynamicSQLContext.ALL_TABLE.equals(tableName)) {
                    tableName = null;
                }
                for (Map.Entry<DynamicConditionOperation, String> operationEntry : tableEntry.getValue()) {
                    DynamicConditionOperation operation = operationEntry.getKey();
                    String condition = operationEntry.getValue();
                    if (DynamicConditionOperation.TABLE_ADD_JOIN_CONDITION.equals(operation) && !JdbcSQLUtils.isInsert(newSql)) {
                        Boolean useSubQuery =
                                JdbcSQLUtils.getOrDefault(DynamicSQLContext.getJoinUseSubQuery(), defaultJoinUseSubQuery);
                        newSql = dynamicSQLRewriter.tableAddJoinCondition(newSql, tableName, condition, useSubQuery);
                    } else if (DynamicConditionOperation.VIEW_ADD_JOIN_CONDITION.equals(operation) && !JdbcSQLUtils.isInsert(newSql)) {
                        Boolean useSubQuery =
                                JdbcSQLUtils.getOrDefault(DynamicSQLContext.getJoinUseSubQuery(), defaultJoinUseSubQuery);
                        newSql = dynamicSQLRewriter.viewAddJoinCondition(newSql, tableName, condition, useSubQuery);
                    } else if (DynamicConditionOperation.TABLE_ADD_WHERE_CONDITION.equals(operation) && !JdbcSQLUtils.isInsert(newSql)) {
                        newSql = dynamicSQLRewriter.tableAddWhereCondition(newSql, tableName, condition);
                    } else if (DynamicConditionOperation.VIEW_ADD_WHERE_CONDITION.equals(operation) && !JdbcSQLUtils.isInsert(newSql)) {
                        newSql = dynamicSQLRewriter.viewAddWhereCondition(newSql, tableName, condition);
                    } else {
                        String msg = String.format("不支持的SQL添加条件操作类型：[%s]，SQL：[%s]。", operation, removeEnter(sql));
                        throw new UnsupportedOperationException(msg);
                    }
                }
            }
        }

        // item 过滤条件
        if (itemMap != null && !itemMap.isEmpty()) {
            for (Map.Entry<String, List<Map.Entry<DynamicItemOperation, Item>>> tableEntry : itemMap.entrySet()) {
                String tableName = tableEntry.getKey();
                if (DynamicSQLContext.ALL_TABLE.equals(tableName)) {
                    tableName = null;
                }
                for (Map.Entry<DynamicItemOperation, Item> operationEntry : tableEntry.getValue()) {
                    DynamicItemOperation operation = operationEntry.getKey();
                    Item item = operationEntry.getValue();
                    UpdateItemMode updateItemMode =
                            JdbcSQLUtils.getOrDefault(DynamicSQLContext.getUpdateItemMode(), defaultUpdateItemMode);
                    if (DynamicItemOperation.INSERT_ADD_ITEM.equals(operation) && JdbcSQLUtils.isInsert(newSql)) {
                        Boolean duplicateKeyUpdate =
                                JdbcSQLUtils.getOrDefault(DynamicSQLContext.getDuplicateKeyUpdate(), defaultDuplicateKeyUpdate);
                        InsertSelectAddItemMode insertSelectAddItemMode =
                                JdbcSQLUtils.getOrDefault(DynamicSQLContext.getInsertSelectAddItemMode(), defaultInsertSelectAddItemMode);
                        newSql = dynamicSQLRewriter.insertAddItem(newSql, tableName, item, insertSelectAddItemMode, duplicateKeyUpdate, updateItemMode);
                    } else if (DynamicItemOperation.UPDATE_ADD_ITEM.equals(operation) && JdbcSQLUtils.isUpdate(newSql)) {
                        newSql = dynamicSQLRewriter.updateAddItem(newSql, tableName, item, updateItemMode);
                    } else {
                        String msg = String.format("不支持的SQL添加item作类型：[%s]，SQL：[%s]。", operation, removeEnter(sql));
                        throw new UnsupportedOperationException(msg);
                    }
                }

            }
        }

        return newSql;
    }

    @Override
    public int order() {
        return MAX / 2 - 60000;
    }


}
