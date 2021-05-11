package com.jzue.tomcat.lifecycle;

/**
 * @Author: junzexue
 * @Date: 2019/3/21 下午4:51
 * @Description:
 **/
public final class LifecycleException extends Exception {
    private static final long serialVersionUID = 1L;
    public LifecycleException() {
        super();
    }
    public LifecycleException(Throwable throwable) {
        super(throwable);
    }
    public LifecycleException(String message) {
        super(message);
    }
    public LifecycleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
