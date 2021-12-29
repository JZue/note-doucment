package com.jzue.study.test;

import org.springframework.boot.autoconfigure.http.HttpProperties;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @Author: junzexue
 * @Date: 2018/12/21 下午1:32
 * @Description:
 **/
public class Test {



    public static void main(String[] args) throws NullPointerException{
//        try {
//            kkk(null);
//        }catch (NullPointerException e){处理完成之后程序继续往下执行
//            e.printStackTrace();
//        }
        kkk(null);
        System.out.println(1111);
    }

    public static void kkk(String str) throws NullPointerException{
        System.out.println(str.length());
    }




}
