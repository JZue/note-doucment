package com.jzue.note.effectivejava.chapter2.item1;

import java.io.Serializable;

/**
 * Item1:考虑用静态工厂方法代替构造器
 *
 * 参考  https://www.cnblogs.com/honoka/p/4858416.html，其中有很多详细例子，理解一下即可
 *
 * - 优点
 *   - 可读性强
 *   - 调用的时候不需要每次都创建一个新对象==>此点针对的是单利模式
 *   - 可以返回原返回类型的任何子类型的对象
 *   - 代码更简洁
 * - 缺点
 *   - 如果类不含public 或者protect的构造方法，将不能被继承
 *   - 与其他普通静态方法没有区别，没有明确的表示一个静态方法用于实例化类，就是说别人并不清楚你这个方法就是用来获取类的实例化对象的，
 *   所以一般一个静态工厂方法需要有详细的注释，遵守标准的命名，如使用getInstance、valueOf、newInstance等方法名；
 *
 * @author jzue
 * @date 2019/9/8 上午10:18
 **/
public class Item1 extends Item1Child {

    private String field1;

    /**
     *
     **/
    public static Item1 getInstance(){
        return new Item1();
    }

    public static Item1Child getChildInstance(){
        return new Item1();
    }


}
