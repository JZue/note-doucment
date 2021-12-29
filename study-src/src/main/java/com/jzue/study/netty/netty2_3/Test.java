package com.jzue.study.netty.netty2_3;

/**
 * @Author: junzexue
 * @Date: 2018/12/4 上午9:52
 * @Description:
 **/
public class Test {
    public static void main(String[] args) throws Exception {
        UIServer server=new UIServer();
        server.bind(8888);
    }
}
