package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the secant (in degrees).
 */
@FunctionParameter(name = "value", nonZero = true)
public class SecFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: sec(x) = 1 / cos(x) */
        return expression.convertDoubleValue(
                1 / Math.cos(Math.toRadians(parameterValues[0].getNumberValue().doubleValue())));
    }
}
