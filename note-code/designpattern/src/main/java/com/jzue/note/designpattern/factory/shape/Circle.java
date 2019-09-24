package com.jzue.note.designpattern.factory.shape;

/**
 * @author jzue
 * @date 2019/9/24 上午9:36
 **/
public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("this is circle");
    }
}
