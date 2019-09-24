package com.jzue.note.designpattern.factory.creator;

import com.jzue.note.designpattern.factory.shape.*;

/**
 * @author jzue
 * @date 2019/9/24 上午9:40
 **/
public class ShapeFactory {

    private static ShapeFactory instance=new ShapeFactory();

    private ShapeFactory(){

    }

    public Shape getShape(ShapeEnum shapeEnum){
        switch (shapeEnum){
            case CIRCLE:
                // break 和return  都可以直接跳出switch
                return new Circle();
            case SQUARE:
                return new Square();
            case RECTANGLE:
                return new Rectangle();
            default:
                return ()-> System.out.println("未知形状");
        }
    }
    public static ShapeFactory newInstance() {
        return instance;
    }
}
