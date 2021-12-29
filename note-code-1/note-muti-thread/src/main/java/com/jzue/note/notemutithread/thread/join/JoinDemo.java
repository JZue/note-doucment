package com.jzue.note.notemutithread.thread.join;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: junzexue
 * @Date: 2019/7/26 上午7:57
 * @Description: t.join()方法阻塞调用此方法的线程(calling thread)，直到线程t完成，此线程再继续；通常用于在main()主线程内，等待其它线程完成再结束main()主线程。
 **/
public class JoinDemo {

    public static void main(String[] args) throws InterruptedException {
//        printOrderQuestion();
        printOrderQuestion1();
    }

    static void printOrderQuestion1() throws InterruptedException {
        Thread thread1 = creator(1);
        Thread thread2 = creator(2);
        thread1.start();
        thread1.join();
        thread2.start();
        thread2.join();
        System.out.println(Thread.currentThread().getName());
    }
    static void printOrderQuestion() throws InterruptedException {
        Thread thread1 = creator(1);
        Thread thread2 = creator(2);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(Thread.currentThread().getName());
    }
    static Thread creator(int index){
        return new Thread(()->{
            System.out.println(index+"......:thread creator");
        });
    }

}
