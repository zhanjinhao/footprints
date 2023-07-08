package cn.addenda.footprints.expression.evalex.parser;

import cn.addenda.footprints.expression.evalex.BaseException;
import lombok.ToString;

/**
 * Exception while parsing the expression.
 */
@ToString(callSuper = true)
public class ParseException extends BaseException {

    public ParseException(int startPosition, int endPosition, String tokenString, String message) {
        super(startPosition, endPosition, tokenString, message);
    }

    public ParseException(String expression, String message) {
        super(1, expression.length(), expression, message);
    }

    public ParseException(Token token, String message) {
        super(
                token.getStartPosition(),
                token.getStartPosition() + token.getValue().length() - 1,
                token.getValue(),
                message);
    }
}
