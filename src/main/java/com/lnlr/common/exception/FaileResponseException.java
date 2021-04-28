package com.lnlr.common.exception;

/**
 * @author:leihfei
 * @description 失败或者返回500消息
 * @date:Create in 15:40 2018/9/6
 * @email:leihfein@gmail.com
 */
public class FaileResponseException extends RuntimeException {
    public FaileResponseException() {
        super();
    }

    public FaileResponseException(String message) {
        super(message);
    }

    public FaileResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaileResponseException(Throwable cause) {
        super(cause);
    }

    protected FaileResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
