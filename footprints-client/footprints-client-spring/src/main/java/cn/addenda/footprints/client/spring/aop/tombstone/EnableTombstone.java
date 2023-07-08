package cn.addenda.footprints.client.spring.aop.tombstone;

import cn.addenda.footprints.client.spring.aop.FootprintsAopMode;
import cn.addenda.footprints.client.spring.aop.NamedConfigurer;
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
@Import(TombstoneSelector.class)
public @interface EnableTombstone {

    int order() default Ordered.LOWEST_PRECEDENCE;

    FootprintsAopMode footprintsAopMode() default FootprintsAopMode.PROXY_CONFIG;

    boolean removeEnter() default true;

    String tombstoneSqlRewriter() default NamedConfigurer.DEFAULT;

    boolean joinUseSubQuery() default false;

}
