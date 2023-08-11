package cn.addenda.footprints.client.spring.aop.baseentity;

import cn.addenda.footprints.client.spring.aop.AbstractFootprintsBeanPostProcessor;
import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
import cn.addenda.footprints.core.interceptor.baseentity.BaseEntityException;
import cn.addenda.footprints.core.interceptor.baseentity.BaseEntityInterceptor;
import cn.addenda.footprints.core.interceptor.baseentity.BaseEntityRewriter;
import cn.addenda.footprints.core.visitor.item.InsertSelectAddItemMode;
import cn.addenda.footprints.core.visitor.item.UpdateItemMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
@Slf4j
@Configuration
public class BaseEntityProxyConfiguration implements ImportAware {

    protected AnnotationAttributes annotationAttributes;

    Map<String, BaseEntityRewriterConfigurer> baseEntityRewriterConfigurerMap;

    private boolean removeEnter;
    private int order;
    private BaseEntityRewriter baseEntityRewriter;

    private boolean reportItemNameExists;

    private boolean duplicateKeyUpdate;

    private InsertSelectAddItemMode insertSelectAddItemMode;

    private UpdateItemMode updateItemMode;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableBaseEntity.class.getName(), false));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                    EnableBaseEntity.class.getName() + " is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    public BaseEntityBeanPostProcessor baseEntityBeanPostProcessor(BeanFactory beanFactory) {
        setBaseEntityRewriter(beanFactory);

        this.order = annotationAttributes.getNumber("order");
        this.removeEnter = annotationAttributes.getBoolean("removeEnter");
        this.insertSelectAddItemMode = annotationAttributes.getEnum("insertSelectAddItemMode");
        this.duplicateKeyUpdate = annotationAttributes.getBoolean("duplicateKeyUpdate");
        this.updateItemMode = annotationAttributes.getEnum("updateItemMode");
        this.reportItemNameExists = annotationAttributes.getBoolean("reportItemNameExists");
        return new BaseEntityBeanPostProcessor();
    }

    private void setBaseEntityRewriter(BeanFactory beanFactory) {
        String baseEntityWriterName = annotationAttributes.getString("baseEntityRewriter");
        BaseEntityRewriterConfigurer baseEntityRewriterConfigurer;
        if (baseEntityRewriterConfigurerMap != null &&
                (baseEntityRewriterConfigurer = baseEntityRewriterConfigurerMap.get(baseEntityWriterName)) != null) {
            baseEntityRewriter = baseEntityRewriterConfigurer.getBaseEntityRewriter();
        } else {
            try {
                baseEntityRewriter = beanFactory.getBean(baseEntityWriterName, BaseEntityRewriter.class);
            } catch (Exception e) {
                String msg = String.format("无法获取配置的%s：[%s]", BaseEntityRewriter.class.getName(), baseEntityWriterName);
                throw new BaseEntityException(msg, e);
            }
        }
        if (baseEntityRewriter == null) {
            String msg = String.format("无法获取配置的%s：[%s]", BaseEntityRewriter.class.getName(), baseEntityWriterName);
            throw new BaseEntityException(msg);
        }
    }

    @Autowired(required = false)
    void setBaseEntityRewriterConfigurers(List<BaseEntityRewriterConfigurer> configurers) {
        baseEntityRewriterConfigurerMap = configurers.stream().collect(Collectors.toMap(NamedConfigurer::getName, a -> a));
    }

    public class BaseEntityBeanPostProcessor extends AbstractFootprintsBeanPostProcessor<BaseEntityInterceptor> {
        @Override
        protected BaseEntityInterceptor getInterceptor() {
            return new BaseEntityInterceptor(removeEnter, baseEntityRewriter,
                    insertSelectAddItemMode, duplicateKeyUpdate, updateItemMode, reportItemNameExists);
        }

        @Override
        public int getOrder() {
            return order;
        }
    }
}
