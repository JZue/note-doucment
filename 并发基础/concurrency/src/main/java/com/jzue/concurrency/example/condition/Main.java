package com.jzue.concurrency.example.condition;

import java.util.ArrayList;

/**
 * @Author: junzexue
 * @Date: 2019/4/12 下午4:21
 * @Description:
 **/
public class Main {
    public static void main(String[] args) {
        Bucket<String> objectBucket = new Bucket<>(new ArrayList<>());
        new Thread(new Consumer(objectBucket)).start();
        new Thread(new Producer(objectBucket)).start();
    }
}
