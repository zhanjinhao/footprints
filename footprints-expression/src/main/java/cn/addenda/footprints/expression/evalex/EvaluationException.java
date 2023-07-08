package cn.addenda.footprints.expression.evalex;

import cn.addenda.footprints.expression.evalex.parser.Token;

/**
 * Exception while evaluating the parsed expression.
 */
public class EvaluationException extends BaseException {

    public EvaluationException(Token token, String message) {
        super(
                token.getStartPosition(),
                token.getStartPosition() + token.getValue().length(),
                token.getValue(),
                message);
    }

    public static EvaluationException ofUnsupportedDataTypeInOperation(Token token) {
        return new EvaluationException(token, "Unsupported data types in operation");
    }
}
