package com.jzue.tomcat.lifecycle;

import java.util.EventObject;

/**
 * @Author: junzexue
 * @Date: 2019/3/21 下午6:04
 * @Description:
 **/
public class LifecycleEvent  extends EventObject {

    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.type = type;
        this.data = data;
    }

    private final Object data;


    /**
     * The event type this instance represents.
     */
    private final String type;

}
