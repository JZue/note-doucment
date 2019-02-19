package com.jzue.study.io.stream;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Author: junzexue
 * @Date: 2019/2/19 上午11:21
 * @Description:
 **/
@Slf4j
public class DirCopyExample {




    public static void main(String[] args)  {
        String srcPath="/Users/jzue/Desktop/springboot-seckill";
        String targetPath="/Users/jzue/Desktop/copyTest";
        File src=new File(srcPath);
        File target =new File(targetPath);
        if (!target.exists()) {
            target.mkdir();
        }
        if (src.isDirectory()) {
            target = new File(targetPath, src.getName());
        }
        copyDir(src, target);

    }

    /**
     * @Description:copy directionary
     * @Date: 下午3:26 2019/2/19
     **/
    public static void copyDir(File src,File target){
        if (src.isFile()){
            FileRWExample.copyFile(src,target);
        }else {
            target.mkdirs();
            for (File childFile:src.listFiles()){
                File targetChildFile=new File(target.getPath(),childFile.getName());
                copyDir(childFile,targetChildFile);
            }
        }
    }
}
