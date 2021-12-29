package com.jzue.study.redis;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: junzexue
 * @Date: 2019/5/9 下午4:35
 * @Description:
 **/
@Service
public class RedisDemo {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 如果你存进去的是一个user对象，那么你反序列化的时候也是一个user对象，如果你存进去的时候是user的json，那么你反序列化的时候就需要用json反序列化工具
     **/
    public void setValue(){
        User user = new User();
        user.setPword("xxxx");
        user.setUname("aaaaa");
        redisTemplate.opsForValue().set("demo", user);
    }
    User getValue(){
        return (User)redisTemplate.opsForValue().get("demo");
    }

    public void printDemo(){
        System.out.println(getValue().toString());
    }
}
