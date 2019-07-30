package com.jzue.note.notemutithread.thread;

/**
 * @Author: junzexue
 * @Date: 2019/7/26 上午7:57
 * @Description:
 **/
public class _Join {
    public static void main(String[] args) {
        Thread thread1=new Thread(()->{

            System.out.println("thread1");
        });
        thread1.start();

        System.out.println("main thread");
    }
}
