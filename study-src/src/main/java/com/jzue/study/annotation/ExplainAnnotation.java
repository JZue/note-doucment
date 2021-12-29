package com.jzue.study.annotation;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author: junzexue
 * @Date: 2018/11/20 下午4:53
 * @Description:  解析注解
 **/
@Slf4j
public class ExplainAnnotation {
    public static void main(String[] args) {
        try {
            Class c = Class.forName("com.jzue.study.annotation.Test");
            Method[] ms=c.getDeclaredMethods();// 能获取到 private  protected public default
          //  Method[] ms=c.getMethods();// 只能访问到具有权限的方法
            for (Method m:ms){
                boolean isExist =m.isAnnotationPresent(TestAnnotation.class);
                if (isExist){
                    //  todo
                    TestAnnotation testAnnotation=(TestAnnotation)m.getAnnotation(TestAnnotation.class);
                    System.out.println(testAnnotation.desc());
                }
            }
        } catch (Exception ex) {
        }
    }
}
