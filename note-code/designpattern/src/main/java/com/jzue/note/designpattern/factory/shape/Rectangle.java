package com.jzue.note.designpattern.factory.shape;

/**
 * @author jzue
 * @date 2019/9/24 上午9:39
 **/
public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("this is rectangle");
    }
}
