package com.jzue.study.io.stream;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @Author: junzexue
 * @Date: 2019/2/19 上午11:21
 * @Description:
 **/
@Slf4j
public class FileRWExample {




    public static void main(String[] args)  {
        String path="/Users/jzue/Desktop/shtest.sh";
        File file=new File(path);
        File file1=new File("/Users/jzue/Desktop/");
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try {
            inputStream=new FileInputStream(file);
            byte[] flush=new byte[10];
            int len=0;
            File file2=File.createTempFile("test",".temp",file1);
             outputStream=new FileOutputStream(file2,true);//需要往文件里加就必须是true，覆盖就不用
            while (-1!=(len=inputStream.read(flush))){//循环读取
                log.info(new String(flush,0,len, Charset.forName("UTF-8")));
                outputStream.write(flush);
            }
            Thread.sleep(2000);
            file2.delete();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }


    }

    /**
     * @Description:copy文件夹方法
     * @Date: 下午3:26 2019/2/19
     **/
    public static void copyFile(File source,File target){
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try {
            inputStream=new FileInputStream(source);
            byte[] flush=new byte[1024*1024];
            int len=0;
            outputStream=new FileOutputStream(target,true);//需要往文件里加就必须是true，覆盖就不用
            while (-1!=(len=inputStream.read(flush))){//循环读取
                log.info(new String(flush,0,len, Charset.forName("UTF-8")));
                outputStream.write(flush);
            }
            //Thread.sleep(2000);
            //file2.delete();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            log.info("copy成功");
        }

    }

}
