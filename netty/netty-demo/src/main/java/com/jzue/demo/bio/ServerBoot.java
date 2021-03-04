package com.jzue.demo.bio;

import java.io.IOException;

/**
 * @author jzue
 * @date 2021/3/3 11:51 上午
 **/
public class ServerBoot {
    private final static int PORT=8000;
    public static void main(String[] args) throws IOException {
        BioServer bioServer = new BioServer(PORT);
        bioServer.start();
    }
}
