package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns the hyperbolic arc-cosine.
 */
@FunctionParameter(name = "value")
public class AcosHFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues)
            throws EvaluationException {

        /* Formula: acosh(x) = ln(x + sqrt(x^2 - 1)) */
        double value = parameterValues[0].getNumberValue().doubleValue();
        if (Double.compare(value, 1) < 0) {
            throw new EvaluationException(functionToken, "Value must be greater or equal to one");
        }
        return expression.convertDoubleValue(Math.log(value + (Math.sqrt(Math.pow(value, 2) - 1))));
    }
}
