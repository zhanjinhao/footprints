package cn.addenda.footprints.expression.evalex.functions.datetime;

import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@FunctionParameter(name = "value", isVarArg = true)
public class ZonedDateTimeParseFunction extends AbstractDateTimeParseFunction {
    protected Instant parse(String value, String format, ZoneId zoneId) {
        return parseZonedDateTime(value, format, zoneId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Unable to parse zoned date/time: " + value));
    }

    private Optional<Instant> parseZonedDateTime(String value, String format, ZoneId zoneId) {
        try {
            DateTimeFormatter formatter =
                    (format == null
                            ? DateTimeFormatter.ISO_ZONED_DATE_TIME
                            : DateTimeFormatter.ofPattern(format))
                            .withZone(zoneId);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, formatter);
            return Optional.of(zonedDateTime.toInstant());
        } catch (DateTimeParseException ex) {
            return Optional.empty();
        }
    }
}
