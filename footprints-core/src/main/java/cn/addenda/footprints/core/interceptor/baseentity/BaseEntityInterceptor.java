package cn.addenda.footprints.core.interceptor.baseentity;

import cn.addenda.footprints.core.util.JdbcSQLUtils;
import cn.addenda.footprints.core.interceptor.ConnectionPrepareStatementInterceptor;
import cn.addenda.footprints.core.visitor.item.InsertSelectAddItemMode;
import cn.addenda.footprints.core.visitor.item.UpdateItemMode;
import cn.addenda.footprints.core.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 只要配置了拦截器就会执行SQL拦截。
 *
 * @author addenda
 * @since 2023/5/2 17:33
 */
@Slf4j
public class BaseEntityInterceptor extends ConnectionPrepareStatementInterceptor {

    private final BaseEntityRewriter baseEntityRewriter;
    private final InsertSelectAddItemMode defaultInsertSelectAddItemMode;
    private final boolean defaultDuplicateKeyUpdate;
    private final UpdateItemMode defaultUpdateItemMode;
    private final boolean defaultReportItemNameExists;

    public BaseEntityInterceptor(boolean removeEnter, BaseEntityRewriter baseEntityRewriter, InsertSelectAddItemMode insertSelectAddItemMode,
                                 boolean duplicateKeyUpdate, UpdateItemMode updateItemMode, boolean reportItemNameExists) {
        super(removeEnter);
        this.baseEntityRewriter = baseEntityRewriter;
        this.defaultInsertSelectAddItemMode = insertSelectAddItemMode;
        this.defaultDuplicateKeyUpdate = duplicateKeyUpdate;
        this.defaultUpdateItemMode = updateItemMode;
        this.defaultReportItemNameExists = reportItemNameExists;
    }

    @Override
    protected String process(String sql) {
        if (!BaseEntityContext.contextActive()) {
            return sql;
        }

        Boolean disable = JdbcSQLUtils.getOrDefault(BaseEntityContext.getDisable(), false);
        if (Boolean.TRUE.equals(disable)) {
            return sql;
        }
        log.debug("Base Entity, before sql rewriting: [{}].", sql);
        try {
            if (JdbcSQLUtils.isSelect(sql)) {
                sql = baseEntityRewriter.rewriteSelectSql(sql, BaseEntityContext.getMasterView());
            } else if (JdbcSQLUtils.isUpdate(sql)) {
                Boolean reportItemNameExists =
                        JdbcSQLUtils.getOrDefault(BaseEntityContext.getReportItemNameExists(), defaultReportItemNameExists);
                UpdateItemMode updateItemMode =
                        JdbcSQLUtils.getOrDefault(BaseEntityContext.getUpdateItemMode(), defaultUpdateItemMode);
                sql = baseEntityRewriter.rewriteUpdateSql(sql, updateItemMode, reportItemNameExists);
            } else if (JdbcSQLUtils.isInsert(sql)) {
                Boolean reportItemNameExists =
                        JdbcSQLUtils.getOrDefault(BaseEntityContext.getReportItemNameExists(), defaultReportItemNameExists);
                UpdateItemMode updateItemMode =
                        JdbcSQLUtils.getOrDefault(BaseEntityContext.getUpdateItemMode(), defaultUpdateItemMode);
                Boolean duplicateKeyUpdate =
                        JdbcSQLUtils.getOrDefault(BaseEntityContext.getDuplicateKeyUpdate(), defaultDuplicateKeyUpdate);
                InsertSelectAddItemMode insertSelectAddItemMode =
                        JdbcSQLUtils.getOrDefault(BaseEntityContext.getInsertSelectAddItemMode(), defaultInsertSelectAddItemMode);
                sql = baseEntityRewriter.rewriteInsertSql(sql, insertSelectAddItemMode, duplicateKeyUpdate, updateItemMode, reportItemNameExists);
            } else {
                throw new BaseEntityException("仅支持select、update、delete、insert语句，当前SQL：" + sql + "。");
            }
        } catch (Throwable throwable) {
            throw new BaseEntityException("基础字段填充时出错，SQL：" + sql + "。", ExceptionUtil.unwrapThrowable(throwable));
        }
        log.debug("Base Entity, after sql rewriting: [{}].", sql);
        return sql;
    }

    @Override
    public int order() {
        return MAX / 2 - 50000;
    }
}
