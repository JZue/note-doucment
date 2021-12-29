package com.jzue.study.annotation;

import java.lang.annotation.*;

/**
 * @Author: junzexue
 * @Date: 2018/11/20 下午4:30
 * @Description:  定义注解
 **/
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})   //方法注解，元注解
@Documented                                                 //生成javadoc
@Inherited                                                  //可继承
@Retention(RetentionPolicy.RUNTIME)                         //运行时
public @interface TestAnnotation {
    String desc();
    int age() default 18;
}
