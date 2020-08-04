package com.jzue.concurrency.aqs;

import com.jzue.concurrency.constant.AqsConst;
import org.apache.catalina.Executor;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.concurrent.*;

public class CountDownLatchExample {



    public static void main(String[] args) {
        ThreadFactory threadFactory = new ThreadPoolExecutorFactoryBean();
        ExecutorService executor = new ThreadPoolExecutor(
                AqsConst.CORE_POOL_SIZE,
                AqsConst.MAX_POOL_SIZE,
                AqsConst.KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(),
                threadFactory,
                /*
                 * new ThreadPoolExecutor.AbortPolicy():丢弃任务并抛出RejectedExecutionException异常。
                 * new ThreadPoolExecutor.DiscardPolicy():丢弃任务，但是不抛出异常。
                 * new ThreadPoolExecutor.DiscardOldestPolicy()：丢弃队列最前面的任务，执行后面的任务
                 * new ThreadPoolExecutor.CallerRunsPolicy()：由线程池调用线程处理该任务,（main线程是线程池的调用者，main线程参与执行任务）
                 */
                new ThreadPoolExecutor.CallerRunsPolicy());

    }
}
