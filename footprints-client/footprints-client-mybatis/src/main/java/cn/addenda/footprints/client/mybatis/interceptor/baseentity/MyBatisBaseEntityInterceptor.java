package cn.addenda.footprints.client.mybatis.interceptor.baseentity;

import cn.addenda.footprints.client.constant.Propagation;
import cn.addenda.footprints.client.mybatis.helper.MsIdExtractHelper;
import cn.addenda.footprints.client.mybatis.interceptor.AbstractFootprintsMybatisInterceptor;
import cn.addenda.footprints.client.utils.ConfigContextUtils;
import cn.addenda.footprints.core.pojo.Binary;
import cn.addenda.footprints.core.interceptor.baseentity.BaseEntityContext;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author addenda
 * @since 2023/6/8 19:18
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class MyBatisBaseEntityInterceptor extends AbstractFootprintsMybatisInterceptor {

    public MyBatisBaseEntityInterceptor(MsIdExtractHelper msIdExtractHelper) {
        super(msIdExtractHelper);
    }

    public MyBatisBaseEntityInterceptor() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Binary<String, Propagation> binary = extract(invocation);
        String msId = binary.getF1();
        Propagation propagation = binary.getF2();
        ConfigContextUtils.pushBaseEntity(propagation);

        try {
            ConfigContextUtils.configBaseEntity(propagation,
                    msIdExtractHelper.extractDisableBaseEntity(msId),
                    msIdExtractHelper.extractConfigMasterView(msId),
                    msIdExtractHelper.extractConfigDuplicateKeyUpdate(msId),
                    msIdExtractHelper.extractConfigUpdateItemMode(msId),
                    msIdExtractHelper.extractConfigReportItemNameExists(msId),
                    msIdExtractHelper.extractConfigInsertSelectAddItemMode(msId));
            return invocation.proceed();
        } finally {
            BaseEntityContext.pop();
        }
    }

}
