package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the arc-co-tangent (in radians).
 */
@FunctionParameter(name = "value", nonZero = true)
public class AcotRFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: acot(x) = (pi / 2) - atan(x) */
        return expression.convertDoubleValue(
                (Math.PI / 2) - Math.atan(parameterValues[0].getNumberValue().doubleValue()));
    }
}
