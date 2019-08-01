package com.jzue.tomcat.connector.handler;

import com.jzue.tomcat.connector.adapter.Adapter;

import java.util.concurrent.Executor;

/**
 * @Author: junzexue
 * @Date: 2019/3/22 下午4:43
 * @Description:
 **/
public class AbstractProtocol implements ProtocolHandler {

    /**
     * @Description:适配器提供了rotocolHandler和connector.的联系
     **/
    protected Adapter adapter;
    public void setAdapter(Adapter adapter) {
        this.adapter=adapter;
    }
    public Adapter getAdapter() {
        return adapter;
    }



    public Executor getExecutor() {
        return null;
    }

    public void init() throws Exception {

    }

    public void start() throws Exception {

    }

    public void pause() throws Exception {

    }

    public void resume() throws Exception {

    }

    public void stop() throws Exception {

    }

    public void destroy() throws Exception {

    }

    public void closeServerSocketGraceful() {

    }

    public boolean isAprRequired() {
        return false;
    }

    public boolean isSendfileSupported() {
        return false;
    }
}
