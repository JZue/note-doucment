package com.jzue.tomcat.connector.endpoint;

import java.util.concurrent.Executor;

/**
 * @Author: junzexue
 * @Date: 2019/3/21 下午4:28
 * @Description:Connector.initInternal()-->protocolHandler.init()->AbstractProtocol.init()--->
 * endpoint.init()--->AbstractEndpoint.bind()-->IO通信的3种实现
 *
 * 该类主要用于启动监听连接请求、提供底层的网络I/O的处理
 *
 * 下面我提供了EndPoint的解释
 *      EndPoint提供基础的网络IO服务，用来实现网络连接和控制，它是服务器对外I/O操作的接入点。
 *      主要任务是管理对外的socket连接，同时将建立好的socket连接交到合适的工作线程中去。
 **/
public abstract class AbstractEndpoint<S> {

    //internalExecutor 用来AbstractEndpoint的实现类判断是否用的是内部线程池
    protected volatile boolean internalExecutor = true;
    private Executor executor = null;
    public void setExecutor(Executor executor) {
        this.executor = executor;
        this.internalExecutor = (executor == null);
    }
    public Executor getExecutor() { return this.executor; }
    /**
     * @Description:在当调用者没有显式指定所用线程池时，会创建一个自己所用的线程池
     **/
    public void createExecutor () {}



    protected Acceptor [] acceptors;
    /**
     * @Description:用来启动底层网络I/O接收器的,这个在子类的startInternal中会被调用
     *
     * 启动acceptors时，并没有使用前面提到过的线程池，而是生成了新的守护线程(getDaemon方法，默认返回true)，来运行。
     * 但，具体在acceptors中线程的执行体，则交由具体的子类负责实现（貌似template-method模式是各种框架的基础配置），
     * 通过重写抽象方法createAcceptor来完成。

     **/
    protected final void startAcceptorThreads() {
    }
    /**
     * @Description:提供给子类自己创建Acceptor的入口
     **/
    protected abstract Acceptor createAcceptor();





    /**
     * @Description: 用来接收底层的Socket连接的抽象类，其线程的run()方法的实现由对应的实现类实现，他只提供基础的状态描述AcceptorState
     * <p>
     * <p>
     * 此处将会阐述一下静态内部类的意义：
     * 1)-在非静态内部类中不可以声明静态成员
     * 2)-一般的非静态内部类，可以随意的访问外部类中的成员变量与成员方法。
     * 即使这些成员方法被修饰为private(私有的成员变量或者方法)，其非静态内部类都可以随意的访问。
     * 3)-不能够从静态内部类的对象中访问外部类的非静态成员(与2对应)
     * 4）-通常情况下，在一个类中创建成员内部类的时候，有一个强制性的规定，即内部类的实例一定要绑定在外部类的实例中
     * 5）-通常情况下，程序员在定义静态内部类的时候，是不需要定义绑定在外部类的实例上的。
     * 6）-静态内部类的加载不需要依附外部类，在使用时才加载。加载静态内部类的时候不会会加载外部类的假象
     * 7）-外部类初次加载，会初始化静态变量、静态代码块、静态方法，但不会加载内部类和静态内部类。
     * 8）-如果内部类不会引用到外部类东西的话，强烈建议使用静态内部类，因为这样更节省资源，减少内部类其中的一个指向外部类的引用
     * <p>
     * 最重要的一点，一般源码中使用内部类最大的一部分原因，是其不能访问外部内的信息，保证内部聚合，确保访问最高的严格性
     **/
    public abstract static class Acceptor implements Runnable {
        public enum AcceptorState {
            NEW, RUNNING, PAUSED, ENDED
        }
        protected volatile AcceptorState state = AcceptorState.NEW;
        public final AcceptorState getState() {
            return state;
        }
        private String threadName;
        protected final void setThreadName(final String threadName) {
            this.threadName = threadName;
        }
        protected final String getThreadName() {
            return threadName;
        }
    }

    /**
     * @Description:它的作用是定义连接的处理.也就是后面被实现成各个版本是ConnectionHandler.
     **/
    public static interface Handler<S> {

    }



    public void init() throws Exception {

    }

    public final void start() throws Exception {
    }
    /**
     * @Description:pause方法是用unlockAccept来解除Acceptor的绑定
     **/
    public void pause() {
    }



    /**
     * @Description:stop和destroy作用是解除它的Socket绑定,也就是不接受Soccket.
     **/
    public final void stop() throws Exception{}
    public final void destroy() throws Exception {}




    /**
     * @Description:下面的4个抽象方法，来实现不同协议的处理方式 抽象方法的意义：当一个方法为抽象方法时，意味着这个方法应该被子类的方法所重写，否则其子类的该方法仍然是abstract的
     **/
    public abstract void bind() throws Exception;

    public abstract void unbind() throws Exception;

    /**
     * @Description:实现类需要提供如下实现
     * 在startInternal中，初始化线程池，创建和启动网络数据接收线程组，创建和启动poller线程组。
     * 在startInternal中，会检查是否指定要使用外部的线程池，如果没有指定外部线程池，Endpoint就会创建一个内部的线程池。但这里面没有与线程的调度和使用相关的代码
     *
     * */
    public abstract void startInternal() throws Exception;

    public abstract void stopInternal() throws Exception;

}
