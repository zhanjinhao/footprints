package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the hyperbolic arc-sine.
 */
@FunctionParameter(name = "value")
public class AsinHFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        /* Formula: asinh(x) = ln(x + sqrt(x^2 + 1)) */
        double value = parameterValues[0].getNumberValue().doubleValue();
        return expression.convertDoubleValue(Math.log(value + (Math.sqrt(Math.pow(value, 2) + 1))));
    }
}
