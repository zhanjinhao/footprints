package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the secant (in radians).
 */
@FunctionParameter(name = "value", nonZero = true)
public class SecRFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: sec(x) = 1 / cos(x) */
        return expression.convertDoubleValue(
                1 / Math.cos(parameterValues[0].getNumberValue().doubleValue()));
    }
}
