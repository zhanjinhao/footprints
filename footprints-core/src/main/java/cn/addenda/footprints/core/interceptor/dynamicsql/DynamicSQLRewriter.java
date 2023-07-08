package cn.addenda.footprints.core.interceptor.dynamicsql;

import cn.addenda.footprints.core.visitor.item.InsertSelectAddItemMode;
import cn.addenda.footprints.core.visitor.item.Item;
import cn.addenda.footprints.core.visitor.item.UpdateItemMode;

/**
 * @author addenda
 * @since 2023/4/30 16:56
 */
public interface DynamicSQLRewriter {

    String tableAddJoinCondition(String sql, String tableName, String condition, boolean useSubQuery);

    String viewAddJoinCondition(String sql, String tableName, String condition, boolean useSubQuery);

    String tableAddWhereCondition(String sql, String tableName, String condition);

    String viewAddWhereCondition(String sql, String tableName, String condition);

    String insertAddItem(String sql, String tableName, Item item, InsertSelectAddItemMode insertSelectAddItemMode,
                         boolean duplicateKeyUpdate, UpdateItemMode updateItemMode);

    String updateAddItem(String sql, String tableName, Item item, UpdateItemMode updateItemMode);

}
