package com.jzue.study.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: junzexue
 * @Date: 2019/3/6 下午3:08
 * @Description:
 **/
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;
}
