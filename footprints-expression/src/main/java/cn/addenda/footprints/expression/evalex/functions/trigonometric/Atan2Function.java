package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the angle of atan2 (in degrees).
 */
@FunctionParameter(name = "y")
@FunctionParameter(name = "x")
public class Atan2Function extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        return expression.convertDoubleValue(
                Math.toDegrees(
                        Math.atan2(
                                parameterValues[0].getNumberValue().doubleValue(),
                                parameterValues[1].getNumberValue().doubleValue())));
    }
}
