package com.jzue.note.notemutithread.atomic;

import java.util.concurrent.*;

/**
 * @Author: junzexue
 * @Date: 2019/7/25 上午8:12
 * @Description:
 **/
public class _AtomicInteger {
    public static ExecutorService executor= Executors.newCachedThreadPool();
    public static void main(String[] args) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("xxxx");
            }
        });

    }
}
