package com.jzue.concurrency.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ThreadPool1 {
    ExecutorService threadPoolExecutor = Executors.newCachedThreadPool();
    Semaphore s = new Semaphore();

}
