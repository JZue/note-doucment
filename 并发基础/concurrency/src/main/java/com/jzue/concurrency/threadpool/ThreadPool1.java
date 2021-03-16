package com.jzue.concurrency.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool1 {
    public static final int CORE=5;
    public static final int MAX=5;
    public static void main(String[] args) {
        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(CORE,
                MAX,
                60,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadPoolExecutor.AbortPolicy());


        Runnable runnable = ()->{ System.out.println(1);
        };
        Callable<Integer> callable = ()->{
            System.out.println(1);
            return 1;
        };
        threadPoolExecutor.execute(runnable);
        threadPoolExecutor.submit(callable);
    }

}
