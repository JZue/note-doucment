package com.jzue.tomcat.pipeline.valve;

import com.jzue.tomcat.exception.ServletException;
import com.jzue.tomcat.paser.request.Request;
import com.jzue.tomcat.paser.response.Response;

import java.io.IOException;

/**
 * @Author: junzexue
 * @Date: 2019/3/22 下午6:04
 * @Description:
 **/
public interface Valve {
    /**
     * @Description:返回pipeline包含的下一个Valve
     **/
    public Valve getNext();

    /**
     * @Description:设置pipline包含的下一个Valve
     **/
    public void setNext(Valve valve);

    /**
     * @Description:执行周期性任务，例如reloading......
     * 这个方法将会在类加载上下文的时候被调用，
     **/
    public void backgroundProcess();

    public void invoke(Request request, Response response)
            throws IOException, ServletException;
}
