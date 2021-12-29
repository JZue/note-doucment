package com.jzue.note.noteredis.bitmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: junzexue
 * @Date: 2019/8/8 上午8:35
 * @Description:
 **/
@Component
public class BitMap {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void bitMapDemo(){
        boolean b=redisTemplate.opsForValue().setBit("bitkey",2,true);
        if (b){
            boolean r=redisTemplate.opsForValue().getBit("bitkey",2);
            System.out.println("操作结果...................."+r);
        }

    }
}
