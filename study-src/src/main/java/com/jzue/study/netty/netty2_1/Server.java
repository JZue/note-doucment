package com.jzue.study.netty.netty2_1;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: junzexue
 * @Date: 2018/11/13 上午9:37
 * @Description:
 **/
@Slf4j
public class Server {
      private ServerSocket serverSocket;

      /*
      新建一个server
       */
      public Server( int port){
            try {
                  this.serverSocket=new ServerSocket();
                  log.info("服务端启动成功，端口：{}",port);
            }catch (IOException e) {
                  e.printStackTrace();
            }
      }
      //开辟一个新线程，避免server线程阻塞主线程
      public void  start(){
            new Thread(new Runnable() {
                  @Override
                  public void run() {
                        listen();
                  }
            });
      }
      private void listen(){
            while (true){
                  try {
                        //监听端口
                        Socket client =serverSocket.accept();
                  }catch (IOException e){
                        e.printStackTrace();
                  }
            }
      }

}
