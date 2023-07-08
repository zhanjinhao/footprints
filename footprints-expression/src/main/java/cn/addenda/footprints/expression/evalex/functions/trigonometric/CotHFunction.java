package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the hyperbolic co-tangent of a value.
 */
@FunctionParameter(name = "value", nonZero = true)
public class CotHFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: coth(x) = 1 / tanh(x) */
        return expression.convertDoubleValue(
                1 / Math.tanh(parameterValues[0].getNumberValue().doubleValue()));
    }
}
