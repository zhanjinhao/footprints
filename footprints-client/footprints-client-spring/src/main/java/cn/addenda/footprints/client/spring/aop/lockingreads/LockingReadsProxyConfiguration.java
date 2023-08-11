package cn.addenda.footprints.client.spring.aop.lockingreads;

import cn.addenda.footprints.client.spring.aop.AbstractFootprintsBeanPostProcessor;
import cn.addenda.footprints.core.interceptor.lockingreads.LockingReadsInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
@Configuration
public class LockingReadsProxyConfiguration implements ImportAware {

    protected AnnotationAttributes annotationAttributes;

    private int order;
    private boolean removeEnter;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLockingReads.class.getName(), false));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                    EnableLockingReads.class.getName() + " is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LockingReadsPostProcessor lockingReadsPostProcessor() {
        this.order = annotationAttributes.getNumber("order");
        this.removeEnter = annotationAttributes.getBoolean("removeEnter");
        return new LockingReadsPostProcessor();
    }

    private class LockingReadsPostProcessor extends AbstractFootprintsBeanPostProcessor<LockingReadsInterceptor> {

        @Override
        protected LockingReadsInterceptor getInterceptor() {
            return new LockingReadsInterceptor(removeEnter);
        }

        @Override
        public int getOrder() {
            return order;
        }
    }

}
