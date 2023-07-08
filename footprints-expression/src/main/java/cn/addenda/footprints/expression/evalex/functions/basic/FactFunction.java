package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;

/**
 * Factorial function, calculates the factorial of a base value.
 */
@FunctionParameter(name = "base")
public class FactFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        int number = parameterValues[0].getNumberValue().intValue();
        BigDecimal factorial = BigDecimal.ONE;
        for (int i = 1; i <= number; i++) {
            factorial =
                    factorial.multiply(
                            new BigDecimal(i, expression.getConfiguration().getMathContext()),
                            expression.getConfiguration().getMathContext());
        }
        return new EvaluationValue(factorial);
    }
}
