![](https://p1.meituan.net/travelcube/82077ccf14127a87b77cefd1ccf562d3253591.png)

以ReentrantLock为例理解AQS

```
1. 抢锁
		公平锁：
		
		修改并发标志位（state）
		
2. 入队

3. 释放锁

4. 出队

5. 阻塞
6. 唤醒
```





```
ReentrantLock
acquire
tryAcquire



acquireInterruptibly
tryAcquire

acquireShared
tryAcquireShared


acquireSharedInterruptibly
tryAcquireShared
```











**条件队列：入队前已经持有锁，在队列中释放锁，离开队列时没有锁 转移到同步队列
同步队列：入队前没有锁，在队列中争锁，离开队列时候获取到锁**



AQS:Public 方法：

```
 public final void acquire(int arg) 
 public final void acquireInterruptibly(int arg)
 public final boolean tryAcquireNanos(int arg, long nanosTimeout)
 public final boolean release(int arg)
 public final void acquireShared(int arg) 
 public final void acquireSharedInterruptibly(int arg)
 public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
 public final boolean releaseShared(int arg)
 public final boolean hasQueuedThreads()
 public final boolean hasContended()
 public final Thread getFirstQueuedThread()
 public final boolean isQueued(Thread thread)
 public final boolean hasQueuedPredecessors()
 public final int getQueueLength()
 public final Collection<Thread> getQueuedThreads()
 public final Collection<Thread> getExclusiveQueuedThreads()
 public final Collection<Thread> getSharedQueuedThreads()
 public final boolean owns(ConditionObject condition)
 public final boolean hasWaiters(ConditionObject condition)
 public final int getWaitQueueLength(ConditionObject condition)
 public final Collection<Thread> getWaitingThreads(ConditionObject condition)
 public final Collection<Thread> getWaitingThreads(ConditionObject condition)
```

ConditionObject:Public方法

```
public final void signal()
public final void signalAll()
public final void awaitUninterruptibly()
public final void await() throws InterruptedException
public final long awaitNanos(long nanosTimeout)
public final boolean awaitUntil(Date deadline)
public final boolean await(long time, TimeUnit unit)
```