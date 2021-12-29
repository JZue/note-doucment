package com.jzue.mycollection.iterator;

import java.util.Iterator;

/**
 * @Author: junzexue
 * @Date: 2019/4/8 下午2:01
 * @Description:
 **/
public interface Iterable<T> {

    Iterator<T> iterator();
}
