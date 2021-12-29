package com.jzue.tomcat.lifecycle;

/**
 * @Author: junzexue
 * @Date: 2019/3/21 下午4:56
 * @Description:
 **/
public interface LifecycleConstant {
    /**
     * @Description:生命周期状态值常理定义
     * @Date: 下午4:57 2019/3/21
     **/
    //component初始化之前
    public static final String BEFORE_INIT_EVENT = "before_init";
    // component初始化之后
    public static final String AFTER_INIT_EVENT = "after_init";
    // component启动
    public static final String START_EVENT = "start";
    //component启动之前
    public static final String BEFORE_START_EVENT = "before_start";
    //component启动后
    public static final String AFTER_START_EVENT = "after_start";
    //component停止
    public static final String STOP_EVENT = "stop";
    //component停止前
    public static final String BEFORE_STOP_EVENT = "before_stop";
    //component停止后
    public static final String AFTER_STOP_EVENT = "after_stop";
    //component销毁后
    public static final String AFTER_DESTROY_EVENT = "after_destroy";
    //component销毁前
    public static final String BEFORE_DESTROY_EVENT = "before_destroy";
    //周期事件 The LifecycleEvent type for the "periodic" event.
    public static final String PERIODIC_EVENT = "periodic";
    /**
     * The LifecycleEvent type for the "configure_start" event. Used by those
     * components that use a separate component to perform configuration and
     * need to signal when configuration should be performed - usually after
     * {@link #BEFORE_START_EVENT} and before {@link #START_EVENT}.
     */
    public static final String CONFIGURE_START_EVENT = "configure_start";
    /**
     * The LifecycleEvent type for the "configure_stop" event. Used by those
     * components that use a separate component to perform configuration and
     * need to signal when de-configuration should be performed - usually after
     * {@link #STOP_EVENT} and before {@link #AFTER_STOP_EVENT}.
     */
    public static final String CONFIGURE_STOP_EVENT = "configure_stop";




}
