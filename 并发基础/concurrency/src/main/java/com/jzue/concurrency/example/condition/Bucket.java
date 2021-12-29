package com.jzue.concurrency.example.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: junzexue
 * @Date: 2019/4/12 下午3:49
 * @Description:
 **/
public class Bucket<T> {
    public static ReentrantLock lock = new ReentrantLock();
    private List<T> tList;

    public Bucket(List<T> tList) {
        this.tList = tList;
    }

    public void add(T t) throws InterruptedException {
        Thread.sleep(100);
        this.tList.add(t);
        System.out.println(Thread.currentThread().getId()+"add====>"+t);
        return;
    }
    public void remove() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(Thread.currentThread().getId()+"remove====>"+this.tList.get(0));
        this.tList.remove(0);
    }
    public int size(){
        return this.tList.size();
    }
}
