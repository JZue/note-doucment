package com.jzue.study.duolu;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.Selector;
import java.util.Map;

/**
 * @author jzue
 * @date 2021/3/8 2:38 下午
 **/
public class Reactor {
//
//    interface ChannelHandler{
//        void channelReadable(Channel channel);
//        void channelWritable(Channel channel);
//    }
//    class Channel{
//        Socket socket;
//        Event event;//读，写或者连接
//    }
//    enum Event {
//        READABLE,
//        WRITABLE,
//        ACCEPTABLE;
//    }
//
//
//
//    //IO线程主循环:
//    class IoThread extends Thread{
//        Map<Channel,ChannelHandler> handlerMap;//所有channel的对应事件处理器
//        @Override
//        public void run(){
//            Channel channel;
//            while(channel= Selector.select()){//选择就绪的事件和对应的连接
//                if(channel.event==Event.ACCEPTABLE){
//                    registerNewChannelHandler(channel);//如果是新连接，则注册一个新的读写处理器
//                }
//                if(channel.event==Event.WRITABLE){
//                    getChannelHandler(channel).channelWritable(channel);//如果可以写，则执行写事件
//                }
//                if(channel.event==Event.READABLE){
//                    getChannelHandler(channel).channelReadable(channel);//如果可以读，则执行读事件
//                }
//            }
//        }
//    }

}
