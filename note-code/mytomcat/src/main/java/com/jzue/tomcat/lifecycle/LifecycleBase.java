package com.jzue.tomcat.lifecycle;

import com.jzue.tomcat.Log.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: junzexue
 * @Date: 2019/3/21 下午5:07
 * @Description: 生命周期的基础实现及其相关的生命周期管理方法
 **/
public abstract class LifecycleBase implements Lifecycle {

    Logger logger=Logger.getInstance(LifecycleBase.class);

    /*
    * 出于线程安全的问题，故而用了cow
    * cow ,就是在修改（增删改）的时候，重新复制一个list,对复制出来的list的做修改操作，然后在用修改后的list去取代原来的list
    * 从以上的增删改查中我们可以发现，增删改都需要获得锁，并且锁只有一把，而读操作不需要获得锁，支持并发。
    * 为什么增删改中都需要创建一个新的数组，操作完成之后再赋给原来的引用？这是为了保证get的时候都能获取到元素，
    * 如果在增删改过程直接修改原来的数组，可能会造成执行读操作获取不到数据
    *
    * CopyOnWriteArrayList适合使用在读操作远远大于写操作的场景里，比如缓存。发生修改时候做copy，新老版本分离，保证读的高性能，适用于以读为主的情况。
    *
    * */
    private final List<LifecycleListener> lifecycleListeners=new CopyOnWriteArrayList<LifecycleListener>();

