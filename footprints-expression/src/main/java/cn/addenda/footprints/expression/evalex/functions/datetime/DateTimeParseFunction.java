package cn.addenda.footprints.expression.evalex.functions.datetime;

import cn.addenda.footprints.expression.evalex.functions.FunctionParameter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@FunctionParameter(name = "value", isVarArg = true)
public class DateTimeParseFunction extends AbstractDateTimeParseFunction {

    protected Instant parse(String value, String format, ZoneId zoneId) {

        Instant parseInstant = parseInstant(value);
        if (parseInstant != null) {
            return parseInstant;
        }
        Instant parseLocalDateTime = parseLocalDateTime(value, format, zoneId);
        if (parseLocalDateTime != null) {
            return parseLocalDateTime;
        }

        Instant parseDate = parseDate(value, format);
        if (parseDate != null) {
            return parseDate;
        }

        throw new IllegalArgumentException("Unable to parse date/time: " + value);
    }

    private Instant parseLocalDateTime(String value, String format, ZoneId zoneId) {
        try {
            DateTimeFormatter formatter =
                    (format == null
                            ? DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            : DateTimeFormatter.ofPattern(format));
            return LocalDateTime.parse(value, formatter).atZone(zoneId).toInstant();
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private Instant parseDate(String value, String format) {
        try {
            DateTimeFormatter formatter =
                    (format == null ? DateTimeFormatter.ISO_LOCAL_DATE : DateTimeFormatter.ofPattern(format));
            LocalDate localDate = LocalDate.parse(value, formatter);
            return localDate.atStartOfDay().atOffset(ZoneOffset.UTC).toInstant();
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private Instant parseInstant(String value) {
        try {
            return Instant.parse(value);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }


}
