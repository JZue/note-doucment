package com.jzue.tomcat.Log;

/**
 * @Author: junzexue
 * @Date: 2019/3/22 上午9:09
 * @Description:
 **/
public class Logger  {

    String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    //私有的构造方法
    private Logger(String className) {
        this.className=className;
    }

    //私有的类名String 为入参的初始化方法
    private static Logger getInstance(String name){
        return new Logger(name);
    }
    // 初始化入口函数
    public static Logger getInstance(Class<?> tClass){
      return getInstance(tClass.getName());
    }

    public void info(String s){
        System.out.println(className+"info--------->"+s);
    }
    public void debug(String s){
        System.out.println(className+"debug--------->"+s);
    }
    public void warning(String s){
        System.out.println(className+"warning--------->"+s);
    }
    public void error(String s){
        System.out.println(className+"error--------->"+s);
    }
}
