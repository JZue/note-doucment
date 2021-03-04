package com.jzue.demo.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author jzue
 * @date 2021/3/3 11:18 上午
 **/
public class BioServer {
    private ServerSocket serverSocket;


    public static final int MAX_DATA_LEN = 1024;
    public BioServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() throws IOException {
        final Socket socket = serverSocket.accept();
        new Thread(()-> {
            try {
                InputStream inputStream = socket.getInputStream();
                int len;
                byte[] data = new byte[MAX_DATA_LEN];
                while ((len = inputStream.read(data))!=-1){
                    String message = new String(data,0,len);
                    System.out.println("客户端消息："+message);
                    // 又会写给客户端
                    socket.getOutputStream().write(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
