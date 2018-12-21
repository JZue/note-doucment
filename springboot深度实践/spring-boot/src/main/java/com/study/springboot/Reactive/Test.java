package com.study.springboot.Reactive;


import rx.Observable;

/**
 * @Author: junzexue
 * @Date: 2018/12/11 下午5:44
 * @Description:
 **/
public class Test {
    public static  void hello(Integer... integers){
        Observable<Integer> observable=Observable.from(integers);
    }
}
