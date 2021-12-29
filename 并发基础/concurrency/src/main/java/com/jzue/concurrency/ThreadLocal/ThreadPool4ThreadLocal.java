package com.jzue.concurrency.ThreadLocal;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池下的threadlocal
 * @author jzue
 * @date 2021/2/22 3:46 下午
 **/
public class ThreadPool4ThreadLocal {
    static ExecutorService executor = new ThreadPoolExecutor(2,
            2,60, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(1024),new ThreadPoolExecutor.CallerRunsPolicy());

    static InheritableThreadLocal<String>  threadLocal = new InheritableThreadLocal<>();
    // 初始化
    static {
        threadLocal.set("parent_thread_local_value");
    }
    public static void main(String[] args) {
        executor.execute(()->{
            threadLocal.set("thread1");
            System.out.println("thread1-set:"+threadLocal.get());
        });
        executor.execute(()->{
            threadLocal.set("thread2");
            System.out.println("thread2-set:"+threadLocal.get());
        });
        executor.execute(()->{
            System.out.println("thread3-set:"+threadLocal.get());
        });
    }
}
