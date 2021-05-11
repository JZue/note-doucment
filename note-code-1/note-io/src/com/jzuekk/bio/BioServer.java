package com.jzuekk.bio;

import com.jzuekk.constant.IOConst;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket=new ServerSocket(IOConst.LISTEN_PORT);

        Socket client = serverSocket.accept();



    }
}
