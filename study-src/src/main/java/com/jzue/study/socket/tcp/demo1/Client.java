package com.jzue.study.socket.tcp.demo1;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author: junzexue
 * @Date: 2019/2/21 下午6:14
 * @Description:
 **/
@Slf4j
public class Client {

    private static Integer port=54321;

    public static void main(String[] args)throws IOException {
        Socket socket=new Socket();
            socket.setSoTimeout(5);
            socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(),port),3*000);
            System.out.println("client "+socket.getLocalAddress()+ " connect server:"+socket.getInetAddress()+":"+socket.getPort()+" port success");
            socket.getOutputStream();
            socket.getInputStream();
            try {
                todo(socket);
            }catch (Exception e){
                System.out.println("client send data error");
            }finally {
                socket.close();
            }
    }

    // 发送数据
    private static  void todo(Socket client)throws IOException{
        //系统输入流
        InputStream in=System.in;
        //键盘输入BufferedReader
        BufferedReader keyboardEvent =new BufferedReader(new InputStreamReader(in));
        //客户端输入的字符串
        String clientStr=keyboardEvent.readLine();
        //客户端输出流往Stream 中写数据
        client.getOutputStream().write(clientStr.getBytes());
        //客户端接收服务端的信息
        byte[] buffer=new byte[1024];
        client.getInputStream().read(buffer);
        System.out.println("收到的服务端的返回："+new String(buffer));
    }
}
