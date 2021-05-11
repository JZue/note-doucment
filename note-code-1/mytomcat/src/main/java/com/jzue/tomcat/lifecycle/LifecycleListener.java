package com.jzue.tomcat.lifecycle;

public interface LifecycleListener {
    // 事件触发函数
    public void lifecycleEvent(LifecycleEvent event);
}
