package com.jzue.note.notemutithread.thread.status;

/**
 * @Author: junzexue
 * @Date: 2019/8/5 下午6:20
 * @Description: 这基本就已经完整的体现了其状态的转换，好好理解， e.printStackTrace()开发阶段调试用，生产环境最好别用
 **/
public class Status {
    public static Status status=new Status();
    public static void main(String[] args) {
    Thread thread1 = new Thread(() -> {
        status.synDemo(Thread.currentThread());
    },"thread1");
    Thread thread2 = new Thread(() -> {
        status.synDemo(thread1);
    },"thread2");
    printSyn(thread1,thread2,Thread.currentThread());
        thread1.start();
        thread2.start();
    printSyn(thread1,thread2,Thread.currentThread());
    printSyn(thread1,thread2,Thread.currentThread());
}
    public  synchronized void synDemo(Thread t) {
        System.out.println(Thread.currentThread().getName() + "：11111获取到锁");
        System.out.println(t.getName() + "...........11111" + t.getState());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getName() + "...........11111" + t.getState());
    }
    public static void printSyn(Thread... ts){
        for (Thread t:ts){
            System.out.println(t.getName()+"..........."+t.getState());
        }
    }
}
