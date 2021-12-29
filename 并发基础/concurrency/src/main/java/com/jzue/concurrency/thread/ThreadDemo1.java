package com.jzue.concurrency.thread;

/**
 * @Author: junzexue
 * @Date: 2019/8/1 下午4:34
 * @Description:
 **/
public class ThreadDemo1 {
    public static void main(String[] args)  {
        Thread thread = new Thread(()->{
            System.out.println("thread-demo");
        });
        thread.start();
        try {
            synchronized (thread){
                Thread.sleep(10000);
                System.out.println("thread-demo1");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       thread.interrupt();
//        thread.join();


    }
}
