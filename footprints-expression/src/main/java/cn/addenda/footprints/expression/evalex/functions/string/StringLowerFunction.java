package cn.addenda.footprints.expression.evalex.functions.string;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Converts the given value to lower case.
 */
@FunctionParameter(name = "value")
public class StringLowerFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        return new EvaluationValue(parameterValues[0].getStringValue().toLowerCase());
    }
}
