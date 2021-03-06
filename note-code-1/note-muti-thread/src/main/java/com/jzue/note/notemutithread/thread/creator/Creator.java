package com.jzue.note.notemutithread.thread.creator;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
/**
 * @Author: junzexue
 * @Date: 2019/8/5 下午4:38
 * @Description:实现一个线程的三种方式,实际上本质，也都是new Thread(),
 * 我觉得网上说的，包括Thread的类上的注释，应该来说，都不算很准确，本质上都是new Thread(),
 * 只是用到的构造方法不一样而已，而且callable和runnable用到的构造方法都是Thread(Runnable runable);
 **/
public class Creator {
    public static void main(String[] args) {
        // extends thread
        Creator1 creator1 = new Creator1();
        creator1.start();

        // implements Runnable
        Creator2 creator2=new Creator2();
        new Thread(creator2).start();

        // implements Callable
        Creator3 creator3=new Creator3();
        FutureTask<Integer> callableFutureTask =new FutureTask<>(creator3);
        new Thread(callableFutureTask).start();
        try {
            if (callableFutureTask.isDone()){
                System.out.println(callableFutureTask.get());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    static class Creator1 extends Thread{
        @Override
        public void run() {
            System.out.println("thread........creator1");
        }
    }
    static class Creator2 implements Runnable{
        @Override
        public void run() {
            System.out.println("thread........creator2");
        }
    }
    static class Creator3 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            int sum=0;
            for (int i=0;i<50;i++){
                sum+=i;
            }
            System.out.println("creator3.......sum:"+sum);
            return sum;
        }
    }
}
