package com.jzue.mycollection.iterator;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 下午1:31
 * @Description:
 **/
public interface Iterator<E> {

    /**
     * next方法不仅要返回当前元素，还要后移游标cursor
     **/
    E next();

    /**
     * next方法不仅要返回当前元素，还要后移游标cursor
     **/
    boolean hasNext();

    /**
     * remove()方法用来删除最近一次已经迭代出的元素
     **/
    void remove();
}
