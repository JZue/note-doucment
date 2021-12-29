package com.jzue.study.kafka.beans;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: junzexue
 * @Date: 2018/11/15 下午4:16
 * @Description:
 **/
@Data
@ToString
public class Message {
    /**
     * @Description: id
     * @Date: 下午4:17 2018/11/15
     **/
    private Long id;

    /**
     * @Description: 消息体
     * @Date: 下午4:17 2018/11/15
     **/
    private String msg;

    /**
     * @Description: 时间戳
     * @Date: 下午4:17 2018/11/15
     **/
    private Date sendTime;

}