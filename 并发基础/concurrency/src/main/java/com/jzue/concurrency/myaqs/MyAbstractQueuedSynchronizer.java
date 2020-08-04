package com.jzue.concurrency.mycoucurrency;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;


/**
 * @Author: junzexue
 * @Date: 2019/4/16 下午2:03
 * @Description:
 * http://ifeve.com/java-special-troops-aqs/
 * https://www.cnblogs.com/waterystone/p/4920797.html
 *
 * 同步对列（也有叫阻塞队列的），running状态的线程，他们是获取了锁的线程，它是利用Node的pre和next维护的一个双向链表
 * 而条件队列(亦或是叫等待队列)，waiting状态的线程，他们是在等待获取锁的线程，它是利用Node 的nextWaiter 维护的单向链表
 *  问题一：为什么同步队列双向链表，条件队列双向链表
 * 同步队列是双向的：原因是当前节点可能需要检查前面节点的等待状态，例如 shouldParkAfterFailedAcquire
 * 条件队列是单向的：只存储的是排他锁，所以单向唤醒即可
 *  问题二：抢占锁的大致过程(以reentrantlock为例)
 *      1-new Lock(),线程尝试获取锁（参数是锁的层数），然后为state（代表锁的层数）赋值，因为是首次然后aqs会把线程封装尾一个结点，
 *      2-然后会判断当前的这个结点的前驱是不是头结点，是头结点，会尝试获取锁，如果不是，或者抢占锁没抢过（非公平锁）,
 *      那么会把这个新加入的结点的前驱的等待状态设置为signal,
 *      就是说，前驱节点的线程如果释放了同步状态或者被取消，将会通知后继节点，使后继节点的线程得以运行；
 *      3-操作成功以后然后LockSupport.park挂起线程，被挂起的线程，就是阻塞态 cpu不分配运行时间，
 *      这时候线程等待被唤醒，唤醒以后线程会接着刚才的代码继续执行，
 *      然后前面的线程都执行完了，当前线程的前驱结点为head，这个时候head执行完毕就会唤醒当前结点
 *      4-然后执行自己的业务逻辑
 *      5-然后解锁unlock,然后会判断state是否为0,保证重入锁都被释放了，然后release，然后会通过LockSupport.unpark()唤醒head结点后面的首个不处于cancel的结点
 *      6-然后线程重新开始获取锁，以此类推
 **/
public abstract class MyAbstractQueuedSynchronizer extends MyAbstractOwnableSynchronizer{

    protected MyAbstractQueuedSynchronizer() { }

    //  头结点-它就是当前获取锁的线程
    private  transient volatile Node head;

    //  尾结点
    private  transient volatile Node tail;

    //  计数器，表示的是当前线程获取的锁的层数
    private  volatile  int state;
    protected final int getState() {
        return state;
    }
    protected final void setState(int newState) {
        state = newState;
    }

    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
    /**
     * 是否需要休眠的状态判断临界值常量
     **/
    static final long spinForTimeoutThreshold = 1000L;

    static final class Node{

