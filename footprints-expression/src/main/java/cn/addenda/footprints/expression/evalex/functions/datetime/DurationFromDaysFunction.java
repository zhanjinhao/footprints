package cn.addenda.footprints.expression.evalex.functions.datetime;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;
import java.time.Duration;

@FunctionParameter(name = "value")
public class DurationFromDaysFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal days = parameterValues[0].getNumberValue();
        return new EvaluationValue(Duration.ofDays(days.longValue()));
    }
}
