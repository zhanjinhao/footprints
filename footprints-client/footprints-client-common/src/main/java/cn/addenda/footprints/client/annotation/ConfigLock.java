package cn.addenda.footprints.client.annotation;

import cn.addenda.footprints.core.interceptor.lockingreads.LockingReadsContext;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/8 22:57
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigLock {

    String value() default LockingReadsContext.W_LOCK;

}
