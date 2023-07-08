package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the trigonometric sine of an angle (in radians).
 */
@FunctionParameter(name = "value")
public class SinRFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        return expression.convertDoubleValue(
                Math.sin(parameterValues[0].getNumberValue().doubleValue()));
    }
}
