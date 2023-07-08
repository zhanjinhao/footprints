package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;

/**
 * Returns the sum value of all parameters.
 */
@FunctionParameter(name = "value", isVarArg = true)
public class SumFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal sum = BigDecimal.ZERO;
        for (EvaluationValue parameter : parameterValues) {
            sum = sum.add(parameter.getNumberValue(), expression.getConfiguration().getMathContext());
        }
        return new EvaluationValue(sum);
    }
}
