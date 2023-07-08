package cn.addenda.footprints.core.interceptor.baseentity;


import cn.addenda.footprints.core.visitor.item.InsertSelectAddItemMode;
import cn.addenda.footprints.core.visitor.item.UpdateItemMode;

/**
 * @author addenda
 * @since 2023/5/2 19:35
 */
public interface BaseEntityRewriter {

    String rewriteInsertSql(String sql, InsertSelectAddItemMode insertSelectAddItemMode,
                            boolean duplicateKeyUpdate, UpdateItemMode updateItemMode, boolean reportItemNameExists);

    String rewriteSelectSql(String sql, String masterView);

    String rewriteUpdateSql(String sql, UpdateItemMode updateItemMode, boolean reportItemNameExists);

}