        /**
         * 标识当前结点是共享结点
         **/
        static final Node SHARED = new Node();
        /**
         * 标识当前结点是独占
         **/
        static final Node EXCLUSIVE = null;
        /**
         * 场景：当该线程等待超时或者被中断，需要从同步队列中取消等待，则该线程被置1，
         * 即被取消（这里该线程在取消之前是等待状态）。节点进入了取消状态则不再变化
         **/
        static final int CANCELLED =  1;
        /**
         * 后继的节点处于等待获取锁的状态，当前节点的线程如果释放了同步状态或者被取消（当前节点状态置为-1），
         * 将会通知后继节点，使后继节点的线程得以运行；
         **/
        static final int SIGNAL    = -1;
        /**
         * 节点处于条件队列中，当其他线程对Condition调用了signal()方法后，
         * 该节点从条件队列中转移到同步队列中，加入到对同步状态的获取中；
         **/
        static final int CONDITION = -2;
        /**
         * 与共享模式相关，在共享模式中，该状态标识结点的线程处于可运行状态
         */
        static final int PROPAGATE = -3;
        /**
         * 上面的CANCELLED、SIGNAL、CONDITION、PROPAGATE分别是其的4种状态，值为0，代表初始化状态
         **/
        volatile int waitStatus;
        /**
         * 前驱结点
         */
        volatile Node prev;
        /**
         * 后置结点
         **/
        volatile Node next;
        /**
         * 当前线程
         **/
        volatile Thread thread;
        /**
         * Node既可以作为同步队列节点使用，也可以作为条件队列(waitStatus=Condition)节点使用(将会在后面讲Condition时讲到)。
         * 在作为同步队列节点时，nextWaiter可能有两个值：EXCLUSIVE、SHARED标识当前节点是独占模式还是共享模式；
         * 在作为条件队列节点使用时，nextWaiter保存后继节点。
         **/
        Node nextWaiter;

        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * 返回前驱结点
         **/
        final MyAbstractQueuedSynchronizer.Node predecessor() throws NullPointerException {
            MyAbstractQueuedSynchronizer.Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }
        /**
         * 构造同步队列结点-在作为同步队列节点时，nextWaiter可能有两个值：EXCLUSIVE、SHARED标识当前节点是独占模式还是共享模式；
         * Used by addWaiter
         **/
        Node(Thread thread, MyAbstractQueuedSynchronizer.Node mode) {
            this.nextWaiter = mode;
            this.thread = thread;
        }
        /**
         * 构造条件队列结点
         * Used by Condition
         **/
        Node(Thread thread, int waitStatus) {
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }
    /**
     * 添加结点，如果双向链表为空，直接添加头结点尾结点为相同的一个结点
     * 如果双向链表不为空的话，直接在尾结点后面加
     **/
    private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                node.prev = t;
                /**
                 * 下面的if中我把t换成了tail,便于理解
                 **/
                if (compareAndSetTail(tail, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
    /**
     * 将当前线程添加到等待队列尾部，并标记为对应的模式，如果尾结点不为空，则直接在尾结点加然后return，否则通过enq(node)（实质用的是为空的情况）
     **/
    private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
    /**
     *  首先调用setHead(Node)的操作，这个操作内部会将传入的node节点作为AQS的head所指向的节点。
     *  线程属性设置为空（因为现在已经获取到锁，不再需要记录下这个节点所对应的线程了），再将这个节点的perv引用赋值为null。
     **/
    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }



    /**
     * todo: 这个是独占锁的获取入口,是aqs的入口
     **/
    public final void acquire(int arg) {

        /**
         * 1-tryAcquire()尝试直接去获取资源，如果成功则直接返回；如果失败，执行2
         * 2-addWaiter()将该线程加入等待队列的尾部，并标记为独占模式；
         * 3-acquireQueued()使线程在等待队列中获取资源，一直获取到资源后才返回。如果在整个等待过程中被中断过，则返回true，否则返回false。
         * 4-如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。
         **/
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
    /**
     * 此方法的作用是在同步队列中排队，直到拿到锁才返回
     *
     *这里也是一个死循环，除非进入if(p == head && tryAcquire(arg))这个判定条件，而p为node.predcessor()得到，
     * 这个方法返回node节点的前一个节点，也就是说只有当前一个节点是head的时候，进一步尝试通过tryAcquire(arg)来征用才有机会成功。
     * tryAcquire(arg)这个方法我们前面介绍过，成立的条件为：锁的状态为0，
     * 且通过CAS尝试设置状态成功或线程的持有者本身是当前线程才会返回true，我们现在来详细拆分这部分代码。
     *
     * 如果这个条件成功后，发生的几个动作包含：
     * （1） 首先调用setHead(Node)的操作，这个操作内部会将传入的node节点作为AQS的head所指向的节点。
     *      线程属性设置为空（因为现在已经获取到锁，不再需要记录下这个节点所对应的线程了），再将这个节点的perv引用赋值为null。
     * （2） 进一步将的前一个节点的next引用赋值为null。
     *      在进行了这样的修改后，队列的结构就变成了以下这种情况了，通过这样的方式，就可以让执行完的节点释放掉内存区域，
     *      而不是无限制增长队列，也就真正形成FIFO了：
     **/
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                // p == head 说明当前节点虽然进到了阻塞队列，但是是阻塞队列的第一个，因为它的前驱是head
                // 注意，阻塞队列不包含head节点，head一般指的是占有锁的线程，head后面的才称为阻塞队列
                // 所以当前节点可以去试抢一下锁
                // 这里我们说一下，为什么可以去试试：
                // 首先，它是队头，这个是第一个条件，其次，当前的head有可能是刚刚初始化的node，
                // enq(node) 方法里面有提到，head是延时初始化的，而且new Node()的时候没有设置任何线程
                // 也就是说，当前的head不属于任何一个线程，所以作为队头，可以去试一试，
                // tryAcquire已经分析过了, 忘记了请往前看一下，就是简单用CAS试操作一下state
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                // 到这里，说明上面的if分支没有成功，要么当前node本来就不是队头，
                // 要么就是tryAcquire(arg)没有抢赢别人，继续往下看
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            // 什么时候 failed 会为 true???
            // tryAcquire() 方法抛异常的情况
            if (failed)
                cancelAcquire(node);
        }
    }
    /**
     * 中断当前线程
     **/
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }
    /**
     * 给子类实现的方法
     **/
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }
    /**
     *此方法就是让线程去休息，真正进入等待状态,通过unsafe.park阻塞当前线程，
     * 被挂起的线程，就是阻塞态 cpu不分配运行时间，当重新被unpark时，线程就会继续往下执行
     * 重点是这个返回值，返回的是-当前线程是否被中断
     **/
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);//调用park()使线程进入waiting状态
        return Thread.interrupted();
    }
    /**
     * 设置前驱为SIGNAL状态，让前驱转换的时候回来提醒当前结点
     **/
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;//拿到前驱的状态
        if (ws == Node.SIGNAL)
            //如果已经告诉前驱拿完号后通知自己一下，那就可以安心休息了
            return true;
        if (ws > 0) {
            /**
             * 如果前驱放弃了，那就一直往前找，直到找到最近一个正常等待的状态，并排在它的后边。
             * 注意：那些放弃的结点，由于被自己“加塞”到它们前边，它们相当于形成一个无引用链，稍后就会被保安大叔赶走了(GC回收)！
             **/
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            //如果前驱正常，那就把前驱的状态设置成SIGNAL，告诉它拿完号后通知自己一下。有可能失败，人家说不定刚刚释放完呢！
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
    /**
     * 将Node置为cancel状态，然后从链表中移除
     **/
    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        if (node == null)
            return;
        //1. node不再关联到任何线程
        node.thread = null;
        //2. 跳过被cancel的前继node，找到一个有效的前继节点pred
        Node pred = node.prev;
        while (pred.waitStatus > 0){
            pred=pred.prev;
            node.prev=pred;
            //上面两行等价于node.prev = pred = pred.prev;
        }
        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        Node predNext = pred.next;
        //3. 将node的waitStatus置为CANCELLED
        node.waitStatus = Node.CANCELLED;
        // node是tail，更新tail为pred，并使pred.next指向null
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            int ws;
            //5. 如果node既不是tail，又不是head的后继节点
            //则将node的前继节点的waitStatus置为SIGNAL
            //并使node的前继节点指向node的后继节点
            if (pred != head &&
                    ((ws = pred.waitStatus) == Node.SIGNAL ||
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                    pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                //6. 如果node是head的后继节点，则直接唤醒从node开始的有效结点
                unparkSuccessor(node);
            }
            node.next = node; // help GC
        }
    }
    /**
     * 唤醒同步队列中的遍历到的第一个未cancel的结点
     **/
    private void unparkSuccessor(Node node) {
        int ws = node.waitStatus;
        if (ws < 0)//置零当前线程所在的结点状态，允许失败
            compareAndSetWaitStatus(node, ws, 0);

        //如果没有后继结点，或者waitStatus>0==》解除其与后继结点的关系
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
             // 从尾到前遍历，找到最前面的未cancel的结点
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        //然后对于未放弃的线程unpark唤醒
        if (s != null)
            LockSupport.unpark(s.thread);
    }
    /**
     * todo: 这里是共享锁的获取入口
     **/
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }
    /**
     * ** 尝试获取共享锁，由实现类自己加逻辑，但是实现逻辑需要注意以下两点
     * 一、该方法必须自己检查当前上下文是否支持获取共享锁，如果支持再进行获取。
     * 二、该方法返回值是个重点。
     *      其一、由上面的源码片段可以看出返回值小于0表示获取锁失败，需要进入等待队列。
     *      其二、如果返回值等于0表示当前线程获取共享锁成功，但它后续的线程是无法继续获取的，也就是不需要把它后面等待的节点唤醒。
     *      最后、如果返回值大于0，表示当前线程获取共享锁成功且它后续等待的节点也有可能继续获取共享锁成功，
     *      也就是说此时需要把后续节点唤醒让它们去尝试获取共享锁。
     **/
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }
    /**
     * 与独占锁的acquireQueued对应，
     * 死循环获取共享锁，将当前线程加入同步队列尾部休息，直到其他线程释放资源唤醒自己
     **/
    private void doAcquireShared(int arg) {
        //添加等待节点的方法跟独占锁一样，唯一区别就是节点类型变为了共享型，不再赘述
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                //表示前面的节点已经获取到锁，自己会尝试获取锁
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    //等于0表示通行证用完了，不用唤醒后继节点，大于0表示还有可用的通行证，可以唤醒后继结点，具体逻辑在setHeadAndPropagate中
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                /**
                 * 如果没有获取锁成功，则进入挂起逻辑
                 **/
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
    //获取锁以后的唤醒操作
    // 两个入参，一个是当前成功获取共享锁的节点，一个就是tryAcquireShared方法的返回值（就是通行证的个数），
    // 注意上面说的，它可能大于0也可能等于0
    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; //记录当前头节点
        //设置新的头节点，即把当前获取到锁的节点设置为头节点
        //注：这里是获取到锁之后的操作，不需要并发控制
        setHead(node);
        //这里意思有两种情况是需要执行唤醒操作
        //1.propagate > 0 表示调用方指明了后继节点需要被唤醒
        //2.头节点后面的节点需要被唤醒（waitStatus<0），不论是老的头结点还是新的头结点
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            Node s = node.next;
            //如果当前节点的后继节点是共享类型或者没有后继节点，则进行唤醒
            //这里可以理解为除非明确指明不需要唤醒（后继等待节点是独占类型），否则都要唤醒
            if (s == null || s.isShared())
                //后面详细说
                doReleaseShared();
        }
    }
    /**
     * 唤醒结点操作
     **/
    private void doReleaseShared() {
        for (;;) {
            //唤醒操作由头结点开始，注意这里的头节点已经是上面新设置的头结点了
            //其实就是唤醒上面新获取到共享锁的节点的后继节点
            Node h = head;
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                //表示后继节点需要被唤醒
                if (ws == Node.SIGNAL) {
                    //这里需要控制并发，因为入口有setHeadAndPropagate跟release两个，避免两次unpark
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue;
                    //执行唤醒操作
                    unparkSuccessor(h);
                }
                //如果后继节点暂时不需要唤醒，则把当前节点状态设置为PROPAGATE确保以后可以传递下去
                else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue;
            }
            //如果头结点没有发生变化，表示设置完成，退出循环
            //如果头结点发生变化，比如说其他线程获取到了锁，为了使自己的唤醒动作可以传递，必须进行重试
            if (h == head)
                break;
        }
    }

    //判断节点是否在同步队列中
    final boolean isOnSyncQueue(Node node) {
        //快速判断1：节点状态或者节点没有前置节点
        //注：同步队列是有头节点的，而条件队列没有
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;
        //快速判断2：next字段只有同步队列才会使用，条件队列中使用的是nextWaiter字段
        if (node.next != null)
            return true;
        //上面如果无法判断则进入复杂判断
        return findNodeFromTail(node);
    }
    //注意这里用的是tail，这是因为条件队列中的节点是被加入到同步队列尾部，这样查找更快
    //从同步队列尾节点开始向前查找当前节点，如果找到则说明在，否则遍历到head的时候head.prev为null的时候返回false
    private boolean findNodeFromTail(Node node) {
        Node t = tail;
        for (;;) {
            if (t == node)
                return true;
            if (t == null)
                return false;
            t = t.prev;
        }
    }
    /**
     * 是否获取到了独占锁，逻辑有实现类来写
     **/
    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    /**
     * acquireQueued 的中断版，在acquireQueued 中，只是置中端标志位为true，而在此方法中是直接抛出异常，其余的逻辑一致
     **/
    private void doAcquireInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    /**
     * 在获取锁 响应中断的基础上可以超时等待。
     **/
    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
                doAcquireNanos(arg, nanosTimeout);
    }
    /**
     * acquireQueued 的超时共享版
     **/
    private boolean doAcquireNanos(int arg, long nanosTimeout)  throws InterruptedException {
        //如果限时时间小于0直接返回false
        if (nanosTimeout <= 0L)
            return false;
        //计算出等待线程截止时间：当前时间+等待时间
        final long deadline = System.nanoTime() + nanosTimeout;
        //因为tryacquire()失败，所以讲当前线程结点尾插到同步队列
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            //不断自旋将前驱结点状态设置为SIGNAL
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return true;
                }
                //如果超过截止时间，线程不再等待，获取锁失败
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
    //======================================conditionObject  条件队列start=============================

    public class ConditionObject implements Condition, Serializable{
        private static final long serialVersionUID = 1173984872572414699L;
        /**
         * 条件队列的头结点
         **/
        private transient Node firstWaiter;
        /**
         * 条件队列的尾结点
         **/
        private transient Node lastWaiter;
        /**
         *重新中断
         **/
        private static final int REINTERRUPT =  1;
        /**
         * 抛出中断异常
         **/
        private static final int THROW_IE    = -1;

        public ConditionObject() { }

        /**
         * 添加当前线程为一个Node 到条件队列，添加到尾结点
         **/
        private Node addConditionWaiter() {
            Node t =lastWaiter;
            // 因为条件队列的状态只可能是CONDITION和Cancel,所以如果最后一个结点不等于CONDITION,就认定发生过取消的操作，
            // 然后就做清除队列的已取消的结点
            if (t!=null&&t.waitStatus!=Node.CONDITION){
                unlinkCancelledWaiters();
                //将做个清楚操作的队列的尾结点赋值给t
                t = lastWaiter;
            }
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }
        /**
         * 这个方法会遍历整个条件队列，然后会将已取消的所有节点清除出队列
         **/
        private void unlinkCancelledWaiters() {
            Node t = firstWaiter;
            Node trail = null;
            while (t != null) {
                Node next = t.nextWaiter;
                if (t.waitStatus != Node.CONDITION) {
                    t.nextWaiter = null;
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    if (next == null)
                        lastWaiter = trail;
                }
                else
                    trail = t;
                t = next;
            }
        }
       /**
        *  1. 如果在 signal 之前已经中断，返回 THROW_IE
        *  2. 如果是 signal 之后中断，返回 REINTERRUPT
        *  3. 没有发生中断，返回 0
        **/
        private int checkInterruptWhileWaiting(Node node) {
            return Thread.interrupted() ?
                    (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
                    0;
        }
        /**
         * 判断signal之前是否中断
         **/
        final boolean transferAfterCancelledWait(Node node) {
            /**
             * 如果compareAndSetWaitStatus(node, Node.CONDITION, 0)执行成功，
             * 则说明中断发生时，没有signal的调用，因为signal方法会将状态设置为0；
             * 则将node添加到Sync队列中，并返回true，表示中断在signal之前；
             **/
            if (compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
                enq(node);
                return true;
            }
            /**
             * 如果第1步失败，则检查当前线程的node是否已经在Sync队列中了，
             * 如果不在Sync队列中，则让步给其他线程执行，直到当前的node已经被signal方法添加到Sync队列中；
             **/
            while (!isOnSyncQueue(node))
                Thread.yield();//放弃cpu执行的时间片，使当前线程从执行状态（运行状态）变为可执行态（就绪状态
            return false;
        }
        /**
         *
         **/
        @Override
        public void await() throws InterruptedException {
            //如果当前线程被中断则直接抛出异常
            if (Thread.interrupted())
                throw new InterruptedException();
            //把当前节点加入条件队列
            Node node = addConditionWaiter();
            //释放掉已经获取的独占锁资源
            int savedState = fullyRelease(node);
            /**
             * interruptMode
             *      0-初始化状态
             *      1-REINTERRUPT
             *      -1-THROW_IE
             **/
            int interruptMode = 0;
            //如果不在同步队列中则循环挂起（signal操作会把节点加入同步队列）
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                //这里被唤醒可能是正常的signal操作也可能是中断
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            //走到这里说明节点已经条件满足被加入到了同步队列中（执行了signal操作）或者中断了
            //这个方法很熟悉吧？就跟独占锁调用同样的获取锁方法，从这里可以看出条件队列只能用于独占锁
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            //走到这里说明已经成功获取到了独占锁，接下来就做些收尾工作
            //删除条件队列中被取消的节点
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            //根据不同模式处理中断
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
        }
        /**
         * 中断处理方法
         * 该方法根据interruptMode来确定是应该抛出InterruptedException还是继续中断。
         **/
        private void reportInterruptAfterWait(int interruptMode)
                throws InterruptedException {
            if (interruptMode == THROW_IE)
                throw new InterruptedException();
            else if (interruptMode == REINTERRUPT)
                selfInterrupt();
        }

        @Override
        public void awaitUninterruptibly() {

        }

        @Override
        public long awaitNanos(long nanosTimeout) throws InterruptedException {
            return 0;
        }

        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public boolean awaitUntil(Date deadline) throws InterruptedException {
            return false;
        }

        // 唤醒等待了最久的线程
        // 其实就是，将这个线程对应的 node 从条件队列转移到阻塞队列
        @Override
        public void signal() {
            // 调用 signal 方法的线程必须持有当前的独占锁
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignal(first);
        }
        // 从条件队列队头往后遍历，找出第一个需要转移的 node
        // 因为前面我们说过，有些线程会取消排队，但是可能还在队列中
        private void doSignal(Node first) {
            do {
                // 将 firstWaiter 指向 first 节点后面的第一个，因为 first 节点马上要离开了
                // 如果将 first 移除后，后面没有节点在等待了，那么需要将 lastWaiter 置为 null
                if ( (firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                // 因为 first 马上要被移到同步队列了，和条件队列的链接关系在这里断掉
                first.nextWaiter = null;
            } while (!transferForSignal(first) && (first = firstWaiter) != null);
            // 这里 while 循环，如果 first 转移不成功，那么选择 first 后面的第一个节点进行转移，依此类推
            // 此处的firstWaiter=first.nextWaiter的哦
        }

        final boolean transferForSignal(Node node) {
            /*
             * 如果设置失败，说明已经被取消，没必要再进入Sync队列了，doSignal中的循环会找到一个node再次执行
             */
            if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
                return false;
            /**
             * 如果设置成功，但之后又被取消了呢？无所谓，虽然会进入到Sync队列，
             * 但在获取锁的时候会调用shouldParkAfterFailedAcquire方法，该方法中会移除此节点
             **/
            /**
             * 如果执行成功，则将node加入到Sync队列中，enq会返回node的前继节点p。
             * 这里的if判断只有在p节点是取消状态或者设置p节点的状态为SIGNAL失败的时候才会执行unpark。
             * 什么时候compareAndSetWaitStatus(p, ws, Node.SIGNAL)会执行失败呢？如果p节点的线程在这时执行了unlock方法，
             * 就会调用unparkSuccessor方法，unparkSuccessor方法可能就将p的状态改为了0，那么执行就会失败。
             **/
            Node p = enq(node);
            int ws = p.waitStatus;
            if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
                LockSupport.unpark(node.thread);
            return true;
        }

        @Override
        public void signalAll() {

        }
    }






    //======================================conditionObject  条件队列end=============================
    /**
     * 返回的是释放的锁之前的state
     **/
    final int fullyRelease(Node node) {
        boolean failed = true;
        try {
            int savedState = getState();
            if (release(savedState)) {
                failed = false;
                return savedState;
            } else {
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (failed)
                node.waitStatus = Node.CANCELLED;
        }
    }

    /**
     * 此方法是独占模式下线程释放共享资源的顶层入口
     * 它会释放指定量的资源，如果彻底释放了（即state=0）,它会唤醒等待队列里的其他线程来获取资源。
     * 这也正是unlock()的语义，当然不仅仅只限于unlock()。
     **/
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            //head不为空，并且head不处于初始化状态（个人理解，如果=0的话，说明没有后继结点了，
            // 因为如果有后继结点一定会对他执行前驱结点waitStatus置位signal状态的操作，即使后面有对其的状态做过其他改变，但是也不可能是0了），
            // 然后执行unparkSuccessor（这个方法会找到队列中的第一个不处于取消状态的结点）
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
    /**
     * 需要实现类自己实现自己的逻辑
     **/
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    //=======================unsafe-start=========================================
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    private static final long stateOffset;

    private static final long headOffset;

    private static final long tailOffset;

    private static final long waitStatusOffset;

    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (MyAbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (MyAbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (MyAbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (MyAbstractQueuedSynchronizer.Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (MyAbstractQueuedSynchronizer.Node.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }

    private final boolean compareAndSetHead(MyAbstractQueuedSynchronizer.Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }
    private final boolean compareAndSetTail(MyAbstractQueuedSynchronizer.Node expect, MyAbstractQueuedSynchronizer.Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }
    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
    }
    private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
//=======================unsafe-end=========================================




}
