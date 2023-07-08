package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Converts an angle measured in radians to an approximately equivalent angle measured in degrees.
 */
@FunctionParameter(name = "radians")
public class DegFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        double rad = Math.toDegrees(parameterValues[0].getNumberValue().doubleValue());

        return expression.convertDoubleValue(rad);
    }
}
