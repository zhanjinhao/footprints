package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the hyperbolic secant.
 */
@FunctionParameter(name = "value", nonZero = true)
public class SecHFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: sech(x) = 1 / cosh(x) */
        return expression.convertDoubleValue(
                1 / Math.cosh(parameterValues[0].getNumberValue().doubleValue()));
    }
}
