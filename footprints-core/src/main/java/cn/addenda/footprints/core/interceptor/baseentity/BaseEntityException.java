package cn.addenda.footprints.core.interceptor.baseentity;

import cn.addenda.footprints.core.FootprintsException;

/**
 * @author addenda
 * @since 2023/5/2 19:37
 */
public class BaseEntityException extends FootprintsException {
    public BaseEntityException() {
    }

    public BaseEntityException(String message) {
        super(message);
    }

    public BaseEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseEntityException(Throwable cause) {
        super(cause);
    }

    public BaseEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
