package cn.addenda.footprints.client.spring.aop.sqlcheck;

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
@Import(SQLCheckSelector.class)
public @interface EnableSQLCheck {

    int order() default Ordered.LOWEST_PRECEDENCE;

    FootprintsAopMode footprintsAopMode() default FootprintsAopMode.PROXY_CONFIG;

    boolean removeEnter() default true;

    String sqlChecker() default NamedConfigurer.DEFAULT;

    boolean checkAllColumn() default true;

    boolean checkExactIdentifier() default true;

    boolean checkDmlCondition() default true;
}
