package com.jzue.study.test;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: junzexue
 * @Date: 2019/3/4 下午7:19
 * @Description:
 **/
@Data
public class TestEntity implements Serializable {
    private static final  long  serialVersionUID = 1L;
    private String name;
}
