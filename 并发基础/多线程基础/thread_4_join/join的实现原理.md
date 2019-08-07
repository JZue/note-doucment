理解：thread1.join方法是阻塞持有对象锁的对象，让对应的线程(thread1)完成以后，被阻塞的线程才可以继续执行

```java
	public static void main(String[] args) throws InterruptedException {
        printOrderQuestion();
    }
    static void printOrderQuestion() throws InterruptedException {
        Thread thread1 = creator(1);
        Thread thread2 = creator(2);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(Thread.currentThread().getName());
    }
    static Thread creator(int index){
        return new Thread(()->{
            System.out.println(index+"......:thread creator");
        });
    }
```

第一个问题：上面的这个代码的执行结果的顺序？

```java
？？？？？
```

这个问题，

* 首先俩线程都start 进入Runnable状态，
* 首先获取时间片的你不能确定是哪一个？
* 可能是Thread1,可能是Thread2,可能是Main，假设是Thread1
* 然后到底是Thread2获取时间片，还是Main获取时间片呢？
* 也不确定~
* 但是可以肯定最后的那个输出肯定是main()
* 总结就是，这样的使用join()跟本没有起到控制thread1和thread2的顺序的作用

```java

 static void printOrderQuestion1() throws InterruptedException {
        Thread thread1 = creator(1);
        Thread thread2 = creator(2);
        thread1.start();
        thread1.join();
        thread2.start();
        thread2.join();
        System.out.println(Thread.currentThread().getName()); 
}
```



正确的操作~应该是start以后就join，才可以达到join想要的效果。



```java
join的实现：

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
```

上面的顺序执行到底是什么原理呢？

* 首先主线程执行t1.start()方法，这个时候是有两个非守护线程的，一个主线程，一个t1线程都处于Runnable状态
* 执行的到t1的join方法
* 这个时候主线程是有对象t1的对象锁的，所以是可以执行t1的wait方法的（此处可以看下上一篇的Thread的线程状态）
* 执行t1的wait方法以后，主线程就相当于放弃了t1的对象锁，故而时间片就只能分给了t1
* t1执行完，这个时候就到重点了，有wait必定得有notify，不然就一直等下去了（join里面的是wait（0）,就是一直等下去），到底是在哪notify的呢？



```c++
// 位置：/hotspot/src/share/vm/runtime/thread.cpp
void JavaThread::exit(bool destroy_vm, ExitType exit_type) {
	........                               
    ensure_join(this);
    ........
}

```

// 上面的exit方法实在是太长了~~~，故而省略了，如果很感兴趣，可以看参考另一篇文章下载hotspot的源码自己翻阅 -----https://jzuekk.com/page/thread_1.html

```c++
static void ensure_join(JavaThread* thread) {
  // We do not need to grap the Threads_lock, since we are operating on ourself.
  Handle threadObj(thread, thread->threadObj());
  assert(threadObj.not_null(), "java thread object must exist");
  ObjectLocker lock(threadObj, thread);
  // Ignore pending exception (ThreadDeath), since we are exiting anyway
  thread->clear_pending_exception();
  // Thread is exiting. So set thread_status field in  java.lang.Thread class to TERMINATED.
  java_lang_Thread::set_thread_status(threadObj(), java_lang_Thread::TERMINATED);
  // Clear the native thread instance - this makes isAlive return false and allows the join()
  // to complete once we've done the notify_all below
  java_lang_Thread::set_thread(threadObj(), NULL);
    
   // 此处就是重点~~上面的t1线程执行完毕以后，就是在此处，唤醒了所有的等待在此的线程
  lock.notify_all(thread);
    
    
  // Ignore pending exception (ThreadDeath), since we are exiting anyway
  thread->clear_pending_exception();
}
```

最后再来总结一下：到底怎么实现的，最先，他先是只有持有锁的才可以进入join方法，这个持有锁的就是main线程，然后主线程被wait（）了，然后t1线程就可以获取时间片了，t1执行完毕以后，在jvm源码底层notify主线程，主线程继续执行，然后到了t2线程，同理

参考资料:   <https://www.zhihu.com/question/44621343>