package com.lnlr.common.exception;

/**
 * @author:leihfei
 * @description 其他资源异常
 * @date:Create in 23:40 2018/9/2
 * @email:leihfein@gmail.com
 */
public class ResourcesException extends RuntimeException {
    public ResourcesException() {
        super();
    }

    public ResourcesException(String message) {
        super(message);
    }

    public ResourcesException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourcesException(Throwable cause) {
        super(cause);
    }

    protected ResourcesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
