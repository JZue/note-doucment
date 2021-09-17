package com.jzue.concurrency.myaqs;

import java.util.concurrent.Semaphore;

/**
 * @author xuejunze
 * @date 2021/9/16 3:07 下午
 **/
public class SemaphoreDemo {
    public  static  Semaphore semaphore = new Semaphore(10);
    public static void main(String[] args) {

        for (int i = 0 ; i<100;i++){
            Thread thread = new Thread(() -> {
                stopCar();
                // 停1s之后就离开
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                release();
            });
            thread.start();

            System.out.println();
        }
    }
    public static void stopCar()  {
        try {
            semaphore.acquire(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void release(){
        semaphore.release(1);
    }
}
