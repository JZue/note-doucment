package com.study.springboot.service;

import org.springframework.stereotype.Service;

/**
 * @Author: junzexue
 * @Date: 2018/12/11 下午12:26
 * @Description:
 **/
@Service(value = "tService")
    /**
     * @Description: 给value 赋值以后，自动装配的实例名就必须是tService；
     * @Date: 下午2:25 2018/12/11
     **/
//@Service
public class TestService {

    public void test(){
        return;
    }
}
