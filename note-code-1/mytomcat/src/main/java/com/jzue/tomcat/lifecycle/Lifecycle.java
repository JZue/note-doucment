package com.jzue.tomcat.lifecycle;

/**
 * @Author junzexue
 * @Description:生命周期
 * @Date: 下午4:54 2019/3/21
 **/
public interface Lifecycle {


    // 添加监听器
    public void addLifecycleListener(LifecycleListener listener);
    // 获取所以监听器
    public LifecycleListener[] findLifecycleListeners();
    // 移除某个监听器
    public void removeLifecycleListener(LifecycleListener listener);
    // 初始化方法
    public void init() throws LifecycleException;
    // 启动方法
    public void start() throws LifecycleException;
    // 停止方法，和start对应
    public void stop() throws LifecycleException;
    // 销毁方法，和init对应
    public void destroy() throws LifecycleException;
    // 获取生命周期状态
    public LifecycleState getState();
    // 获取字符串类型的生命周期状态
    public String getStateName();

    /**
     * 标记当前实例只能被使用一次
     */
    public interface SingleUse {
    }
}
