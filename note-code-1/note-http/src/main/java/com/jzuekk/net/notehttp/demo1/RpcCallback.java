package com.jzuekk.net.notehttp.demo1;

/**
 * @author jzue
 * @date 2019/12/27 下午6:27
 **/
public interface RpcCallback<T> {
    void completed(AsyncResponse<T> var1);

    void failed(Exception var1);

    void cancelled();
}
