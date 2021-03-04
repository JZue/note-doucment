package com.jzue.study.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("服务端启动成功，端口:" + port);
        } catch (IOException exception) {
            System.out.println("服务端启动失败");
        }
    }


    public void start() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                new Thread(new ClientHandler(client)).start();
            } catch (IOException e) {
                System.out.println("服务端异常");
            }
        }
    }
}
