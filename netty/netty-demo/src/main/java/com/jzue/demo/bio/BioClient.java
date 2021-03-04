package com.jzue.demo.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author jzue
 * @date 2021/3/3 11:18 上午
 **/
public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8000);
        new Thread(()->{
            try {
                InputStream inputStream = socket.getInputStream();
           

                    String message = "hello world";
                    Scanner scanner = new Scanner(inputStream);
                boolean b = scanner.hasNext();
                System.out.println("客户端发送数据: " + message);
         
                socket.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
