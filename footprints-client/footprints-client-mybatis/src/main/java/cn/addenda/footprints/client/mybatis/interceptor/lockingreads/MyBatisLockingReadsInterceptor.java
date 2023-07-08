package cn.addenda.footprints.client.mybatis.interceptor.lockingreads;

import cn.addenda.footprints.client.constant.Propagation;
import cn.addenda.footprints.client.mybatis.interceptor.AbstractFootprintsMybatisInterceptor;
import cn.addenda.footprints.client.utils.ConfigContextUtils;
import cn.addenda.footprints.core.pojo.Binary;
import cn.addenda.footprints.core.interceptor.lockingreads.LockingReadsContext;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author addenda
 * @since 2023/6/8 22:58
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MyBatisLockingReadsInterceptor extends AbstractFootprintsMybatisInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Binary<String, Propagation> binary = extract(invocation);
        String msId = binary.getF1();
        Propagation propagation = binary.getF2();

        ConfigContextUtils.pushLockingReads(propagation);
        try {
            ConfigContextUtils.configLockingReads(propagation,
                    msIdExtractHelper.extractConfigLock(msId));
            return invocation.proceed();
        } finally {
            LockingReadsContext.pop();
        }
    }

}
