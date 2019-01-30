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



    public static void main(String[] args) throws  Exception {

        byte[] srtbyte = {18, 12, 12, 13};
        StringBuilder stringBuilder = new StringBuilder("");
        for (Byte b:srtbyte){
            String str=Integer.toString(b);
            stringBuilder.append(str);
        }
        System.out.println(stringBuilder.toString());

    }
}
