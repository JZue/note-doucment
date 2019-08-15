package com.jzue.note.notemutithread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: junzexue
 * @Date: 2019/7/25 上午8:38
 * @Description:
 **/

/**
 *
 **/
public class _NewCachedThreadPool {

    public static final int corePoolSize=2;
    public static final int maxPoolSize=5;
    public static final long keepAliveTime=1000L;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Executors.defaultThreadFactory();


    }

}
