package cn.addenda.footprints.client.annotation;

import cn.addenda.footprints.core.interceptor.dynamicsql.DynamicItemOperation;
import cn.addenda.footprints.core.interceptor.dynamicsql.DynamicSQLContext;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/9 20:25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamicItem {

    DynamicItemOperation operation();

    String name() default DynamicSQLContext.ALL_TABLE;

    String itemName();

    String itemValue();

    Class<?> clazz();
}
