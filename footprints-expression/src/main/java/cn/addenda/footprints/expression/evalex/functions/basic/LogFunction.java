package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * The natural logarithm (base e) of a value
 */
@FunctionParameter(name = "value", nonZero = true, nonNegative = true)
public class LogFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        double d = parameterValues[0].getNumberValue().doubleValue();

        return expression.convertDoubleValue(Math.log(d));
    }
}
