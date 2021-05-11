package com.jzue.tomcat.connector.adapter;

import com.jzue.tomcat.net.SocketEvent;
import com.jzue.tomcat.paser.request.Request;
import com.jzue.tomcat.paser.response.Response;
/**
 * @Author: junzexue
 * @Date: 2019/3/22 下午5:10
 * @Description:Adapter连接了Tomcat连接器Connector和容器Container.
 * 它的实现类是CoyoteAdapter主要负责的是对请求进行封装,构造Request和Response对象.
 * 并将请求转发给Container也就是Servlet容器.
 **/
public interface Adapter {

    public void service(Request req, Response res) throws Exception;
    public boolean prepare(Request req, Response res) throws Exception;
    public boolean asyncDispatch(Request req,Response res, SocketEvent status)
            throws Exception;
    public void log(Request req, Response res, long time);
    public void checkRecycled(Request req, Response res);
    public String getDomain();
}
