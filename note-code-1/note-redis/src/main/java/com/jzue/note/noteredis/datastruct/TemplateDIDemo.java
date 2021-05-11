package com.jzue.note.noteredis.datastruct;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author jzue
 * @date 2019/9/10 下午4:40
 **/
@Service
public class TemplateDIDemo {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public void putDemoEntity(DemoEntity demoEntity){
        redisTemplate.opsForValue().set("demo_entity", JSON.toJSONString(demoEntity));
    }
}
