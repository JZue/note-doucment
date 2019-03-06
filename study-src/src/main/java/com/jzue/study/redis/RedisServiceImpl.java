package com.jzue.study.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisServiceImpl {
    @Autowired
    private RedisTemplate<String, ?> redisTemplate;


    public boolean set(final String key, final String value){
        boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.set(serializer.serialize(key), serializer.serialize(value));
            return true;
        });
        return result;
    }

    public String get(final String key){
        String result = redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value =  connection.get(serializer.serialize(key));
            return serializer.deserialize(value);
        });
        return result;
    }

    public boolean expire(final String key, long expire){
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public <T> boolean setList(String key, List<T> list) {
        String value = JSON.toJSONString(list);
        return set(key,value);
    }

    public <T> List<T> getList(String key,Class<T> clz) {
        String json = get(key);
        List<T> list;
        if(json!=null){
            list = JSON.parseArray(json, clz);
        }else {
            list = new ArrayList<>();
        }
        return list;
    }


    public long lpush(final String key, Object obj) {
        final String value = JSON.toJSONString(obj);
        long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
            return count;
        });
        return result;
    }

    public long rpush(final String key, Object obj) {
        final String value = JSON.toJSONString(obj);
        long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
            return count;
        });
        return result;
    }

    public String lpop(final String key) {
        String result = redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] res =  connection.lPop(serializer.serialize(key));
            return serializer.deserialize(res);
        });
        return result;
    }

    public void delete() {
        Set<String> keys = redisTemplate.keys("optdict" + "*");
        redisTemplate.delete(keys);
    }

}