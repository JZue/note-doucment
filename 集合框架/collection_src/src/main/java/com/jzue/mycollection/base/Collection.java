package com.jzue.mycollection.base;

import java.util.Iterator;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 下午1:40
 * @Description:
 **/
public interface Collection<E> extends Iterable<E>{

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Iterator<E> iterator();

    Object[] toArray();

    <T> T[] toArray(T[] a);

    boolean add(E e);

    boolean remove(Object o);

    boolean containsAll(java.util.Collection<?> c);

    boolean addAll(java.util.Collection<? extends E> c);


    /**
     * 从集合中删除c集合中也有的元素
     **/
    boolean removeAll(java.util.Collection<?> c);

    /**
     * 从集合中删除集合c中不包含的元素
     **/
    boolean retainAll(java.util.Collection<?> c);


    void clear();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
