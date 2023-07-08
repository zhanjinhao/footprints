package cn.addenda.footprints.client.spring.aop.baseentity;

import cn.addenda.footprints.client.spring.aop.FootprintsAopMode;
import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
import cn.addenda.footprints.core.visitor.item.InsertSelectAddItemMode;
import cn.addenda.footprints.core.visitor.item.UpdateItemMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/11 21:16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BaseEntitySelector.class})
public @interface EnableBaseEntity {

    int order() default Ordered.LOWEST_PRECEDENCE;

    FootprintsAopMode footprintsAopMode() default FootprintsAopMode.PROXY_CONFIG;

    boolean removeEnter() default true;

    /**
     * 原始SQL -> BaseEntity SQL的重写器
     */
    String baseEntityRewriter() default NamedConfigurer.DEFAULT;

    boolean reportItemNameExists() default false;

    boolean duplicateKeyUpdate() default false;

    InsertSelectAddItemMode insertSelectAddItemMode() default InsertSelectAddItemMode.VALUE;

    UpdateItemMode updateItemMode() default UpdateItemMode.NOT_NULL;

}
