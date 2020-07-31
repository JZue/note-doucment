yield()方法是让当前线程获取到cpu时间片的线程让出cpu，使正在运行中的线程变成就绪状态，并重新和就绪状态（Runnable）的线程竞争cpu的调度权。竞争cpu的时候，它可能立即就获取到，也有可能被其他线程获取到。

```
package com.jzue.demo.thread.multithread.state;

/**
 * @author jzue
 * @date 2020/7/24 9:01 上午
 **/
public class ThreadYield {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 1; i <= 50; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
                if (i%10  == 0) {
                    System.out.println(Thread.currentThread().getName() + ":" +"让出cpu");
                    Thread.yield();
                }
            }
        }, "thread1");

        Thread thread2 = new Thread(() -> {
            for (int i = 1; i <= 50; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
                if (i %10 == 0) {
                    System.out.println(Thread.currentThread().getName() + ":" +"让出cpu");
                    Thread.yield();
                }
            }
        }, "thread2");

        thread1.start();
        thread2.start();

    }
}
```

这段代码的含义相当于，每当执行10个之后当前持有cpu的线程就让出cpu,然后同时和就绪态的线程进行竞争