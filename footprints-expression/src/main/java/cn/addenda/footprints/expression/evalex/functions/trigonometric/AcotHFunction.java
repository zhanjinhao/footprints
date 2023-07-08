package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the arc hyperbolic cotangent.
 */
@FunctionParameter(name = "value")
public class AcotHFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: acoth(x) = log((x + 1) / (x - 1)) * 0.5 */
        double value = parameterValues[0].getNumberValue().doubleValue();
        return expression.convertDoubleValue(Math.log((value + 1) / (value - 1)) * 0.5);
    }
}
