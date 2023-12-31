package cn.addenda.footprints.client.annotation;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/10 13:53
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UnCheckDmlCondition {
    boolean value() default true;
}
