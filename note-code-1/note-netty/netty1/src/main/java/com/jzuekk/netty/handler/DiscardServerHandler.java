package com.jzuekk.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;


/**
 * handler 处理服务端Channel
 * @author jzue
 * @date 2019/10/23 下午2:20
 **/
public class DiscardServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(11111);
        System.out.println(msg);
    }


//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, java.lang.Throwable cause) throws Exception {
////        super.exceptionCaught(ctx, cause);
//        cause.printStackTrace();
//        // 遇到异常~关闭连接
//        ctx.close();
//    }
}
