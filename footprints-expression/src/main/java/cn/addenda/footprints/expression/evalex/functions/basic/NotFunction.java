package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Boolean negation function.
 */
@FunctionParameter(name = "value")
public class NotFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        boolean result = parameterValues[0].getBooleanValue();

        return new EvaluationValue(!result);
    }
}
