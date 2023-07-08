package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;

/**
 * Returns the maximum value of all parameters.
 */
@FunctionParameter(name = "value", isVarArg = true)
public class MaxFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal max = null;
        for (EvaluationValue parameter : parameterValues) {
            if (max == null || parameter.getNumberValue().compareTo(max) > 0) {
                max = parameter.getNumberValue();
            }
        }
        return new EvaluationValue(max);
    }
}
