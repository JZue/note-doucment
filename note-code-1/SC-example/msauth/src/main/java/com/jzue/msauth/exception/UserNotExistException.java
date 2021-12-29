package com.jzue.msauth.exception;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 上午10:59
 * @Description:
 **/
public class UserNotExistException extends RuntimeException {
    public UserNotExistException() {
    }

    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotExistException(Throwable cause) {
        super(cause);
    }

    public UserNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
