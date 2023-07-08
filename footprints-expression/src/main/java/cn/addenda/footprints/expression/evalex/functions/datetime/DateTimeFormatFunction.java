package cn.addenda.footprints.expression.evalex.functions.datetime;

import cn.addenda.footprints.expression.evalex.Expression;
import cn.addenda.footprints.expression.evalex.data.EvaluationValue;
import cn.addenda.footprints.expression.evalex.functions.AbstractFunction;
import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;
import cn.addenda.footprints.expression.evalex.parser.Token;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@FunctionParameter(name = "value", isVarArg = true)
public class DateTimeFormatFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(
            Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        String formatted;
        ZoneId zoneId = expression.getConfiguration().getDefaultZoneId();
        if (parameterValues.length < 2) {
            formatted = parameterValues[0].getDateTimeValue().atZone(zoneId).toLocalDateTime().toString();
        } else {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(parameterValues[1].getStringValue());
            formatted =
                    parameterValues[0].getDateTimeValue().atZone(zoneId).toLocalDateTime().format(formatter);
        }
        return new EvaluationValue(formatted);
    }
}
