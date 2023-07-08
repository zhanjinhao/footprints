package cn.addenda.footprints.expression.evalex.functions.trigonometric;

import cn.addenda.footprints.expression.evalex.EvaluationException;
import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;

/**
 * Returns the arc-sine (in radians).
 */
@FunctionParameter(name = "value")
public class AsinRFunction extends AbstractFunction {

    private static final BigDecimal MINUS_ONE = BigDecimal.valueOf(-1);

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues)
            throws EvaluationException {

        BigDecimal parameterValue = parameterValues[0].getNumberValue();

        if (parameterValue.compareTo(BigDecimal.ONE) > 0) {
            throw new EvaluationException(
                    functionToken, "Illegal asinr(x) for x > 1: x = " + parameterValue);
        }
        if (parameterValue.compareTo(MINUS_ONE) < 0) {
            throw new EvaluationException(
                    functionToken, "Illegal asinr(x) for x < -1: x = " + parameterValue);
        }
        return expression.convertDoubleValue(
                Math.asin(parameterValues[0].getNumberValue().doubleValue()));
    }
}
