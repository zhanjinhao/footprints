package cn.addenda.footprints.expression.evalex.functions;

import cn.addenda.footprints.expression.evalex.functions.basic.IfFunction;
import cn.addenda.footprints.expression.evalex.functions.basic.MinFunction;
import cn.addenda.footprints.expression.evalex.parser.ASTNode;
import lombok.Builder;
import lombok.Value;

/**
 * Definition of a function parameter.
 */
@Value
@Builder
public class FunctionParameterDefinition {

    /**
     * Name of the parameter, useful for error messages etc.
     */
    String name;

    /**
     * Whether this parameter is a variable argument parameter (can be repeated).
     *
     * @see MinFunction for an example.
     */
    boolean isVarArg;

    /**
     * Set to true, the parameter will not be evaluated in advance, but the corresponding {@link
     * ASTNode} will be passed as a parameter value.
     *
     * @see IfFunction for an example.
     */
    boolean isLazy;

    /**
     * If the parameter does not allow zero values.
     */
    boolean nonZero;

    /**
     * If the parameter does not allow negative values.
     */
    boolean nonNegative;
}
