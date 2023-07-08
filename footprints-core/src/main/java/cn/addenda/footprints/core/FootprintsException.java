package cn.addenda.footprints.core;

/**
 * @author addenda
 * @since 2023/6/7 20:31
 */
public class FootprintsException extends RuntimeException {
    public FootprintsException() {
    }

    public FootprintsException(String message) {
        super(message);
    }

    public FootprintsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FootprintsException(Throwable cause) {
        super(cause);
    }

    public FootprintsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
