Java version "1.8.0_192"**

hotspot源码：[http://hg.openjdk.java.net](http://hg.openjdk.java.net/)

具体下载步骤可以参考下:<https://blog.csdn.net/u012534326/article/details/85457119>

具体源码的位置~各个jdk甚至各个版本~都会有不同，文中以hotspot ——jdk8u60为例

关于部分native方法，最好试着理解一下jvm中源码的实现~这样才能真正的理解；

实际上，我感觉就算你不会c++~但是理解里面的大致逻辑应该也没啥问题。当然，好好学学c++也是没啥问题的.如果大学计算机专业出身的话，基本都学过c、c++



如果有觉得注释不够明白的地方，第一反应，去看看jdk源码上的注释，然后再去查资料，jdk源码上的注释大部分时候还是特别特别详细的，少部分地方也写的很让人迷糊





```java
public final void stop();
public final synchronized void stop(Throwable obj);
public void destroy();
public final void suspend();
public final void resume();

@Deprecated原因：https://docs.oracle.com/javase/8/docs/technotes/guides/concurrency/threadPrimitiveDeprecation.html
```



```java


// 标注了大部分核心的方法的解释~深入理解thread类~应该是说多线程的基础，很多人虽然做过很多多线程的开发，然后却很少关注thread的类
package java.lang;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.AccessControlContext;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.LockSupport;
import sun.nio.ch.Interruptible;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.security.util.SecurityConstants;

/**
 * 线程就是程序中一个线程的执行.JVM允许一个应用中多个线程并发执行.
 *
 * 每个线程都有优先级.高优先级线程优先于低优先级线程执行.
 * 每个线程都可以被标记为守护线程或非守护线程.
 * 当线程中的run()方法代码里面又创建了一个新的线程对象时,新创建的线程优先级和父线程优先级一样.
 * 当且仅当父线程为守护线程时,新创建的线程才会是守护线程.
 *
 * 当JVM启动时,通常会有唯一的一个非守护线程(这一线程用于调用指定类的main()方法)
 * JVM会持续执行线程直到下面情况某一个发生为止:
 * 1.类运行时exit()方法被调用 且 安全机制允许此exit()方法的调用.
 * 2.所有非守护类型的线程均已经终止,or run()方法调用返回  or 在run()方法外部抛出了一些可传播性的异常.
 *
 *
 * 有2种方式可以创建一个可执行线程.
 * 1.定义一个继承Thread类的子类.子类可覆写父类的run()方法.子类实例分配内存后可运行(非立即,取决于CPU调用)
 * 比如:计算大于指定值的素数的线程可以写成如下
 * class PrimeThread extends Thread {
 *         long minPrime;
 *         PrimeThread(long minPrime) {
 *             this.minPrime = minPrime;
 *         }
 *
 *         public void run() {
 *             // compute primes larger than minPrime
 *         }
 * }
 * 下面的代码将创建一个线程并启动它.
 * PrimeThread p = new PrimeThread(143);
 *     p.start();
 *
 * 2.另一个实现线程的方式就是使类实现Runnable接口.
 * 此类自己会实现run()方法.然后此线程会被分配内存,当线程被创建时,会传入一个参数,然后开始执行.
 * 此种方式的样例代码如下:
 *
 * class PrimeRun implements Runnable {
 *         long minPrime;
 *         PrimeRun(long minPrime) {
 *             this.minPrime = minPrime;
 *         }
 *
 *         public void run() {
 *             // compute primes larger than minPrime
 *             &nbsp;.&nbsp;.&nbsp;.
 *         }
 * }
 * 下面的代码能够创建一个线程并开始执行:
 *  PrimeRun p = new PrimeRun(143);
 *     new Thread(p).start();
 *
 * 每一个线程都有一个用于目的标识的名字.多个线程可以有相同的名字.
 * 线程被创建时如果名字没有被指定,则系统为其自动生成一个新的名字.
 *
 * 除非特别说明,否则在创建线程时传入一个null参数到构造器或者方法会抛出空指针异常NullPointerException
 *
 */
public class Thread implements Runnable {
    //确保本地注册(类构造器方法<clinit>方法用于类初始化)是创建一个线程首要做的事情.
    private static native void registerNatives();
    static {
        registerNatives();
    }
    
    // 线程名，可更改且线程可见
    private volatile String name;
    // 优先级
    private int            priority;
    -// 
    private Thread         threadQ;
    -// JVM 中的Java Thread 指针
    private long           eetop;
    // 是否单步执行
    private boolean     single_step;
    // 是否为守护进程，默认为false
    private boolean     daemon = false;
    // JVM 的状态
    private boolean     stillborn = false;

    /* What will be run. */
    // 将会被执行的Runnable
    private Runnable target;

    // 此线程所在线程组
    private ThreadGroup group;

    // 此线程的类加载器
    private ClassLoader contextClassLoader;

    //此线程继承的访问控制上下文
    private AccessControlContext inheritedAccessControlContext;

    /* For autonumbering anonymous threads. */
    //默认线程计数变量，为匿名线程自动编号，用在nextThreadNum方法中为匿名线程生成名称，本质就是一个放在内存中的计数器，通过synchronized控制并发
    private static int threadInitNumber;
    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }

    // 当前线程附属的ThreadLocal，而ThreadLocalMap会被ThreadLocal维护
    //ThreadLocalMap是ThreadLocal的私有类，是一个用于维护线程本地变量的hashmap,此hashmap的key引用类型为弱引用,这是为了支持大且长期存活的使用方法.
    ThreadLocal.ThreadLocalMap threadLocals = null;

   
    // 主要作用：为子线程提供从父线程那里继承的值
  	//在创建子线程时，子线程会接收所有可继承的线程局部变量的初始值，以获得父线程所具有的值
	// 创建一个线程时如果保存了所有 InheritableThreadLocal 对象的值，那么这些值也将自动传递给子线程
  	//如果一个子线程调用 InheritableThreadLocal 的 get() ，那么它将与它的父线程看到同一个对象
    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;

    
    // 此线程请求栈的深度,如果线程创建者未指定栈深度则其值为0.
    // 此数字如何被使用完全取决于虚拟机自己;也有一些虚拟机会忽略此变量值.
    // JVM参数-Xss
    private long stackSize;

    /*
     * JVM-private state that persists after native thread termination.
     */
    // //此变量表示:在本地线程终止后,JVM私有的一个状态值
    private long nativeParkEventPointer;

    // 每个线程都有专属ID，但名字可能重复
    private long tid;

    // 计数变量，用在nextThreadID方法中为线程生成ID
    // 与threadInitNumber区别~threadInitNumber是用来为没有命名的thread的线程命名的，threadSeqNumber是用来分配线程的ID用的。
    // 参考 https://blog.csdn.net/u012627861/article/details/82904600
    private static long threadSeqNumber;


    //标识线程状态，默认0是线程未启动
    private volatile int threadStatus = 0;


    private static synchronized long nextThreadID() {
        return ++threadSeqNumber;
    }

 
    // 此变量为用于调用java.util.concurrent.locks.LockSupport.park方法的参数.
    // 其值由方法(private) java.util.concurrent.locks.LockSupport.setBlocker进行设定.
    // 其值访问由方法java.util.concurrent.locks.LockSupport.getBlocker进行获取.
    volatile Object parkBlocker;


    //中断阻塞器：当线程发生IO中断时，需要在线程被设置为中断状态后调用该对象的interrupt方法
    // 在Thread.interrupt() 中有就使用到
    private volatile Interruptible blocker;
    //设置上述blocker变量时用的锁
    private final Object blockerLock = new Object();

     //设定block变量的值;通过java.nio代码中的 sun.misc.SharedSecrets进行调用.
    void blockedOn(Interruptible b) {
        synchronized (blockerLock) {
            blocker = b;
        }
    }

    // 每一个线程都会分配一个线程优先级是NORM_PRIORITY （5），切记，线程优先级是优先分配处理器的资源，但是优先级并不能保证线程的执行顺序

    // 最小优先级
    public final static int MIN_PRIORITY = 1;

    // 默认优先级
    public final static int NORM_PRIORITY = 5;

    // 最大优先级
    public final static int MAX_PRIORITY = 10;

    /**
     * Returns a reference to the currently executing thread object.
     *
     * @return  the currently executing thread.
     */
    // 返回当前正在执行的线程的引用
    public static native Thread currentThread();
    
    //Java线程中的Thread.yield( )方法，译为线程让步。顾名思义，就是说当一个线程使用了这个方法之后，它就会把自己CPU执行的时间让掉，让自己或者其它的线程运行，注意是让自己或者其他线程运行，并不是单纯的让给其他线程。
    //yield()的作用是让步。它能让当前线程由“运行状态”进入到“就绪状态”，从而让其它具有相同优先级的等待线程获取执行权；但是，并不能保证在当前线程调用yield()之后，其它具有相同优先级的线程就一定能获得执行权；也有可能是当前线程又进入到“运行状态”继续运行！	
    /**
     * 提示线程调度器当前线程愿意放弃当前CPU的使用。当然调度器可以忽略这个提示。
     *
     * 让出CPU是一种启发式的尝试，以改善线程之间的相对进展，否则将过度利用CPU。
     * 它的使用应该与详细的分析和基准测试相结合以确保它实际上具有预期的效果。
     *
     * 此方法很少有适用的场景.它可以用用于debug或者test,通过跟踪条件可以重现bug.
     * 当设计并发控制结构(如java.util.concurrent.locks包中的并发结构)时,它可能比较有用.
     */
    public static native void yield();


    //当前正在执行的线程休眠 millis 秒，不会释放任何锁的所有权(ownership)
    public static native void sleep(long millis) throws InterruptedException;

    // 第一个参数毫秒，第二个参数纳秒，逻辑比较简单
    public static void sleep(long millis, int nanos)
    throws InterruptedException {
        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos >= 500000 || (nanos != 0 && millis == 0)) {
            millis++;
        }

        sleep(millis);
    }


    //利用当前访问控制上下文(AccessControlContext)来初始化一个线程.
    // @see #init(ThreadGroup,Runnable,String,long,AccessControlContext,boolean)
    private void init(ThreadGroup g, Runnable target, String name,
                      long stackSize) {
        init(g, target, name, stackSize, null, true);
    }

    
    // @param acc 用于继承的访问控制上下文
    // @param inheritThreadLocals 如果值为true,从构造线程继承可继承线程thread-locals的初始值
    //利用当前访问控制上下文(AccessControlContext)来初始化一个线程的实际逻辑所在，很重	     要！！！！！！.
    private void init(ThreadGroup g, Runnable target, String name,
                      long stackSize, AccessControlContext acc,
                      boolean inheritThreadLocals) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        this.name = name;

        Thread parent = currentThread();
        SecurityManager security = System.getSecurityManager();
        if (g == null) {
            //检测其是否为一个应用
            //如果有安全管理,查询安全管理需要做的工作
            if (security != null) {
                g = security.getThreadGroup();
            }

            //如果安全管理在线程所属父线程组的问题上没有什么强制的要求
            if (g == null) {
                g = parent.getThreadGroup();
            }
        }

        //无论所属线程组是否显示传入,都要进行检查访问.
        g.checkAccess();

       //检查是否有required权限
        if (security != null) {
            if (isCCLOverridden(getClass())) {
                security.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
            }
        }

        g.addUnstarted();

        this.group = g;
        //如果父线程为守护线程,则此线程也被 设置为守护线程.
        this.daemon = parent.isDaemon();
        this.priority = parent.getPriority();
        if (security == null || isCCLOverridden(parent.getClass()))
            this.contextClassLoader = parent.getContextClassLoader();
        else
            this.contextClassLoader = parent.contextClassLoader;
        this.inheritedAccessControlContext =
                acc != null ? acc : AccessController.getContext();
        this.target = target;
        setPriority(priority);
        if (inheritThreadLocals && parent.inheritableThreadLocals != null)
            this.inheritableThreadLocals =
                ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);

        this.stackSize = stackSize;

        tid = nextThreadID();
    }


    //线程不支持前拷贝.取而代之的是构造一个新的线程.
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

   
    //分配一个新的线程对象.此构造器和Thread(ThreadGroup,Runnable,String) 构造器的效果一样.
    public Thread() {
        init(null, null, "Thread-" + nextThreadNum(), 0);
    }

  
    public Thread(Runnable target) {
        init(null, target, "Thread-" + nextThreadNum(), 0);
    }
    
    //此构造器生成的线程继承控制访问上下文.
    //此构造器为非公有方法.
    Thread(Runnable target, AccessControlContext acc) {
        init(null, target, "Thread-" + nextThreadNum(), 0, acc, false);
    }

  
    public Thread(ThreadGroup group, Runnable target) {
        init(group, target, "Thread-" + nextThreadNum(), 0);
    }

 
    public Thread(String name) {
        init(null, null, name, 0);
    }

 
    public Thread(ThreadGroup group, String name) {
        init(group, null, name, 0);
    }

    public Thread(Runnable target, String name) {
        init(null, target, name, 0);
    }

    public Thread(ThreadGroup group, Runnable target, String name) {
        init(group, target, name, 0);
    }

    public Thread(ThreadGroup group, Runnable target, String name,
                  long stackSize) {
        init(group, target, name, stackSize);
    }

   /**
     * 此方法的调用会引起当前线程的执行;线程会从NEW->RUNNABLE但是不一定就可以获取到时间片.所以说只能说	   * 到就绪态，不一定是running，获取到时间片以后，JVM会调用此线程的run()方法，此时才真正的在执行run中	   * 的逻辑.
     * 结果就是两个线程可以并发执行:当前线程(从调用的start方法返回)和另一个线程(它在执行run方法).
     * 多次启动同一个线程是不合法的。
     * 尤其注意:一旦一个线程执行完成后就不会再被重新执行.
     */
    public synchronized void start() {
        /**
         * threadStatus != 0的时候
         * 此方法并不会被主要方法线程or由虚拟机创建的系统组线程所调用.
         * 任何向此方法添加的新功能方法在未来都会被添加到虚拟机中.
         * 0状态值代表了NEW的状态.
         */
        if (threadStatus != 0)
            throw new IllegalThreadStateException();

    
        // 通知组此线程即将启动,以便将其添加到组的线程列表中,并且组中未开始的数量会减少
        group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                /* do nothing. If start0 threw a Throwable then
                  it will be passed up the call stack */
            }
        }
    }

    private native void start0();

    //如果此线程有runable对象,则执行,否则什么也不执行.
    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }

  
    // 注意，这个方法是由系统调用的，作用并不是退出当前线程，而是在最终跳出之前清理thread中的数据，直接置位null，比如group、threadlocal等。
    private void exit() {
        if (group != null) {
            group.threadTerminated(this);
            group = null;
        }
        /* Aggressively null out all reference fields: see bug 4006245 */
        target = null;
        /* Speed the release of some of these resources */
        threadLocals = null;
        inheritableThreadLocals = null;
        inheritedAccessControlContext = null;
        blocker = null;
        uncaughtExceptionHandler = null;
    }


    
    // 强制线程退出.此时会创建一个新的对象ThreadDeath作为异常.
    // 允许对一个还没有start的线程执行此方法.如果当前线程已经start了,则此方法的调用会使其立即停止.
     
    // 客户端不应该经常去捕获ThreadDeath异常,除非有一些额外的清除工作要做(注意:在线程死亡前,ThreadDeath的异常在抛出时会引发try对应的finally代码块的执行).如果catch捕获了ThreadDeath对象,必须重新抛出此异常以保证线程可以真正的死亡.
     
    // 最顶级的错误处理器会对其它未被捕获类型的异常进行处理,但是如果未处理异常是线程死亡的实例，则不会打印消息或通知应用程序。

    // 这个方法的注释老长了~但是中间有一段很重要的，讲他为啥要被   @Deprecated (注释自己看jdk源码)
    // 大致意思就是这个方法是一个不安全的方法，会unlock all of the monitors(unlock所有它获取的锁)，这个还是建议看一下，几个被@Deprecated的方法都建议看下原因
    @Deprecated
    public final void stop() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            checkAccess();
            if (this != Thread.currentThread()) {
                security.checkPermission(SecurityConstants.STOP_THREAD_PERMISSION);
            }
        }
        // A zero status value corresponds to "NEW", it can't change to
        // not-NEW because we hold the lock.
        if (threadStatus != 0) {
            resume(); // Wake up thread if it was suspended; no-op otherwise
        }

        // The VM can handle all thread states
        stop0(new ThreadDeath());
    }



    @Deprecated
    public final synchronized void stop(Throwable obj) {
        throw new UnsupportedOperationException();
    }
    /**
     * 此方法功能:中断当前线程.
     *
     * 除非当前线程在中断自己(这么做是允许的),此线程的checkAccess()方法被调用且抛出异常SecurityException
     * 1.如果当前线程由于wait类型方法,join类型方法或者sleep类型的方法的调用被阻塞,则它的中断状态将被清除且会收到一个,中断异常InterruptedException
     * 2.如果此线程由于java.nio.channels.InterruptibleChannel类中的InterruptibleChannel的I/O操作而被阻塞, 则此方法会导致通道被关闭,且线程的中断状态会被重置,同时线程会收到一个异常ClosedByInterruptException.
     * 3.如果此线程由于java.nio.channels.Selector而阻塞,则线程的中断状态会被重置,且它将立即从阻塞的selection操作返回, 且返回值通常是一个非零值,这就和java.nio.channels.Selector#wakeup的wakeup()方法被调用一样.
     * 4.如果前面的条件都不成立，那么该线程的中断状态将被重置.。
     *
     * 中断一个处于非活着状态的线程并不需要产生任何其它影响.
     */
    // 实际上只是给线程设置一个中断标志，线程仍会继续运行。并不会直接干掉线程
    public void interrupt() {
        if (this != Thread.currentThread())
            checkAccess();

        synchronized (blockerLock) {
            Interruptible b = blocker;
            if (b != null) {
                interrupt0();           // Just to set the interrupt flag
                b.interrupt(this);
                return;
            }
        }
        interrupt0();
    }


    // 作用是测试当前线程是否被中断（检查中断标志），返回一个boolean并清除中断状态，第二次再调用时中断状态已经被清除，将返回一个false。
    public static boolean interrupted() {
        return currentThread().isInterrupted(true);
    }


    //作用是只测试此线程是否被中断 ，不清除中断状态。
    public boolean isInterrupted() {
        return isInterrupted(false);
    }

    /**
     * 测试一些线程是否被中断.
     * 中断状态会被重置or并不依赖于之前的中断清除的值.
     */
    private native boolean isInterrupted(boolean ClearInterrupted);


    /**
     * Throws {@link NoSuchMethodError}.
     * 此方法在最开始被设计的目的是:不带任何清除操作的销毁一个线程.
     * 此方法被调用后线程持有的监听器依旧处于锁状态.
     * 然而,此方法从未被实现过.如果被实现,则由于suspend()的问题会带来死锁.
     * 当目标线程被销毁时,如果它持有了一个用于保护临界资源的锁,那么会导致此临界资源再也无法被其它线程使用.
     * 如果其它线程尝试对此资源进行加锁,就会导致死锁.这种死锁通常表现为"冻结"状态.
     * @throws NoSuchMethodError always
     */

    @Deprecated
    public void destroy() {
        throw new NoSuchMethodError();
    }

    //判断当前的线程是否处于活动状态；活动状态就是线程已经启动尚未终止，那么这时候线程就是存活的，则返回true，否则则返回false；
    public final native boolean isAlive();


    // 挂起此线程，首先，会调用checkAccess()方法，这样可能就会抛出 SecurityException
    // 如果此线程是Alive的，它将会被挂起，并且不会继续执行，只有当此线程被恢复(resumed)
    @Deprecated
    public final void suspend() {
        checkAccess();
        suspend0();
    }

 
     // 恢复被suspended的线程，首先，会调用checkAccess()方法，这样可能就会抛出 SecurityException
    // 如果此线程是Alive的，但是被suspended了，此线程会被恢复（resume）,然后继续执行，
    // @Deprecated原因：可能导致死锁
    
    @Deprecated
    public final void resume() {
        checkAccess();
        resume0();
    }

    //设置优先级的方法,newPriority在规定的范围内时，如果其大于线程组允许的最大的优先级，那么就直接将优先级设置为线程组允许的最大的优先级
    public final void setPriority(int newPriority) {
        ThreadGroup g;
        checkAccess();
        if (newPriority > MAX_PRIORITY || newPriority < MIN_PRIORITY) {
            throw new IllegalArgumentException();
        }
        //要求此线程必须有所属线程组
        if((g = getThreadGroup()) != null) {
            if (newPriority > g.getMaxPriority()) {
                newPriority = g.getMaxPriority();
            }
            setPriority0(priority = newPriority);
        }
    }

   // 获取线程的优先级
    public final int getPriority() {
        return priority;
    }

     //这是一个同步方法,用于设定线程名
    public final synchronized void setName(String name) {
        checkAccess();
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        this.name = name;
        if (threadStatus != 0) {
            setNativeName(name);
        }
    }

 
    //返回此线程的名字
    public final String getName() {
        return name;
    }

    //返回线程所属的线程组
    public final ThreadGroup getThreadGroup() {
        return group;
    }


    //此方法返回活动线程的当前线程的线程组中的数量,返回的值只是一个估值。因为，此方法遍历内部数据结构时，线程可能会动态的改变，也可能被某些系统线程影响，此线程主要用于调试以及监控
    public static int activeCount() {
        return currentThread().getThreadGroup().activeCount();
    }

    //ThreadGroup类的enumerate()方法用于将每个活动线程的线程组及其子组复制到指定的数组中
    //一个应用如果想获得这个线程数组,则它必须调用此方法,然而如果此数组太小而无法存放所有的线程,则放不下的线程就自动被忽略了.其实从线程组里面获取存活线程的方法是受到争议的,此方法调用者应该证明方法返回值应该严格小于参数数组的长度.
    // 因为此方法在被调用时存在竞争,因此建议此方法只用于debug和监听目的.
    public static int enumerate(Thread tarray[]) {
        return currentThread().getThreadGroup().enumerate(tarray);
    }

    /**
     * @exception  IllegalThreadStateException  if this thread is not
     *             suspended.
     * @deprecated The definition of this call depends on {@link #suspend},
     *             which is deprecated.  Further, the results of this call
     *             were never well-defined.
     */
    // 统计此线程中栈帧(stack frames)的数量，此线程必须被挂起
    // 如果线程没有被挂起会返回异常：IllegalThreadStateException
    // 这个方法的定义依赖了@Deprecated #suspend()，而且这个方法的没有对返回结果给出很好的定义
    @Deprecated
    public native int countStackFrames();

   
    // 重点来了~这是重点啊 
    // 这个很重要，join方法的理解很重要，我先标明了一遍我找到的一篇资料，后面会专门自己整理
    // 此方法在实现上:循环调用以this.isAlive()方法为条件的wait()方法.当线程终止时notifyAll()方法会被调用.建议应用程序不要在线程实例上使用wait,notify,notifyAll方法.
    // 那么问题来了，这个notify是在哪触发的呢？在这个方法的实现中并没有吧？上面的文章中描述的挺清楚的，https://blog.csdn.net/u010983881/article/details/80257703 ;
    public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }

     //等待时间单位为纳秒,其它解释都和上面方法一样
    public final synchronized void join(long millis, int nanos)
    throws InterruptedException {

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos >= 500000 || (nanos != 0 && millis == 0)) {
            millis++;
        }

        join(millis);
    }

 
     //方法功能:等待一直到线程死亡.
    public final void join() throws InterruptedException {
        join(0);
    }

    // 打印一个追踪当前线程的堆栈,此方法只用于debug
    public static void dumpStack() {
        new Exception("Stack trace").printStackTrace();
    }


    //JAVA线程分为即实线程与守护线程,当只有守护线程运行的时候jvm就已经exits了，就是说守护线程存活与否不影响JVM的退出的线程，实现守护线程的方法是在线程start()之前setDaemon(true)，否则会抛出一个IllegalThreadStateException异常。
    public final void setDaemon(boolean on) {
        checkAccess();
        if (isAlive()) {
            throw new IllegalThreadStateException();
        }
        daemon = on;
    }

 
    public final boolean isDaemon() {
        return daemon;
    }


    // 确定当前运行的线程是否有权利更改此线程.
    // 如果有安全管理器,则会将当前线程 作为安全管理器的参数 传入安全管理器的checkAccess()方法.
    // 如果当前线程不被允许访问此线程，那么会抛出SecurityException
    public final void checkAccess() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkAccess(this);
        }
    }

  
    public String toString() {
        ThreadGroup group = getThreadGroup();
        if (group != null) {
            return "Thread[" + getName() + "," + getPriority() + "," +
                           group.getName() + "]";
        } else {
            return "Thread[" + getName() + "," + getPriority() + "," +
                            "" + "]";
        }
    }

    
    // 此方法返回此线程的上下文类加载器.
    // 上下文加载器由线程的创建者 在加载资源和类的代码running之前提供，这个地方的这句英文注释我觉得不是很好理解特意贴出来(The context ClassLoader is provided by the creator of the thread for use by code running in this thread when loading classes and resource)(use by :在....之前使用)(主要是这个use by容易理解错，我看了网上的好多翻译都有问题，所以特意标注一下)
    // 如果通过方法setContextClassLoader进行上下文类加载器的设定,则默认的上下文类加载器为父线程.
    // 原始线程的类加载器通常被设定为:加载应用的类加载器.
    // 如果安全管理器存在,且调用者的类加载器不为null,且它们不相同,且也不是父子关系,则此方法的调用会导致安全管理的方法checkPermission的调用,用于确定对上下文类加载器的检索是否被允许.
    //@CallerSensitive 这个注解是为了堵住漏洞用的。曾经有黑客通过构造双重反射来提升权限，原理是当时反射只检查固定深度的调用者的类，看它有没有特权，例如固定看两层的调用者（getCallerClass(2)）。如果我的类本来没足够权限群访问某些信息，那我就可以通过双重反射去达到目的：反射相关的类是有很高权限的，而在 我->反射1->反射2 这样的调用链上，反射2检查权限时看到的是反射1的类，这就被欺骗了，导致安全漏洞。使用CallerSensitive后，getCallerClass不再用固定深度去寻找actual caller（“我”），而是把所有跟反射相关的接口方法都标注上CallerSensitive，搜索时凡看到该注解都直接跳过，这样就有效解决了前面举例的问题。

    @CallerSensitive
    public ClassLoader getContextClassLoader() {
        if (contextClassLoader == null)
            return null;
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            ClassLoader.checkClassLoaderPermission(contextClassLoader,
                                                   Reflection.getCallerClass());
        }
        return contextClassLoader;
    }

    // 定一个线程的上下文类加载器.上下文类加载器可以在线程被创建时被设定,且允许线程创建者提供合适的类加载器.如果存在安全管理器,则它的checkPermission()方法会被调用,用于查看设定上下文类加载器的行为是否被允许.
     * @since 1.2
    public void setContextClassLoader(ClassLoader cl) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("setContextClassLoader"));
        }
        contextClassLoader = cl;
    }

    // https://blog.csdn.net/w410589502/article/details/54949506
    // 当且仅当当前线程拥有某个具体对象的锁，它返回true
    // 这一方法被设计的目的是:用于程序自身去声明它已经有某个对象的锁啦
    public static native boolean holdsLock(Object obj);

    private static final StackTraceElement[] EMPTY_STACK_TRACE
        = new StackTraceElement[0];


    // 返回表示该线程堆栈转储的堆栈跟踪元素数组。如果线程还没有start,or虽然start了,但是并没有被CPU调用过,or线程以及终止了,则返回数组长度为0.如果返回数组长度非0,则数组中第一个元素(索引为0)代表了栈的顶部,就是所有调用方法中距离现在时间最近的那个.数组的最后一个元素代表了栈的底部,这是所有调用方法中距离现在时间最远的那个.
    // 如果存在安全管理器,且这一线程又不是当前线程,则安全管理器的checkPermission()方法会被调用以查看.
    // 一些虚拟机在某些情况下,可能会在栈跟踪时遗漏至少一个以上的栈.在极端情况下,虚拟机没有任何栈跟踪信息所以返回数组长度为0.
    public StackTraceElement[] getStackTrace() {
        if (this != Thread.currentThread()) {
            // check for getStackTrace permission
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                security.checkPermission(
                    SecurityConstants.GET_STACK_TRACE_PERMISSION);
            }
            // optimization so we do not call into the vm for threads that
            // have not yet started or have terminated
            if (!isAlive()) {
                return EMPTY_STACK_TRACE;
            }
            StackTraceElement[][] stackTraceArray = dumpThreads(new Thread[] {this});
            StackTraceElement[] stackTrace = stackTraceArray[0];
            // a thread that was alive during the previous isAlive call may have
            // since terminated, therefore not having a stacktrace.
            if (stackTrace == null) {
                stackTrace = EMPTY_STACK_TRACE;
            }
            return stackTrace;
        } else {
            // Don't need JVM help for current thread
            return (new Exception()).getStackTrace();
        }
    }

    //获取虚拟机中所有线程的StackTraceElement对象，可以查看堆栈
    // 返回一个用于所有存活线程的栈跟踪信息的map.
    // map的key是每个线程;value是对应线程的栈跟踪元素的一个数组.
    // 当此方法被调用时,可能有些线程正在执行.每一个线程的栈跟踪信息都代表了线程在某一时刻状态的快照且每一个栈跟踪信息会在不同的时间得到.如果虚拟机中某一个线程没有栈跟踪信息则其数组长度为0.
    // 如果有安全管理器,则安全管理器的checkPermission方法会被调用以检查是否允许获取所有线程的栈跟踪信息.
    // @since 1.5
    public static Map<Thread, StackTraceElement[]> getAllStackTraces() {
        // check for getStackTrace permission
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(
                SecurityConstants.GET_STACK_TRACE_PERMISSION);
            security.checkPermission(
                SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
        }

        // Get a snapshot of the list of all threads
        Thread[] threads = getThreads();
        StackTraceElement[][] traces = dumpThreads(threads);
        Map<Thread, StackTraceElement[]> m = new HashMap<>(threads.length);
        for (int i = 0; i < threads.length; i++) {
            StackTraceElement[] stackTrace = traces[i];
            if (stackTrace != null) {
                m.put(threads[i], stackTrace);
            }
            // else terminated so we don't put it in the map
        }
        return m;
    }


    // 常量： 运行时许可证
    private static final RuntimePermission SUBCLASS_IMPLEMENTATION_PERMISSION =
                    new RuntimePermission("enableContextClassLoaderOverride");

    //子类安全审核结果的缓存
    //在将来如果它出现的话,可以替代ConcurrentReferenceHashMap
    private static class Caches {
        //子类安全审核结果的缓存值
        static final ConcurrentMap<WeakClassKey,Boolean> subclassAudits =
            new ConcurrentHashMap<>();

        //审核子类的弱引用队列
        static final ReferenceQueue<Class<?>> subclassAuditsQueue =
            new ReferenceQueue<>();
    }

   // 证明创建当前子类实例能够忽略安全限制:子类不能覆盖安全敏感,非final类型的方法,否则enableContextClassLoaderOverride这个运行时许可会被坚持.
    private static boolean isCCLOverridden(Class<?> cl) {
        if (cl == Thread.class)
            return false;

        processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
        WeakClassKey key = new WeakClassKey(cl, Caches.subclassAuditsQueue);
        Boolean result = Caches.subclassAudits.get(key);
        if (result == null) {
            result = Boolean.valueOf(auditSubclass(cl));
            Caches.subclassAudits.putIfAbsent(key, result);
        }

        return result.booleanValue();
    }

    // 在给定子类上的检查操作以证明它并未覆写安全敏感,非final类型的方法.
    // 如果子类覆写了所有的此类型的方法 则返回true，否则false.
    private static boolean auditSubclass(final Class<?> subcl) {
        Boolean result = AccessController.doPrivileged(
            new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    for (Class<?> cl = subcl;
                         cl != Thread.class;
                         cl = cl.getSuperclass())
                    {
                        try {
                            cl.getDeclaredMethod("getContextClassLoader", new Class<?>[0]);
                            return Boolean.TRUE;
                        } catch (NoSuchMethodException ex) {
                        }
                        try {
                            Class<?>[] params = {ClassLoader.class};
                            cl.getDeclaredMethod("setContextClassLoader", params);
                            return Boolean.TRUE;
                        } catch (NoSuchMethodException ex) {
                        }
                    }
                    return Boolean.FALSE;
                }
            }
        );
        return result.booleanValue();
    }

    private native static StackTraceElement[][] dumpThreads(Thread[] threads);
    private native static Thread[] getThreads();
    public long getId() {
        return tid;
    }

   
    // NEW表示:线程还未开始,只是进行了一些线程创建的初始化操作,但未调用start()方法.
    // RUNNABLE表示:线程在JVM里面处于运行状态(这里就绪和运行同属于运行).
    // BLOCKED表示:线程正在等待一个监视器锁,处于阻塞状态.
    // WAITING表示:一个线程在等待另一个线程的特定操作(通知or中断),这种等待是无限期的.
    // TIMED_WAITING表示:一个线程在等待另一个线程的特定操作,这种等待是有时间限制的.一旦超时则线程自行返回.
    // TERMINATED表示:线程已退出.表示线程已经执行完毕.
    public enum State {
        
        NEW,

        RUNNABLE,

        //在调用完wait()方法后,为了进入同步方法(锁)或者重进入同步方法(锁).
        BLOCKED,

        /**
         * 一个线程处于wating状态,是因为调用了下面方法中的某一个:
         * 1.Object.wait
         * 2.Thread.join
         * 3.LockSupport.park
         *
         * 其它线程的特定操作包括 :notify(),notifyAll(),join()等.
         */
        WAITING,
      
        /**
         * 线程等待指定时间.
         * 这种状态的出现是因为调用了下面方法中的某一个:
         * 1.Thread.sleep()
         * 2.Object.wait()
         * 3.Thread.join()
         * 4.LockSupport.parkNanos()
         * 5.LockSupport.parkUntil()
         */
        TIMED_WAITING,

        TERMINATED;
    }

    /**
     * 返回线程状态.
     * 这一方法的设计目的:用于系统状态的监听,而非同步控制.
     * @since 1.5
     */
    public State getState() {
        // get current thread state
        return sun.misc.VM.toThreadState(threadStatus);
    }

    // Added in JSR-166


    /**
     * 由于未捕获异常而导致线程终止的函数接口处理器.
     * 当一个线程由于未捕获异常而终止时,JVM将会使用getUncaughtExceptionHandler来查询此线程的UncaughtExceptionHandler,
     * 且会调用处理器handler的uncaughtException()方法,将此线程和其异常作为参数.
     *
     * 如果一个线程没有它特定的UncaughtExceptionHandler,则它所属的线程组对象充当其UncaughtExceptionHandler.
     * 如果线程组对象没有处理异常的指定请求,它可以向前调用getDefaultUncaughtExceptionHandler的默认处理异常的方法.
     *
     * @see #setDefaultUncaughtExceptionHandler
     * @see #setUncaughtExceptionHandler
     * @see ThreadGroup#uncaughtException
     * @since 1.5
     */
    // 异常处理接口
    @FunctionalInterface // 函数式接口，现在jdk都12了，lambda如果都还不愿意了解一下~，我觉得你得反思一下了，jdk8就出来的特性了。实际上挺简单的，做程序员，不停的学习是不可避免的
    public interface UncaughtExceptionHandler {
        
        // 由于未捕获异常而导致线程终止的方法调用.
        // 此方法抛出的任何异常都会被JVM忽略.
        void uncaughtException(Thread t, Throwable e);
    }

    // null unless explicitly set
    private volatile UncaughtExceptionHandler uncaughtExceptionHandler;

    // null unless explicitly set
    private static volatile UncaughtExceptionHandler defaultUncaughtExceptionHandler;


     /**
     * 设定默认处理器用于处理:由于未捕获异常而导致的线程终止,且此线程还未定义任何其它的处理器.
     *
     * 未捕获异常首先由本线程进行处理,然后由线程所属的线程组对象处理,最后由默认未捕获异常处理器进行处理.
     * 如果线程未设定明确的未捕获异常处理器,且线程的线程组(包括父线程组)也未指定,则此时默认处理器的uncaughtException
     * 方法会被执行.由于设定了默认的未捕获异常处理器,则应用能够更改未捕获异常的处理方法.
     *
     * 注意:默认的未捕获异常处理器不应该经常使用线程的线程组对象,因为这会引起无限递归.
     *
     * @since 1.5
     */
    public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(
                new RuntimePermission("setDefaultUncaughtExceptionHandler")
                    );
        }

         defaultUncaughtExceptionHandler = eh;
     }

 
    // 当一个线程因未捕获异常而突然终止时,返回的默认处理器.
    public static UncaughtExceptionHandler getDefaultUncaughtExceptionHandler(){
        return defaultUncaughtExceptionHandler;
    }


    //返回由于未捕获异常而导致线程中断的处理器.如果此线程无未捕获异常处理器,则返回此线程的线程组对象
    //如果此线程已经终止,则返回null.
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler != null ?
            uncaughtExceptionHandler : group;
    }

 
    /**
     * 设定一个由于未捕获异常而导致线程中断的处理器.
     * 通过设定未捕获异常处理器,一个线程可以完全控制如何处理未捕获异常.
     * 如果没有设定未捕获异常,则线程组对象默认为其未捕获异常处理器.
     * @since 1.5
     */
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        checkAccess();
        uncaughtExceptionHandler = eh;
    }

    /**
     * Dispatch an uncaught exception to the handler. This method is
     * intended to be called only by the JVM.
     */
    //将未捕获异常分发给处理器.这一方法通常被JVM调用.
    private void dispatchUncaughtException(Throwable e) {
        getUncaughtExceptionHandler().uncaughtException(this, e);
    }

    /**
     * Removes from the specified map any keys that have been enqueued
     * on the specified reference queue.
     */
    // 删除指定map中那些在特定引用队列中已经排队的所有key
    static void processQueue(ReferenceQueue<Class<?>> queue,
                             ConcurrentMap<? extends
                             WeakReference<Class<?>>, ?> map)
    {
        Reference<? extends Class<?>> ref;
        while((ref = queue.poll()) != null) {
            map.remove(ref);
        }
    }

    /**
     *  Weak key for Class objects.
     **/
    static class WeakClassKey extends WeakReference<Class<?>> {
        /**
         * saved value of the referent's identity hash code, to maintain
         * a consistent hash code after the referent has been cleared
         */
        // 用于保存引用的参照hash值,一旦参照明确后用于保持一个持续的hash值.
        private final int hash;

        /**
         * Create a new WeakClassKey to the given object, registered
         * with a queue.
         */
        // 根据给定的对象创建一个weakClassKey,使用给定队列进行注册.
        WeakClassKey(Class<?> cl, ReferenceQueue<Class<?>> refQueue) {
            super(cl, refQueue);
            hash = System.identityHashCode(cl);
        }

        /**
         * Returns the identity hash code of the original referent.
         */
        @Override
        public int hashCode() {
            return hash;
        }

        /**
         * Returns true if the given object is this identical
         * WeakClassKey instance, or, if this object's referent has not
         * been cleared, if the given object is another WeakClassKey
         * instance with the identical non-null referent as this one.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;

            if (obj instanceof WeakClassKey) {
                Object referent = get();
                return (referent != null) &&
                       (referent == ((WeakClassKey) obj).get());
            } else {
                return false;
            }
        }
    }


  	/**
     * 以下三个初始未初化的变量专门由java.util.concurrent.ThreadLocalRandom管理.
     * 这些变量用在并发代码中构建高性能的PRNGs,由于存在共享失败的情况所以我们不能冒险共享.
     * 因此,这些变量和注解@Contended是隔离的.
     */

    /** The current seed for a ThreadLocalRandom */
    @sun.misc.Contended("tlr")
    long threadLocalRandomSeed;

    /** Probe hash value; nonzero if threadLocalRandomSeed initialized */
    @sun.misc.Contended("tlr")
    int threadLocalRandomProbe;

    /** Secondary seed isolated from public ThreadLocalRandom sequence */
    @sun.misc.Contended("tlr")
    int threadLocalRandomSecondarySeed;

    /* Some private helper methods */
    private native void setPriority0(int newPriority);
    private native void stop0(Object o);
    private native void suspend0();
    private native void resume0();
    private native void interrupt0();
    private native void setNativeName(String name);
}




```

