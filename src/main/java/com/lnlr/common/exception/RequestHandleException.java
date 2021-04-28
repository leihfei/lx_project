package com.lnlr.common.exception;

/**
 * @author leihfei
 * @description 请求异常
 * @date:Create in 23:39 2018/9/2
 * @email:leihfein@gmail.com
 */
public class RequestHandleException extends RuntimeException {
    public RequestHandleException() {
        super();
    }

    public RequestHandleException(String message) {
        super(message);
    }

    public RequestHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestHandleException(Throwable cause) {
        super(cause);
    }

    protected RequestHandleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
