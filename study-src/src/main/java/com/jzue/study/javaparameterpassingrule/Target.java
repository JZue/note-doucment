package com.jzue.study.javaparameterpassingrule;

import lombok.AllArgsConstructor;


/**
 * @Author: junzexue
 * @Date: 2018/11/21 下午4:45
 * @Description:
 **/
@AllArgsConstructor
public class Target {
    public StringBuilder testStringBuilder(Parameter parameter){
        return parameter.getParamStrngBuilder();
    }
    public int testInt(Parameter parameter) {
        return   parameter.getParamInt();
    }
    public String  testString(Parameter parameter){
        return parameter.getParamString();
    }

    public static void main(String[] args) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("StringBuilder");
        String str="String";

        //第一次给三个类型参数赋值
        Parameter parameter=new Parameter();
        parameter.setParamStrngBuilder(stringBuilder);
        parameter.setParamInt(1111);
        parameter.setParamString(str);

        Target target=new Target();
        StringBuilder targetStringBuilderPrint=target.testStringBuilder(parameter);
        int targetIntPrint =target.testInt(parameter);
        String targetStringPrint=target.testString(parameter);
        // 第一次打印三个参数
        System.out.println(targetIntPrint);
        System.out.println(targetStringPrint);
        System.out.println(targetStringBuilderPrint);


        //第二次给参数赋值
        stringBuilder.append("--------Stringbuilder2222");
        parameter.setParamStrngBuilder(stringBuilder);
        parameter.setParamInt(2222);
        parameter.setParamString("--String2");
        //第二次打印
        System.out.println(targetIntPrint);
        System.out.println(targetStringBuilderPrint);



        /*
        * 原因分析：
        *           int 两次打印的是一样的结果：值传递
        *           String 两次打印的是一样的结果： 传递的是地址的副本，但是由于String是不可变的，故而第二次的时候实际上是新创建了一个字符串，
        *                                       故而没影响到String字符串，所以两次一样
        *           StringBuilder两次结果不一样：StringBuilder 是可变字符串，当apend方法执行不会重新创建对象，
        *                   而是改变了地址指向的内存区域的值，故而证明对象作为参数的时是传递的地址的副本
        *
        * */
    }
}
