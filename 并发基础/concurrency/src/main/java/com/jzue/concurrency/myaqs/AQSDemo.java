package com.jzue.concurrency.myaqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xuejunze
 * @date 2021/9/9 2:56 下午
 **/
public class AQSDemo {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        Semaphore semaphore = new Semaphore(1);
    }
}
