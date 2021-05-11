package com.jzue.tomcat.connector.handler;

import com.jzue.tomcat.connector.adapter.Adapter;

import java.util.concurrent.Executor;

/**
 * @Author junzexue
 * @Description:
 **/
public interface ProtocolHandler {

    public void setAdapter(Adapter adapter);
    public Adapter getAdapter();
    public Executor getExecutor();
    public void init() throws Exception;
    public void start() throws Exception;
    public void pause() throws Exception;
    public void resume() throws Exception;
    public void stop() throws Exception;
    public void destroy() throws Exception;
    public void closeServerSocketGraceful();
    public boolean isAprRequired();
    public boolean isSendfileSupported();

//    public void addSslHostConfig(SSLHostConfig sslHostConfig);
//    public SSLHostConfig[] findSslHostConfigs();
//
//    public void addUpgradeProtocol(UpgradeProtocol upgradeProtocol);
//    public UpgradeProtocol[] findUpgradeProtocols();

}
