package cn.addenda.footprints.client.spring.aop.lockingreads;

import cn.addenda.footprints.client.spring.aop.AbstractFootprintsAopModeImportSelector;
import cn.addenda.footprints.client.spring.aop.FootprintsAopMode;
import cn.addenda.footprints.core.interceptor.lockingreads.LockingReadsException;
import org.springframework.context.annotation.AutoProxyRegistrar;

/**
 * @author addenda
 * @since 2022/9/29 13:50
 */
public class LockingReadsSelector extends AbstractFootprintsAopModeImportSelector<EnableLockingReads> {

    @Override
    protected String[] selectImports(FootprintsAopMode footprintsAopMode) {
        if (footprintsAopMode == FootprintsAopMode.ONLY_CONFIG) {
            return new String[]{
                    AutoProxyRegistrar.class.getName(),
                    LockingReadsConfigConfiguration.class.getName()};
        } else if (footprintsAopMode == FootprintsAopMode.PROXY_CONFIG) {
            return new String[]{
                    AutoProxyRegistrar.class.getName(),
                    LockingReadsConfigConfiguration.class.getName(),
                    LockingReadsProxyConfiguration.class.getName()};
        }
        String msg = String.format("FootprintsAopMode 只可选 ONLY_CONFIG 和 PROXY_CONFIG 两种，当前是：[%s]。", footprintsAopMode);
        throw new LockingReadsException(msg);
    }
}
