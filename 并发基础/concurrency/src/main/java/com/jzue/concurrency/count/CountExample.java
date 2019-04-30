package com.jzue.concurrency.count;

import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.concurrent.*;

/**
 * @Author: junzexue
 * @Date: 2019/1/25 上午11:45
 * @Description:
 **/
public class CountExample {
    private static  int clientTotal=5000;
    private static  int threadTotal=200;//线程池线程最大连接数，也就是最大的可同时执行的线程数
    private static  int count =0;

    public static void main(String[] args) {
        ThreadFactory threadFactory=new ThreadPoolExecutorFactoryBean();
        ExecutorService pool=new ThreadPoolExecutor(5,threadTotal,0L,
                TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>(1024),threadFactory,new ThreadPoolExecutor.AbortPolicy());
        final Semaphore semaphore=new Semaphore(threadTotal);
        for (int index =0;index<clientTotal;index++){
            pool.execute(()->{
                try {
                    semaphore.acquire();
                    count++;
                    semaphore.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
        System.out.println(count);
        pool.shutdown();
    }
}
