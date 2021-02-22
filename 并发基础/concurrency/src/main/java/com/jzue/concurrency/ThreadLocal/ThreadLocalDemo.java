package com.jzue.concurrency.ThreadLocal;

/**
 * @author jzue
 * @date 2021/2/22 2:45 下午
 **/
public class ThreadLocalDemo {
    static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    static {
        threadLocal.set("parent_thread_local_value");
    }

    public static void main(String[] args) {
        // 值1
        Thread thread1 =new Thread(()->{
            threadLocal.set("thread1");
            System.out.println("thread1-set:"+threadLocal.get());
            threadLocal.remove();
            System.out.println("thread1-remove:"+threadLocal.get());
        });
        // 值2
        Thread thread2 =new Thread(()->{
            threadLocal.set("thread2");
            System.out.println("thread2-set:"+threadLocal.get());
            threadLocal.remove();
            System.out.println("thread2-remove:"+threadLocal.get());
        });
        //  无set值
        Thread thread3 =new Thread(()->{
//            threadLocal.set("thread2");
            System.out.println("thread3-set:"+threadLocal.get());
            threadLocal.remove();
            System.out.println("thread3-remove:"+threadLocal.get());
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }


}
