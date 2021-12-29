package com.jzue.study.socket.tcp.demo1;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: junzexue
 * @Date: 2019/3/25 下午5:09
 * @Description:
 **/
public class Server {

    private final static Integer port=54321;


    public static void main(String[] args) {
        ServerSocket serverSocket=null;
        Socket socket=null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                socket=serverSocket.accept();
                System.out.println("client "+serverSocket.getInetAddress()+"connect success");
                if (socket!=null){
                    Handler handler=new Handler(socket);
                    new Thread(handler).start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

     public static  class  Handler implements  Runnable{
        private Socket socket;
        private Boolean flag=false;

        public Handler(Socket socket){
            this.socket=socket;
        }

        @Override
        public void run() {
            InputStream is=null;
            OutputStream os=null;
            byte[] buffer=new byte[1024];
            try {
                is=socket.getInputStream();
                is.read(buffer);
                System.out.println(new String(buffer));
                os.write("已经收到客户端的请求".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
