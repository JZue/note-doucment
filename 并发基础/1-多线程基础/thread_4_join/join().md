thread1.join()方法是阻塞持有对象锁的对象，让对应的线程(thread1)完成以后，被阻塞的线程才可以继续执行。
join相当于正在执行线程的中断作用，仅作用于正在执行的线程

按照上面thread的作用的理解，join是可以保证线程顺序的.

```java
package com.jzue.demo.thread.multithread.state;

/**
 *
 * thread1.join方法是阻塞持有对象锁的对象，让对应的线程(thread1)完成以后，被阻塞的线程才可以继续执行
 *
 *
 * 两个线程相继start() 进入Runnable状态，
 * 不确定哪一个线程获取cpu时间片？
 * 可能是Thread1,可能是Thread2,可能是Main，假设是Thread1
 * 然后到底是Thread2获取时间片，还是Main获取时间片呢？
 * 也不确定~
 * 但是可以肯定最后的那个输出肯定是main()
 * 总结就是，这样的使用join()跟本没有起到控制thread1和thread2的顺序的作用
 * @author jzue
 * @date 2020/7/24 10:23 上午
 **/
public class ThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        printOrderQuestion();
    }
    static void printOrderQuestion()  {
        Thread thread1 = creator(1);
        Thread thread2 = creator(2);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            thread1.interrupt();
        }
        try {
            thread2.join();
        } catch (InterruptedException e) {
            thread2.interrupt();
        }
        System.out.println(Thread.currentThread().getName());
    }

    static void printOrderQuestion1()  {
        Thread thread1 = creator(1);
        Thread thread2 = creator(2);
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            thread1.interrupt();
        }
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            thread2.interrupt();
        }
        System.out.println(Thread.currentThread().getName());
    }

    static Thread creator(int index){
        return new Thread(()->{
            System.out.println(index+"......:thread creator");
        });
    }
}
```


那么在上文的这段代码中，先分析printOrderQuestion这段代码：
```java 
 static void printOrderQuestion()  {
        Thread thread1 = creator(1);
        Thread thread2 = creator(2);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            thread1.interrupt();
        }
        try {
            thread2.join();
        } catch (InterruptedException e) {
            thread2.interrupt();
        }
        System.out.println(Thread.currentThread().getName());
    }
```
这段代码的执行结果会是什么样的呢？

答：两种情况：
1. 线程1->线程2->main线程
2. 线程2->线程1->main线程

为什么线程1线程2不是顺序执行的呢？

答：两种情况的原理：

1. 最初，main线程持有锁，然后执行 thread1.start();thread2.start()，在执行t1.join（）之前分三种情况：

    1. 此时main线程只有锁，t1.join(),main线程被阻塞，t1,t2挣抢锁，谁抢到谁执行
    2. 此时t1持有锁，t1执行，然后不论是主线程还是t2抢到锁，都是一样的结果
    3. 此时t2持有锁，t2执行，然后不论是主线程还是t1抢到锁，都是一样的结果
    
    **关键点在于，main线程只要持有锁就有


按照这个理解，如果去掉thread2.join()，应该是可能出现t1=>main=>t2这样的情况的，大家可以去试试


```java
static void printOrderQuestion1()  {
        Thread thread1 = creator(1);
        Thread thread2 = creator(2);
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            thread1.interrupt();
        }
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            thread2.interrupt();
        }
        System.out.println(Thread.currentThread().getName());
    }
```
那么按照上面的理解，再来分析这段代码：

1. main线程启动，t1.start;
2. 此时if mian线程持有锁 t1.join(),mian线程被阻塞，只能执行t1；如果t1持有锁，t1执行完毕，只能执行t1，然后t1.join(),由于t1已经执行完毕，mian继续执行 t2.start,过程就和t1一样了~



https://blog.csdn.net/u013425438/article/details/80205693
如果t1.join()放在t2.start()之后的话，仍然会是交替执行，然而并不是没有效果，这点困扰了我很久”，这个在Java编程思想并发那一章有详细的解释。首先你的t1.join是在main主线程上调用的，所以只会让main主线程暂时挂起，不会影响到t2线程。这里只要记住，哪个线程挂起，取决于在哪个线程上调用x.join方法，而不是影响到所有的线程，希望这么说你可以理解







java 是没有办法销毁一个线程的，销毁线程是由c++去做的，当线程执行完以后就会退出销毁


* 怎么控制线程的执行顺序呢？

  * join 去控制

  * ```java
       		Thread thread1 = new Thread();
            Thread thread2 = new Thread();
            Thread thread3 = new Thread();
            
            //启动线程
            thread1.start();
            //控制线程必须执行完成
            thread1.join();
            thread2.start();
            thread2.join();
            thread3.start();
            thread3.join();
     ```
    ```
  
  * 
  
  * 可以通过同步执行，isAlive去阻塞自旋
  
    ```
         thread1.start();
            while (thread1.isAlive()){
                
            }
            thread2.start();
            while (thread1.isAlive()){
        
            }
            thread3.start();
            while (thread1.isAlive()){
        
            }
    ```
  
    ```
