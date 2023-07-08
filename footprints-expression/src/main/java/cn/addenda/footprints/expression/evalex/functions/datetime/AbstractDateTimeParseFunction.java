package cn.addenda.footprints.expression.evalex.functions.datetime;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.time.Instant;
import java.time.ZoneId;

public abstract class AbstractDateTimeParseFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        ZoneId zoneId = expression.getConfiguration().getDefaultZoneId();
        Instant instant;

        if (parameterValues.length < 2) {
            instant = parse(parameterValues[0].getStringValue(), null, zoneId);
        } else {
            instant =
                    parse(parameterValues[0].getStringValue(), parameterValues[1].getStringValue(), zoneId);
        }
        return new EvaluationValue(instant);
    }

    protected abstract Instant parse(String value, String format, ZoneId zoneId);
}
