package com.jzue.study.io.bio;

import java.io.*;
import java.net.Socket;

/**
 * @Author: junzexue
 * @Date: 2019/4/29 下午5:53
 * @Description:
 **/
public class BioClient {

    public static void main(String[] args)  {
//        InputStream inputStream=null;
        OutputStream outputStream=null;
        BufferedWriter bufferedWriter=null;
        BufferedReader consoleIn=null;
        try {
            Socket socket=new Socket("localhost",BioServer.port);
            outputStream=socket.getOutputStream();
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
            consoleIn=new BufferedReader(new InputStreamReader(System.in));
            bufferedWriter.write(consoleIn.readLine());
        } catch (IOException e) {
            System.out.println("客户端发送数据异常");
        }finally {
            /**
             * bufferedWriter如果不close会出现写入流为空
             **/
            try {
                bufferedWriter.close();
                consoleIn.close();
                outputStream.close();
//                inputStream.close();
            } catch (IOException e) {
                System.out.println("流关闭异常");
            }

        }
    }
}
