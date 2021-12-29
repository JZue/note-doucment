package com.jzue.study.netty.netty2_3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @Author: junzexue
 * @Date: 2018/12/4 上午9:29
 * @Description:
 **/
@Slf4j
public class UIServer {
    //避免使用默认线程数参数
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private	EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

    public void bind(int port) throws Exception {
        log.info("服务端已启动，正在监听用户的请求......");
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            ChannelFuture f = b.bind(new InetSocketAddress(port))
                    .sync();
            f.channel().closeFuture().sync();
        }catch(Exception e){
            log.error("", e);
            throw e;
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            ChannelPipeline pipeline = arg0.pipeline();

        }
    }
}
