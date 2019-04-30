package com.jzue.study.io.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: junzexue
 * @Date: 2019/3/25 下午12:00
 * @Description:
 **/
public class FileChannelDemo {


    public static void main(String[] args) {
        try {
            RandomAccessFile accessFile =new RandomAccessFile("/Users/jzue/Desktop/myskilltree.md","rw");
            FileChannel fileChannel=accessFile.getChannel();
            ByteBuffer buffer=ByteBuffer.allocate(2048);
            int bytesRead =fileChannel.read(buffer);
            while (bytesRead !=-1){
                System.out.println("read-->"+bytesRead);
                buffer.flip();
            }


        }catch (IOException exception){

        }

    }
}
