package cn.addenda.footprints.client.annotation;

import cn.addenda.footprints.core.interceptor.dynamicsql.DynamicConditionOperation;
import cn.addenda.footprints.core.interceptor.dynamicsql.DynamicSQLContext;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/9 20:16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamicCondition {

    DynamicConditionOperation operation();

    String name() default DynamicSQLContext.ALL_TABLE;

    String condition();

}
