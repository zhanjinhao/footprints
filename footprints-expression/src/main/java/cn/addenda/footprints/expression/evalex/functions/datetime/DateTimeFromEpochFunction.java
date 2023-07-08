package cn.addenda.footprints.expression.evalex.functions.datetime;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.math.BigDecimal;
import java.time.Instant;

@FunctionParameter(name = "value")
public class DateTimeFromEpochFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal millis = parameterValues[0].getNumberValue();
        return new EvaluationValue(Instant.ofEpochMilli(millis.longValue()));
    }
}
