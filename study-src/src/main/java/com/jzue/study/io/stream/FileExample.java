package com.jzue.study.io.stream;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Author: junzexue
 * @Date: 2019/2/19 上午11:21
 * @Description:
 **/
@Slf4j
public class FileExample {




    public static void main(String[] args) throws Exception {
        String path="/Users/jzue/Desktop/shtest.sh";
        File file=new File(path);
        File file1=new File("/Users/jzue/Desktop/");
        if (file.exists()==true){
            log.info("存在");
        }
        log.info("绝对路径"+file.getAbsolutePath());
        log.info("上级目录"+file.getParent());
        log.info("文件名"+file.getName());
        log.info("绝对路径"+file.getPath());
        System.out.println("是否可读"+file.canRead()+"   文件是否可写"+file.canWrite()+"    是否可执行"+file.canExecute());
        System.out.println(file.createNewFile());
        //临时文件
        File file2=File.createTempFile("test",".temp",file1);
        Thread.sleep(2000);
        file2.delete();
        InputStream in=new FileInputStream(file);

    }

}
