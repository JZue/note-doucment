package com.jzuekk.nio;

import com.jzuekk.constant.IOConst;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

public class NioServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel nioServer=ServerSocketChannel.open();
        nioServer.bind(new InetSocketAddress(IOConst.LISTEN_PORT));

    }
}
