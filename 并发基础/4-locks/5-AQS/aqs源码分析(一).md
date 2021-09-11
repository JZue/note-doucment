同步队列：双向队列

```
双向队列的组成是：AQS类成员变量
// 分别是头尾结点
private transient volatile Node head;
private transient volatile Node tail;
```

等待队列：

```

private transient Node firstWaiter;      
private transient Node lastWaiter;
```

获取锁：

```
public final void acquire(int arg)
public final void acquireInterruptibly(int arg)

final boolean acquireQueued(final Node node, int arg)

public final void acquireShared(int arg)
public final void acquireSharedInterruptibly(int arg)
```

取消获取锁：

```
private void cancelAcquire(Node node)
```

获取的逻辑

```
private void doAcquireInterruptibly(int arg)
private boolean doAcquireNanos(int arg, long nanosTimeout)
private void doAcquireShared(int arg)
private void doAcquireSharedInterruptibly(int arg)
private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
```





casSet方法

```
private final boolean compareAndSetHead(Node update)
private final boolean compareAndSetTail(Node expect, Node update)
private static final boolean compareAndSetWaitStatus(Node node,int expect,int update)
private static final boolean compareAndSetNext(Node node, Node expect,Node update)
```





```
// 将当前线程添加到等待队列尾部，并标记为对应的模式，如果尾结点不为空，
// 则直接在尾结点加然后return，否则通过enq(node)（实质用的是为空的情况）
Node addWaiter(Node mode) 


```



```
// https://blog.csdn.net/anlian523/article/details/106964711/
final boolean apparentlyFirstQueuedIsExclusive()
```

