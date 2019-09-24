package com.jzue.note.designpattern.factory;

import com.jzue.note.designpattern.factory.creator.ShapeFactory;
import com.jzue.note.designpattern.factory.shape.ShapeEnum;

/**
 * @author jzue
 * @date 2019/9/24 上午9:52
 **/
public class FactoryDemo {
    public static void main(String[] args) {
       ShapeFactory.newInstance().getShape(ShapeEnum.CIRCLE).draw();
       ShapeFactory.newInstance().getShape(ShapeEnum.RECTANGLE).draw();
       ShapeFactory.newInstance().getShape(ShapeEnum.SQUARE).draw();
       ShapeFactory.newInstance().getShape(ShapeEnum.DEFAULT).draw();
    }
}
