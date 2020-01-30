package com.jzuekk.net.notehttp.demo1;

import org.springframework.stereotype.Component;

/**
 * @author jzue
 * @date 2019/12/27 下午6:22
 **/
@Component
public class DefaultCallBack<T> extends AbstractCallback<T> {


    @Override
    public void completed(AsyncResponse var1) {

    }

    @Override
    public void failed(Exception e) {

    }

    @Override
    public void cancelled() {

    }
}
