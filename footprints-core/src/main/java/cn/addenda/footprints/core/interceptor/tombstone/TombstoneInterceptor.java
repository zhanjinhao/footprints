package cn.addenda.footprints.core.interceptor.tombstone;

import cn.addenda.footprints.core.util.JdbcSQLUtils;
import cn.addenda.footprints.core.interceptor.ConnectionPrepareStatementInterceptor;
import cn.addenda.footprints.core.interceptor.baseentity.BaseEntityException;
import cn.addenda.footprints.core.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 只要配置了拦截器就会执行SQL拦截。
 *
 * @author addenda
 * @since 2023/5/2 17:33
 */
@Slf4j
public class TombstoneInterceptor extends ConnectionPrepareStatementInterceptor {

    private final TombstoneSqlRewriter tombstoneSqlRewriter;
    private final boolean defaultJoinUseSubQuery;

    public TombstoneInterceptor(boolean removeEnter, TombstoneSqlRewriter tombstoneSqlRewriter, boolean joinUseSubQuery) {
        super(removeEnter);
        this.tombstoneSqlRewriter = tombstoneSqlRewriter;
        this.defaultJoinUseSubQuery = joinUseSubQuery;
    }

    @Override
    protected String process(String sql) {
        if (!TombstoneContext.contextActive()) {
            return sql;
        }

        Boolean disable = JdbcSQLUtils.getOrDefault(TombstoneContext.getDisable(), false);
        if (Boolean.TRUE.equals(disable)) {
            return sql;
        }
        log.debug("Tombstone, before sql rewriting: [{}].", removeEnter(sql));
        try {
            if (JdbcSQLUtils.isDelete(sql)) {
                sql = tombstoneSqlRewriter.rewriteDeleteSql(sql);
            } else if (JdbcSQLUtils.isSelect(sql)) {
                Boolean useSubQuery =
                        JdbcSQLUtils.getOrDefault(TombstoneContext.getJoinUseSubQuery(), defaultJoinUseSubQuery);
                sql = tombstoneSqlRewriter.rewriteSelectSql(sql, useSubQuery);
            } else if (JdbcSQLUtils.isUpdate(sql)) {
                sql = tombstoneSqlRewriter.rewriteUpdateSql(sql);
            } else if (JdbcSQLUtils.isInsert(sql)) {
                Boolean useSubQuery =
                        JdbcSQLUtils.getOrDefault(TombstoneContext.getJoinUseSubQuery(), defaultJoinUseSubQuery);
                sql = tombstoneSqlRewriter.rewriteInsertSql(sql, useSubQuery);
            } else {
                throw new BaseEntityException("仅支持select、update、delete、insert语句，当前SQL：" + sql + "。");
            }
        } catch (Throwable throwable) {
            String msg = String.format("物理删除改写为逻辑删除时出错，SQL：[%s]。", removeEnter(sql));
            throw new TombstoneException(msg, ExceptionUtil.unwrapThrowable(throwable));
        }
        log.debug("Tombstone, after sql rewriting: [{}].", removeEnter(sql));
        return sql;
    }

    @Override
    public int order() {
        // baseEntity不会处理delete语句，所以tombstone需要先于baseEntity执行
        return MAX / 2 - 70000;
    }

}
