package com.jzue.study.io.reader_writer;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Author: junzexue
 * @Date: 2019/2/19 下午3:39
 * @Description:
 **/
@Slf4j
public class Example {
    public static void main(String[] args)throws IOException {
        //创建远源
        String path="/Users/jzue/Desktop/shtest.sh";
        String path2="/Users/jzue/Desktop/shtest-1.sh";
        File file=new File(path);
        File file2=new File(path2);
        //选择流
        Reader reader=null;
        Writer writer=null;
        try {
            reader=new FileReader(file);
            writer=new FileWriter(file2);
            char[] flush =new char[10];
            int len=0;
            while (-1!=(len=reader.read(flush))){
                log.info("字符流读取"+new String(flush));
                writer.write(flush);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            reader.close();
            writer.close();
        }

    }
}
