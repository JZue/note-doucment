package com.jzue.collection_src.queue;

import java.util.*;

/**
 * @Author: junzexue
 * @Date: 2019/2/26 下午1:09
 * @Description:
 * queue:
     * add()        增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
     * remove()   移除并返回队列头部的元素    如果队列为空，则抛出一个NoSuchElementException异常
     * element()  返回队列头部的元素             如果队列为空，则抛出一个NoSuchElementException异常
     * offer()       添加一个元素并返回true       如果队列已满，则返回false
     * poll()         移除并返问队列头部的元素    如果队列为空，则返回null
     * peek()       返回队列头部的元素             如果队列为空，则返回null
 **/
public class LinkedListDemo {




    public static void main(String[] args) {
        LinkedList<String> queue= new LinkedList<>();
        queue.add("xue1");
        queue.add("xue2");
        queue.add("xue3");
        System.out.println("queue:"+queue);
        //从开头开始删的，使用remove()方法删除元素时候，如果队列是空的，会抛出NoSuchElementException异常
        queue.remove();
        System.out.println("queue.remove():"+queue);
        // 使用poll()方法删除队列中的元素,poll()方法和remove()相似，唯一的区别是当队列为空时候poll()返回null
        queue.poll();
        System.out.println("queue.poll():"+queue);
    }
}
