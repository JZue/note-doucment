package com.jzue.note.notemutithread.thread;

/**
 * @Author: junzexue
 * @Date: 2019/7/25 上午8:59
 * @Description:
 **/
public class ThreadExecutionQuestion {
    public static void main(String[] args) throws InterruptedException {

    }

    void answer1() throws InterruptedException {
        Thread thread1 = new Thread(()->{
            System.out.println("thread1");
        },"thread1");
        Thread thread2 = new Thread(()->{
            System.out.println("thread2");
        },"thread2");
        Thread thread3 = new Thread(()->{
            System.out.println("thread3");
        },"thread3");

        //启动线程
        thread1.start();
        //控制线程必须执行完成
        thread1.join();
        thread2.start();
        thread2.join();
        thread3.start();
        thread3.join();
    }
    void answer2() throws InterruptedException {
        Thread thread1 = new Thread();
        Thread thread2 = new Thread();
        Thread thread3 = new Thread();

        thread1.start();
        while (thread1.isAlive()){

        }
        thread2.start();
        while (thread1.isAlive()){

        }
        thread3.start();
        while (thread1.isAlive()){

        }
    }
    void answer3() throws InterruptedException {
        Thread thread1 = new Thread();
        Thread thread2 = new Thread();
        Thread thread3 = new Thread();

       threadStartAndWait(thread1);
       threadStartAndWait(thread2);
       threadStartAndWait(thread3);
    }

    //https://blog.csdn.net/u010983881/article/details/80257703
    void threadStartAndWait(Thread thread){
        if (Thread.State.NEW.equals(thread.getState())){
            thread.start();
        }
        while (thread.isAlive()){
            synchronized (thread){
                try {
                    thread.wait();
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
