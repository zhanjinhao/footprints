package cn.addenda.footprints.client.annotation;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/9 20:15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamicConditions {

    DynamicCondition[] value();

}