    /**
     *这里就是组件的生命周期状态
     */
    private volatile LifecycleState state = LifecycleState.NEW;


    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
    }

    public LifecycleListener[] findLifecycleListeners() {
        return lifecycleListeners.toArray(new LifecycleListener[0]);
    }

    public void removeLifecycleListener(LifecycleListener listener) {
         lifecycleListeners.remove(listener);
    }

    /**
     * @Description:new=====>INITIALIZING===>INITIALIZED==>...
     * @Date: 上午8:56 2019/3/22
     **/
    public void init() throws LifecycleException {
        if(!state.equals(LifecycleState.NEW)){
            throw new LifecycleException("只有new状态才可以初始化");
        }
        try {
            //new=====>INITIALIZING===>INITIALIZED==>......这是状态的切换过程
            // new 状态==》 方法将状态变更为INITIALIZING
            setStateInternal(LifecycleState.INITIALIZING,null,false);
            // 初始化，该方法为一个abstract 方法，需要组件自行实现
            initInternal();
            // 初始化完成之后，状态变更为INITIALIZED
            setStateInternal(LifecycleState.INITIALIZED,null,false);
        }catch (Throwable t){
            throw new LifecycleException("初始化异常");
        }

    }
    /**
     * @Description: INITIALIZED --»-- STARTING_PREP --»- STARTING --»- STARTED
     * @Date: 上午8:55 2019/3/22
     **/
    public void start() throws LifecycleException {

        // 如果已经start()  直接返回
        if (LifecycleState.STARTING_PREP.equals(state) || LifecycleState.STARTING.equals(state) ||
                LifecycleState.STARTED.equals(state)){
            logger.warning("已经start()，请勿重复执行状态切换");
            return;
        }

        // 如果未初始化就先初始化，如果处于异常状态，立即stop(),elseif 不出去INITIALIZED&&  不处于STOPPED ,抛出异常
        if (state.equals(LifecycleState.NEW)) {
            init();
        } else if (state.equals(LifecycleState.FAILED)) {
            stop();
        } else if (!state.equals(LifecycleState.INITIALIZED) &&
                !state.equals(LifecycleState.STOPPED)) {
            throw new LifecycleException("");
        }


        try {
            setStateInternal(LifecycleState.STARTING_PREP, null, false);
            // start逻辑，抽象方法，由组件自行实现
            startInternal();
            if (state.equals(LifecycleState.FAILED)) {
                stop();
            } else if (!state.equals(LifecycleState.STARTING)) {
                throw new LifecycleException("STARTING_PREP后状态切换失败");
            } else {
                //已经处于STARTING ，那么切换状态到STARTED
                setStateInternal(LifecycleState.STARTED, null, false);
            }
        } catch (Throwable t) {
            //如果捕获到异常，拿那么先将生命周期状态切换奥FAILED,然后抛出异常
            setStateInternal(LifecycleState.FAILED, null, false);
            throw new LifecycleException("lifecycleBase.startFail");
        }
    }

    public void stop() throws LifecycleException {
        // `STOPPING_PREP`、`STOPPING`和STOPPED时，将忽略stop()的执行
        if (LifecycleState.STOPPING_PREP.equals(state) || LifecycleState.STOPPING.equals(state) ||
                LifecycleState.STOPPED.equals(state)) {
                logger.debug("lifecycleBase.alreadyStopped");
            return;
        }
        // `NEW`状态时，直接将状态变更为`STOPPED`
        if (state.equals(LifecycleState.NEW)) {
            state = LifecycleState.STOPPED;
            return;
        }

        // stop()的执行，必须要是`STARTED`和`FAILED`
        if (!state.equals(LifecycleState.STARTED) && !state.equals(LifecycleState.FAILED)) {
            throw new LifecycleException(getStateName()+"状态不能执行stop();"+"stop()的执行，必须要是`STARTED`和`FAILED`");
        }
        try {
            // `FAILED`时，直接触发BEFORE_STOP_EVENT事件
            if (state.equals(LifecycleState.FAILED)) {
                // Don't transition to STOPPING_PREP as that would briefly mark the
                // component as available but do ensure the BEFORE_STOP_EVENT is
                // fired
                fireLifecycleEvent(LifecycleConstant.BEFORE_STOP_EVENT, null);
            } else {
                // 设置状态为STOPPING_PREP
                setStateInternal(LifecycleState.STOPPING_PREP, null, false);
            }

            // stop逻辑，抽象方法，组件自行实现
            stopInternal();

            // Shouldn't be necessary but acts as a check that sub-classes are
            // doing what they are supposed to.
            if (!state.equals(LifecycleState.STOPPING) && !state.equals(LifecycleState.FAILED)) {
                throw new LifecycleException("STOPPING/FAILED才可以切换到STOPPED");
            }
            // 设置状态为STOPPED
            setStateInternal(LifecycleState.STOPPED, null, false);
        }catch (LifecycleException e){
            setStateInternal(LifecycleState.FAILED, null, false);
            throw new LifecycleException("lifecycleBase.stopFail");
        }  finally {
        if (this instanceof Lifecycle.SingleUse) {
            // Complete stop process first
            setStateInternal(LifecycleState.STOPPED, null, false);
            destroy();
        }
    }


    }

    public void destroy() throws LifecycleException {
        if (LifecycleState.FAILED.equals(state)) {
            try {
                // Triggers clean-up
                stop();
            } catch (LifecycleException e) {
                // Just log. Still want to destroy.
                logger.warning("lifecycleBase.destroyStopFail");
            }
        }
        if (LifecycleState.DESTROYING.equals(state) ||
                LifecycleState.DESTROYED.equals(state)) {
                logger.debug("lifecycleBase.alreadyDestroyed");
            return;
        }
        //非法状态判断，根据状态转换图可知STOPPED，FAILED，NEW，INITIALIZED
        if (!state.equals(LifecycleState.STOPPED) &&
                !state.equals(LifecycleState.FAILED) &&
                !state.equals(LifecycleState.NEW) &&
                !state.equals(LifecycleState.INITIALIZED)) {
            throw new LifecycleException("现阶段生命周期状态不允许destroy");
        }
        try {
            // destroy前状态设置
            setStateInternal(LifecycleState.DESTROYING, null, false);
            // 抽象方法，组件自行实现
            destroyInternal();
            // destroy后状态设置
            setStateInternal(LifecycleState.DESTROYED, null, false);
        } catch (Throwable t) {
            setStateInternal(LifecycleState.FAILED, null, false);
            throw new LifecycleException("lifecycleBase.destroyFail");
        }
    }

    public LifecycleState getState() {
        return this.state;
    }

    public String getStateName() {
        return getState().toString();
    }

    //修改State 状态
    // 三个参数，变更后的状态 ，，check 是否做状态校验
    private synchronized void setStateInternal(LifecycleState state,
                                               Object data, boolean check) throws LifecycleException{

        // 是否校验状态
        if (check) {
            if (state == null) {
                throw new NullPointerException();
            }
            if (!(state == LifecycleState.FAILED ||
                    (this.state == LifecycleState.STARTING_PREP &&
                            state == LifecycleState.STARTING) ||
                    (this.state == LifecycleState.STOPPING_PREP &&
                            state == LifecycleState.STOPPING) ||
                    (this.state == LifecycleState.FAILED &&
                            state == LifecycleState.STOPPING))) {
                // No other transition permitted
                throw new LifecycleException();
            }
        }
        // 设置状态
        this.state = state;
        // 触发事件
        String lifecycleEvent = state.getLifecycleEvent();
        if (lifecycleEvent != null) {
            fireLifecycleEvent(lifecycleEvent, data);
        }
    }
    /**
     * @Description:事件触发机制；子类触发周期事件
     * @Date: 下午6:04 2019/3/21
     **/
    protected void fireLifecycleEvent(String type, Object data) {
        LifecycleEvent event = new LifecycleEvent(this, type, data);
        for (LifecycleListener listener : lifecycleListeners) {
            listener.lifecycleEvent(event);
        }
    }
    // 初始化方法,需要组件自行实现
    protected abstract void initInternal() throws LifecycleException;
    // 启动方法，需要组件自行实现
    protected abstract void startInternal() throws LifecycleException;
    // 停止方法，需要组件自行实现
    protected abstract void stopInternal() throws LifecycleException;
    // 销毁方法，需要组件自行实现
    protected abstract void destroyInternal() throws LifecycleException;
}
