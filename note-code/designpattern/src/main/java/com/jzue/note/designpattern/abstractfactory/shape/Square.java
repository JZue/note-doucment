package com.jzue.note.designpattern.abstractfactory.shape;

/**
 * @author jzue
 * @date 2019/9/24 上午10:36
 **/
public class Square implements Shape {
    @Override
    public void draw() {
        System.out.println("this is square!");
    }
}
