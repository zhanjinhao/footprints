package cn.addenda.footprints.expression.evalex.operators;

import java.lang.annotation.*;

/**
 * The infix operator annotation
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InfixOperator {

    /**
     * Operator precedence, usually one from the constants in {@link OperatorIfc}.
     */
    int precedence();

    /**
     * Operator associativity, defaults to <code>true</code>.
     */
    boolean leftAssociative() default true;
}
