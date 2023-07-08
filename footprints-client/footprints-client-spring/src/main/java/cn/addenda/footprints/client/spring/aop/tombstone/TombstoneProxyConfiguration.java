package cn.addenda.footprints.client.spring.aop.tombstone;

import cn.addenda.footprints.client.spring.aop.AbstractFootprintsBeanPostProcessor;
import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
import cn.addenda.footprints.core.interceptor.tombstone.TombstoneException;
import cn.addenda.footprints.core.interceptor.tombstone.TombstoneInterceptor;
import cn.addenda.footprints.core.interceptor.tombstone.TombstoneSqlRewriter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @since 2022/9/29 13:51
 */
@Configuration
public class TombstoneProxyConfiguration implements ImportAware {

    protected AnnotationAttributes annotationAttributes;
    private Map<String, TombstoneRewriterConfigurer> tombstoneRewriterConfigurerMap;
    private boolean removeEnter;
    private TombstoneSqlRewriter tombstoneSqlRewriter;
    private boolean joinUseSubQuery;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableTombstone.class.getName(), false));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                    EnableTombstone.class.getName() + " is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TombstoneBeanPostProcessor tombstoneBeanPostProcessor(BeanFactory beanFactory) {
        setTombstoneSqlRewriter(beanFactory);

        this.removeEnter = annotationAttributes.getBoolean("removeEnter");
        this.joinUseSubQuery = annotationAttributes.getBoolean("joinUseSubQuery");
        return new TombstoneBeanPostProcessor();
    }

    private void setTombstoneSqlRewriter(BeanFactory beanFactory) {
        String tombstoneSqlRewriterName = annotationAttributes.getString("tombstoneSqlRewriter");
        TombstoneRewriterConfigurer tombstoneRewriterConfigurer;
        if (tombstoneRewriterConfigurerMap != null &&
                (tombstoneRewriterConfigurer = tombstoneRewriterConfigurerMap.get(tombstoneSqlRewriterName)) != null) {
            tombstoneSqlRewriter = tombstoneRewriterConfigurer.getTombstoneSqlRewriter();
        } else {
            try {
                tombstoneSqlRewriter = beanFactory.getBean(tombstoneSqlRewriterName, TombstoneSqlRewriter.class);
            } catch (Exception e) {
                String msg = String.format("无法获取配置的%s：[%s]", TombstoneSqlRewriter.class.getName(), tombstoneSqlRewriterName);
                throw new TombstoneException(msg, e);
            }
        }
        if (tombstoneSqlRewriter == null) {
            String msg = String.format("无法获取配置的%s：[%s]", TombstoneSqlRewriter.class.getName(), tombstoneSqlRewriterName);
            throw new TombstoneException(msg);
        }
    }

    @Autowired(required = false)
    void setConfigurers(List<TombstoneRewriterConfigurer> configurers) {
        tombstoneRewriterConfigurerMap = configurers.stream().collect(Collectors.toMap(NamedConfigurer::getName, a -> a));
    }

    private class TombstoneBeanPostProcessor extends AbstractFootprintsBeanPostProcessor<TombstoneInterceptor> {

        @Override
        protected TombstoneInterceptor getInterceptor() {
            return new TombstoneInterceptor(removeEnter, tombstoneSqlRewriter, joinUseSubQuery);
        }
    }

}
