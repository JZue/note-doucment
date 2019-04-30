package com.jzue.study.io.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: junzexue
 * @Date: 2019/4/29 下午4:44
 * @Description: 这是一个bio的理解初级demo
 *
 *
 * 读：FileInputStream -> InputStreamReader -> BufferedReader -> br.readLine()；
 *
 * 写： FileOutputStream-> OutputStreamWriter -> BufferedWriter -> bw.write(line);
 **/
public class BioServer {
    public static final int port=1111;
    public static void main(String[] args) {
        InputStream inputStream=null;
        OutputStream outputStream=null;
        ServerSocket serverSocket=null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("**********服务器即将启动，等待客户端的连接*************");
            while (true) {
                Socket accept = serverSocket.accept();
                inputStream = accept.getInputStream();
                outputStream = accept.getOutputStream();
                readMessage(inputStream);
//                writeMessage(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream=null;
                outputStream=null;
                serverSocket=null;
                serverSocket.close();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 接收（从流里读）消息
     **/
    static void readMessage(InputStream inputStream) throws  IOException{
        //字符流
        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        String buf=null;
        while ((buf=bufferedReader.readLine())!=null){
            System.out.println(buf);
        }
//        //字节流
//        byte[] bs=new byte[1024];
//        BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
//        int count=0;
//        while ((count=bufferedInputStream.read(bs))!=-1){
//            String s=new String(bs,0,count);
//            System.out.println(s);
//        }
    }
    /**
     * 回（往流里写）消息
     **/
    static void writeMessage(OutputStream outputStream){
        //键盘获取
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try {
            outputStream.write(bufferedReader.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
