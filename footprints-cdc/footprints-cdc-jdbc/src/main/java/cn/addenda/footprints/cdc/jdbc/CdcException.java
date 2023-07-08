package cn.addenda.footprints.cdc.jdbc;

import cn.addenda.footprints.core.FootprintsException;

/**
 * @author addenda
 * @since 2022/8/24 19:32
 */
public class CdcException extends FootprintsException {

    public CdcException() {
    }

    public CdcException(String message) {
        super(message);
    }

    public CdcException(String message, Throwable cause) {
        super(message, cause);
    }

    public CdcException(Throwable cause) {
        super(cause);
    }

    public CdcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
