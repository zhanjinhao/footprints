package cn.addenda.footprints.client.annotation;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/8 18:41
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableBaseEntity {

    boolean value() default true;

}
