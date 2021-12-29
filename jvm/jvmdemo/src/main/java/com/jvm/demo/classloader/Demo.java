package com.jvm.demo.classloader;

import java.util.Date;

/**
 * @Author: junzexue
 * @Date: 2019/4/11 上午9:29
 * @Description:
 **/
public class Demo {
    public static void main(String[] args) {
        Demo demo = new Demo();
        System.out.println(demo.getClass().getClassLoader().getClass().getName());
    }
}
