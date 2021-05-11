package com.jzue.note.notemysql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Author: junzexue
 * @Date: 2019/6/26 上午9:09
 * @Description:
 **/
@Component
public class ExecutorConfig {
    private static int CORE_POOL_SIZE = 3;
    private static int MAX_POOL_SIZE = 10;
    // 当线程空闲时间达到keepAliveTime，该线程会退出，直到线程数量等于corePoolSize。如果allowCoreThreadTimeout设置为true，则所有线程均会退出直到线程数量为0。
    private static Long keepAliveTime=60L;


    @Bean
    public ThreadPoolExecutor taskExecutor() {
        BlockingQueue queue=new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                keepAliveTime,
                TimeUnit.SECONDS,
                queue,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
                );
        //是否允许核心线程空闲退出，默认值为false。
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
}
