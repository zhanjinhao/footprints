package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the hyperbolic arc-sine.
 */
@FunctionParameter(name = "value")
public class AtanHFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues)
            throws EvaluationException {

        /* Formula: atanh(x) = 0.5*ln((1 + x)/(1 - x)) */
        double value = parameterValues[0].getNumberValue().doubleValue();
        if (Math.abs(value) >= 1) {
            throw new EvaluationException(functionToken, "Absolute value must be less than 1");
        }
        return expression.convertDoubleValue(0.5 * Math.log((1 + value) / (1 - value)));
    }
}
