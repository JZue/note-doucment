package com.jzue.study.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    public static final int MAX_DATA_LEN = 1024;
    private  Socket socket;

    public ClientHandler(Socket socket) {

            this.socket = socket;

    }

    @Override
    public void run() {
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            byte[] data = new byte[MAX_DATA_LEN];
            int len= -1;
            while (true) {
                try {
                    if (!((len = inputStream.read(data)) != -1)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String message = new String(data, 0, len);
                System.out.println("客户端传来消息: " + message);
                try {
                    socket.getOutputStream().write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
