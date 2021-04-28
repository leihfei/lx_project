package com.lnlr.common.exception;

/**
 * 功能说明：警告异常
 *
 * @author wangwt
 * @date 2019-04-04 10:00
 */
public class WarnException extends RuntimeException {
    public WarnException() {
        super();
    }

    public WarnException(String message) {
        super(message);
    }

    public WarnException(String message, Throwable cause) {
        super(message, cause);
    }

    public WarnException(Throwable cause) {
        super(cause);
    }

    protected WarnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
