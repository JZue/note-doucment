![thread_status](/Users/jzue/Desktop/blog_file/thread_status.jpg)





**二.初始状态**

　　实现Runnable接口和继承Thread可以得到一个线程类，new一个实例出来，线程就进入了初始状态。

**三.就绪状态**

　　就绪状态只是说你资格运行，调度程序没有挑选到你，你就永远是就绪状态。

* 调用线程的start()方法，此线程进入就绪状态。

* 当前线程sleep()方法结束，其他线程join()结束，等待用户输入完毕，某个线程拿到对象锁，这些线程也将进入就绪状态。

* 当前线程时间片用完了，调用当前线程的yield()方法，当前线程进入就绪状态。

* 锁池里的线程拿到对象锁后，进入就绪状态。


**四.运行中状态**

　　线程调度程序从可运行池中选择一个线程作为当前线程时线程所处的状态。这也是线程进入运行状态的唯一一种方式。

**五.阻塞状态**

　　阻塞状态是线程阻塞在进入synchronized关键字修饰的方法或代码块(获取锁)时的状态。

**六.终止状态**

　　当线程的run()方法完成时，或者主线程的main()方法完成时，我们就认为它终止了。这个线程对象也许是活的，但是，它已经不是一个单独执行的线程。线程一旦终止了，就不能复生。

​	在一个终止的线程上调用start()方法，会抛出java.lang.IllegalThreadStateException异常。

**七.等待队列(本是Object里的方法，但影响了线程)**

​	调用obj的wait(), notify()方法前，必须获得obj锁，也就是必须写在synchronized(obj) 代码段内。

　　与等待队列相关的步骤和图

![thread_status_2](/Users/jzue/Desktop/blog_file/thread_status_2.jpg)

​	1.线程1获取对象A的锁，正在使用对象A。

　　2.线程1调用对象A的wait()方法。

　　3.线程1释放对象A的锁，并马上进入等待队列。

　　4.锁池里面的对象争抢对象A的锁。

　　5.线程5获得对象A的锁，进入synchronized块，使用对象A。

　　6.线程5调用对象A的notifyAll()方法，唤醒所有线程，所有线程进入同步队列。若线程5调用对象A的notify()方法，则唤醒一个线程，不知道会唤醒谁，被唤醒的那个线程进入同步队列。

　　7.notifyAll()方法所在synchronized结束，线程5释放对象A的锁。

　　8.同步队列的线程争抢对象锁，但线程1什么时候能抢到就不知道了。

　　同步队列状态

　　当前线程想调用对象A的同步方法时，发现对象A的锁被别的线程占有，此时当前线程进入同步队列。简言之，同步队列里面放的都是想争夺对象锁的线程。

　　当一个线程1被另外一个线程2唤醒时，1线程进入同步队列，去争夺对象锁。

　　同步队列是在同步的环境下才有的概念，一个对象对应一个同步队列。

**八.几个方法的比较**

　　Thread.sleep(long millis)，一定是当前线程调用此方法，当前线程进入TIME_WAITING状态，但不释放对象锁，millis后线程自动苏醒进入就绪状态。作用：给其它线程执行机会的最佳方式。

　　Thread.yield()，一定是当前线程调用此方法，当前线程放弃获取的cpu时间片，由运行状态变会就绪状态，让OS再次选择线程。作用：让相同优先级的线程轮流执行，但并不保证一定会轮流执行。实际中无法保证yield()达到让步目的，因为让步的线程还有可能被线程调度程序再次选中。Thread.yield()不会导致阻塞。

　　t.join()/t.join(long millis)，当前线程里调用其它线程t的join方法，当前线程进入TIME_WAITING/TIME_WAITING状态，当前线程不释放已经持有的对象锁。线程t执行完毕或者millis时间到，当前线程进入就绪状态。

　　obj.wait()，当前线程调用对象的wait()方法，当前线程释放对象锁，进入等待队列。依靠notify()/notifyAll()唤醒或者wait(long timeout)timeout时间到自动唤醒。

　　obj.notify()唤醒在此对象监视器上等待的单个线程，选择是任意性的。notifyAll()唤醒在此对象监视器上等待的所有线程。

#### 线程thread的状态 BLOCKED,WAITING,TIMED_WAITING

##### BLOCKED

这种状态是指一个阻塞线程在等待monitor锁

如：等待进入同步区域

##### WAITING（无限期等待）

一个线程在等待另一个线程执行一个动作时在这个状态

如：

* Object#wait() 而且不加超时参数

* Thread#join() 而且不加超时参数

* LockSupport#park()

##### TIMED_WAITING（限期等待）

一个线程在一个特定的等待时间内等待另一个线程完成一个动作会在这个状态

如：



* Thread#sleep()

* Object#wait() 并加了超时参数

* Thread#join() 并加了超时参数

* LockSupport#parkNanos()

* LockSupport#parkUntil()

本文原文链接：<http://www.zhiliaotang.net/jishujiaoliu/Java/249.html>