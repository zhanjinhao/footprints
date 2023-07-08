package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the trigonometric co-tangent of an angle (in radians).
 */
@FunctionParameter(name = "value", nonZero = true)
public class CotRFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: cot(x) = cos(x) / sin(x) = 1 / tan(x) */
        return expression.convertDoubleValue(
                1 / Math.tan(parameterValues[0].getNumberValue().doubleValue()));
    }
}
