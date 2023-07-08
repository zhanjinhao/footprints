package cn.addenda.footprints.client.spring.aop.baseentity;

import cn.addenda.footprints.client.spring.aop.FootprintsAopMode;
import cn.addenda.footprints.client.spring.aop.AbstractFootprintsAopModeImportSelector;
import cn.addenda.footprints.core.interceptor.baseentity.BaseEntityException;
import org.springframework.context.annotation.AutoProxyRegistrar;

/**
 * @author addenda
 * @since 2022/9/29 13:50
 */
public class BaseEntitySelector extends AbstractFootprintsAopModeImportSelector<EnableBaseEntity> {

    @Override
    public String[] selectImports(FootprintsAopMode footprintsAopMode) {
        if (footprintsAopMode == FootprintsAopMode.ONLY_CONFIG) {
            return new String[]{
                    AutoProxyRegistrar.class.getName(),
                    BaseEntityConfigConfiguration.class.getName()};
        } else if (footprintsAopMode == FootprintsAopMode.PROXY_CONFIG) {
            return new String[]{
                    AutoProxyRegistrar.class.getName(),
                    BaseEntityConfigConfiguration.class.getName(),
                    BaseEntityProxyConfiguration.class.getName()};
        }
        String msg = String.format("%s 只可选 ONLY_CONFIG 和 PROXY_CONFIG 两种，当前是：[%s]。", FootprintsAopMode.class.getName(), footprintsAopMode);
        throw new BaseEntityException(msg);
    }

}
