package cn.addenda.footprints.client.spring.aop.baseentity;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
public class BaseEntityConfigConfiguration implements ImportAware {

    protected AnnotationAttributes annotationAttributes;

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
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BaseEntityAdvisor baseEntityAdvisor() {
        BaseEntityAdvisor argResLogAdvisor = new BaseEntityAdvisor();
        argResLogAdvisor.setAdvice(new SpringBaseEntityInterceptor());
        argResLogAdvisor.setOrder(annotationAttributes.<Integer>getNumber("order"));
        return argResLogAdvisor;
    }

}
