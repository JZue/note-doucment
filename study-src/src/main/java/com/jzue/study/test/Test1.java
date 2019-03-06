package com.jzue.study.test;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @Author: junzexue
 * @Date: 2019/3/4 下午6:25
 * @Description:
 **/
public class Test1 {
    public static void main(String[] args) throws Exception {

        TestEntity testEntity=new TestEntity();
        testEntity.setName("xue");
        String s=JSON.toJSONString(testEntity);
//        if (b!=null){
//            InputStream stream=new ByteArrayInputStream(b);
//            ObjectInputStream outputStream = new ObjectInputStream(stream);
//            String  o=(String) outputStream.readObject();
//            System.out.println(o.toString());
//        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.write(s.getBytes());
        byte [] strData = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(strData);;
        ObjectInputStream ois =  new ObjectInputStream(bais);
        TestEntity obj = (TestEntity) ois.readObject();
        System.out.println(obj);

    }
}
