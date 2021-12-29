package com.jzue.tomcat.connector.adapter;

import com.jzue.tomcat.connector.adapter.Adapter;
import com.jzue.tomcat.net.SocketEvent;
import com.jzue.tomcat.paser.request.Request;
import com.jzue.tomcat.paser.response.Response;

/**
 * @Author: junzexue
 * @Date: 2019/3/22 下午5:10
 * @Description:
 **/
public class CoyoteAdapter implements Adapter {
    public void service(Request req, Response res) throws Exception {

    }

    public boolean prepare(Request req, Response res) throws Exception {
        return false;
    }

    public boolean asyncDispatch(Request req, Response res, SocketEvent status) throws Exception {
        return false;
    }

    public void log(Request req, Response res, long time) {

    }

    public void checkRecycled(Request req, Response res) {

    }

    public String getDomain() {
        return null;
    }
}
