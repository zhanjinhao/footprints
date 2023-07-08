package cn.addenda.footprints.expression.evalex.functions.basic;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.RoundingMode;

/**
 * Rounds the given value an integer using the rounding mode {@link RoundingMode#CEILING}
 */
@FunctionParameter(name = "value")
public class CeilingFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {

        EvaluationValue value = parameterValues[0];

        return new EvaluationValue(value.getNumberValue().setScale(0, RoundingMode.CEILING));
    }
}
