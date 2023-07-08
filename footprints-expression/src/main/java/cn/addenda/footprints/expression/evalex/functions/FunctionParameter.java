package cn.addenda.footprints.expression.evalex.functions;

import java.lang.annotation.*;

/**
 * Annotation to define a function parameter.
 */
@Documented
@Target(ElementType.TYPE)
@Repeatable(FunctionParameters.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionParameter {

    /**
     * The parameter name.
     */
    String name();

    /**
     * If the parameter is lazily evaluated. Defaults to false.
     */
    boolean isLazy() default false;

    /**
     * If the parameter is a variable arg type (repeatable). Defaults to false.
     */
    boolean isVarArg() default false;

    /**
     * If the parameter does not allow zero values.
     */
    boolean nonZero() default false;

    /**
     * If the parameter does not allow negative values.
     */
    boolean nonNegative() default false;
}
