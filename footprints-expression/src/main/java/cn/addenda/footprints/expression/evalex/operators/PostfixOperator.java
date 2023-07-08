package cn.addenda.footprints.expression.evalex.operators;

import java.lang.annotation.*;

import static cn.addenda.footprints.expression.evalex.operators.OperatorIfc.OPERATOR_PRECEDENCE_UNARY;

/**
 * The postfix operator annotation
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostfixOperator {

    /**
     * Operator precedence, usually one from the constants in {@link OperatorIfc}.
     */
    int precedence() default OPERATOR_PRECEDENCE_UNARY;

    /**
     * Operator associativity, defaults to <code>true</code>.
     */
    boolean leftAssociative() default true;
}
