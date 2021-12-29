package com.jzue.mycollection.list.impl;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 下午2:33
 * @Description:
 **/

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable{
    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
