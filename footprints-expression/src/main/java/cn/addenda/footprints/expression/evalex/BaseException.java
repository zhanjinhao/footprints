package cn.addenda.footprints.expression.evalex;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Base exception class used in EvalEx.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
public class BaseException extends Exception {

    @Getter
    @EqualsAndHashCode.Include
    private final int startPosition;
    @Getter
    @EqualsAndHashCode.Include
    private final int endPosition;
    @Getter
    @EqualsAndHashCode.Include
    private final String tokenString;
    @Getter
    @EqualsAndHashCode.Include
    private final String message;

    public BaseException(int startPosition, int endPosition, String tokenString, String message) {
        super(message);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.tokenString = tokenString;
        this.message = super.getMessage();
    }
}
