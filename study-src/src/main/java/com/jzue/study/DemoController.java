package com.jzue.study;

import com.jzue.study.redis.RedisDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: junzexue
 * @Date: 2019/5/9 下午4:39
 * @Description:
 **/
@RestController
public class DemoController {
    @Autowired
    RedisDemo redisDemo;

    @GetMapping("/demo")
    public void demo(){

        redisDemo.setValue();
        redisDemo.printDemo();
    }
}
