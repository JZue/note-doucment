package com.jzue.study.netty.netty2_1;

/**
 * @Author: junzexue
 * @Date: 2018/11/13 下午3:21
 * @Description:
 **/
public class TestMain {
    public static void main(String[] args) {
        Server server=new Server(8080);
        server.start();
    }
}
