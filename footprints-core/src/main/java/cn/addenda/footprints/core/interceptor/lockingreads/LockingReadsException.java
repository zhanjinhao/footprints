package cn.addenda.footprints.core.interceptor.lockingreads;

import cn.addenda.footprints.core.FootprintsException;

/**
 * @author addenda
 * @since 2022/10/11 19:19
 */
public class LockingReadsException extends FootprintsException {
    public LockingReadsException() {
    }

    public LockingReadsException(String message) {
        super(message);
    }

    public LockingReadsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockingReadsException(Throwable cause) {
        super(cause);
    }

    public LockingReadsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
