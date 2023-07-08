package cn.addenda.footprints.expression.evalex.functions.string;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Returns true, if the string contains the substring (case-insensitive).
 */
@FunctionParameter(name = "string")
@FunctionParameter(name = "substring")
public class StringContains extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        String string = parameterValues[0].getStringValue();
        String substring = parameterValues[1].getStringValue();
        return new EvaluationValue(string.toUpperCase().contains(substring.toUpperCase()));
    }
}
