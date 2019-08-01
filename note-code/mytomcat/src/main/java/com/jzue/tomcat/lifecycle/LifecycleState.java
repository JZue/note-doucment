package com.jzue.tomcat.lifecycle;
/**
 * @Description:生命周期状态
 * @Date: 下午4:52 2019/3/21
 * @Author junzexue
 **/
public enum LifecycleState {
    /*
     * Lifecycle实例化完成时的状态
     */
    NEW(false, null),
    /*
     * Lifecycle正在初始化的状态
     */
    INITIALIZING(false, LifecycleConstant.BEFORE_INIT_EVENT),
    /*
     * Lifecycle初始化完成的状态
     */
    INITIALIZED(false, LifecycleConstant.AFTER_INIT_EVENT),
    /*
     * Lifecycle启动前的状态
     */
    STARTING_PREP(false, LifecycleConstant.BEFORE_START_EVENT),
    /*
     * Lifecycle启动中的状态
     */
    STARTING(true, LifecycleConstant.START_EVENT),
    /*
     * Lifecycle启动完成的状态
     */
    STARTED(true, LifecycleConstant.AFTER_START_EVENT),
    /*
     * Lifecycle停止之前的状态
     */
    STOPPING_PREP(true, LifecycleConstant.BEFORE_STOP_EVENT),
    /*
     * Lifecycle停止中的状态
     */
    STOPPING(false, LifecycleConstant.STOP_EVENT),
    /*
     * Lifecycle启动完成的状态
     */
    STOPPED(false, LifecycleConstant.AFTER_STOP_EVENT),
    /*
     * Lifecycle销毁前的状态
     */
    DESTROYING(false, LifecycleConstant.BEFORE_DESTROY_EVENT),
    /*
     * Lifecycle销毁完成的状态
     */
    DESTROYED(false, LifecycleConstant.AFTER_DESTROY_EVENT),
    /*
     * Lifecycle启动、停止过程中出现异常
     */
    FAILED(false, null);

    private final boolean available;
    private final String lifecycleEvent;

    private LifecycleState(boolean available, String lifecycleEvent) {
        this.available = available;
        this.lifecycleEvent = lifecycleEvent;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getLifecycleEvent() {
        return lifecycleEvent;
    }

}
