package com.jzuekk.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;


/**
 * handler 处理服务端Channel
 * @author jzue
 * @date 2019/10/23 下午2:20
 **/
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, java.lang.Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            System.out.println("111");
        }
        System.out.println(2222);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, java.lang.Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        // 遇到异常~关闭连接
        ctx.close();
    }
}
