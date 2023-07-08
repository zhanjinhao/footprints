package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;

/**
 * Returns the minimum value of all parameters.
 */
@FunctionParameter(name = "value", isVarArg = true)
public class MinFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal min = null;
        for (EvaluationValue parameter : parameterValues) {
            if (min == null || parameter.getNumberValue().compareTo(min) < 0) {
                min = parameter.getNumberValue();
            }
        }
        return new EvaluationValue(min);
    }
}
