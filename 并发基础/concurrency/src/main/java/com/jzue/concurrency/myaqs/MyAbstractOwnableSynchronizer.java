package com.jzue.concurrency.mycoucurrency;

/**
 * @Author: junzexue
 * @Date: 2019/4/16 下午2:05
 * @Description:对于当前是哪个线程获取锁的维护
 **/
public abstract class MyAbstractOwnableSynchronizer implements java.io.Serializable {
    private static final long serialVersionUID = 3737899427754241961L;

    protected MyAbstractOwnableSynchronizer() { }

    /**
     * 当前执获取到锁的线程
     **/
    private transient Thread exclusiveOwnerThread;

    /**
     * 不可override
     **/
    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread(){
        return this.exclusiveOwnerThread;
    }
}
