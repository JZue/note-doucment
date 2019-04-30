package com.jzue.mycollection.queue;


import com.jzue.mycollection.base.Collection;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 下午2:48
 * @Description:Queue使用时要尽量避免Collection的add()和remove()方法,
 * 而是要使用offer()来加入元素，使用poll()来获取并移出元素。
 * 它们的优点是通过返回值可以判断成功与否，add()和remove()方法在失败的时候会抛出异常。
 * 如果要使用前端而不移出该元素，使用element()或者peek()方法。
 *
 * 详细demo请见：com.jzue.collection_src.queue.LinkedListDemo
 **/
public interface Queue<E> extends Collection<E> {

    @Override
    boolean add(E e);

    boolean offer(E e);

    E remove();

    E poll();

    E element();

    E peek();
}
