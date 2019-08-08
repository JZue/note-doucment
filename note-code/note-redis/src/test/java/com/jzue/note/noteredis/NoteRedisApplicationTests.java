package com.jzue.note.noteredis;

import com.jzue.note.noteredis.bitmap.BitMap;
import com.jzue.note.noteredis.datastruct.RedisDataStruct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteRedisApplicationTests {


    @Autowired
    private RedisDataStruct redisDataStruct;

    @Autowired
    private BitMap bitMap;

    @Test
    public void redisString() {
        redisDataStruct.redisString();
    }
    @Test
    public void redisHash(){
        redisDataStruct.redis_hash();
    }

    @Test
    public void redisBitMap(){
        bitMap.bitMapDemo();
    }

}
