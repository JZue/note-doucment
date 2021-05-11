package com.jzue.msauth.exception;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午11:40
 * @Description:
 **/
public class UserCheckException extends RuntimeException {
    public UserCheckException() {
    }

    public UserCheckException(String message) {
        super(message);
    }

    public UserCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCheckException(Throwable cause) {
        super(cause);
    }

    public UserCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
