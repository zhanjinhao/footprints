package cn.addenda.footprints.client.spring.aop.lockingreads;

import cn.addenda.footprints.client.spring.aop.FootprintsAopMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author addenda
 * @since 2023/6/10 18:02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LockingReadsSelector.class)
public @interface EnableLockingReads {

    FootprintsAopMode footprintsAopMode() default FootprintsAopMode.PROXY_CONFIG;

    boolean removeEnter() default true;

    int order() default Ordered.LOWEST_PRECEDENCE;

}
