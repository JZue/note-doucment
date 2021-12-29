package com.jzue.concurrency.myaqs;

import org.apache.tomcat.util.threads.LimitLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xuejunze
 * @date 2021/9/9 2:56 下午
 **/
public class AQSDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);

        ReentrantLock reentrantLock = new ReentrantLock();
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        CountDownLatch countDownLatch = new CountDownLatch(1);
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);

        LimitLatch limitLatch = new LimitLatch(1);
        // 线程池中的worker
    }
}
