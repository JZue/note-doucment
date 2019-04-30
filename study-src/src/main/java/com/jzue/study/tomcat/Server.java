package com.jzue.study.tomcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: junzexue
 * @Date: 2019/3/21 上午10:49
 * @Description:
 **/
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket=null;
        Socket socket=null;
        InputStream is=null;
        OutputStream os=null;
        try {
            serverSocket=new ServerSocket(8090);
            while (true) {

                socket = serverSocket.accept();
                is = socket.getInputStream();
                os = socket.getOutputStream();
                parse(is);
                //response(os);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (socket!=null){
                    socket.close();
                    socket=null;
                }
                if (is!=null){
                    is.close();
                    is=null;
                }
                if (os!=null){
                    os.close();
                    os=null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void response(OutputStream os) throws IOException {
        //todo: 输出流，响应数据
        String resp="Status：OK - 200                                                <– 响应状态码，表示 web 服务器处理的结果。 \n" +
                "Date：Sun, 01 Jun 2008 12:35:47 GMT\n" +
                "Server：Apache/2.0.61 (Unix)\n" +
                "Last-Modified：Sun, 01 Jun 2008 12:35:30 GMT\n" +
                "Accept-Ranges：bytes\n" +
                "Content-Length：18616\n" +
                "Cache-Control：max-age=120\n" +
                "Expires：Sun, 01 Jun 2008 12:37:47 GMT\n" +
                "Content-Type：html/xml\n" +
                "Age：2\n" +
                "X-Cache：HIT from 236-41.D07071951.sina.com.cn                  <– 反向代理服务器使用的 HTTP 头部\n" +
                "Via：1.0 236-41.D07071951.sina.com.cn:80 (squid/2.6.STABLE13)\n" +
                "Connection：close";
        os.write(Byte.parseByte(resp));
    }

    private static void parse(InputStream is) throws IOException{
        // todo: 输入流，解析数据
        byte[] buffer=new byte[1024];
        StringBuffer sb=new StringBuffer();
        while (is.read(buffer)!=0) {
            sb.append(new String(buffer));
            System.out.println(sb);
        };
        // todo  sb就是http协议头，然后做htpp协议头的解析
    }

}

