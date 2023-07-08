package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the hyperbolic sine of a value.
 */
@FunctionParameter(name = "value")
public class SinHFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        return expression.convertDoubleValue(
                Math.sinh(parameterValues[0].getNumberValue().doubleValue()));
    }
}
