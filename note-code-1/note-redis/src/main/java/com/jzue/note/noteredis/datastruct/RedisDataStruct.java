package com.jzue.note.noteredis.datastruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: junzexue
 * @Date: 2019/8/7 下午10:48
 * @Description:
 **/
@Component
public class RedisDataStruct {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     *
     * redis 的字符串的内部编码，三种
     *      int:8字节长整形，字符串值是整形时，这个值用long整形表示
     *      embstr: 临界值(3.2前<=39,     3.2之后<=44)
     *          embstr与raw都使用redisObject和sds保存数据，区别在于，embstr的使用只分配一次内存空间（因此redisObject和sds是连续的），而raw需要分配两次内存空间（分别为redisObject和sds分配空间）。
     *          因此与raw相比，embstr的好处在于创建时少分配一次空间，删除时少释放一次空间，以及对象的所有数据连在一起，寻找方便。而embstr的坏处也很明显，如果字符串的长度增加需要重新分配内存时，
     *          整个redisObject和sds都需要重新分配空间，因此redis中的embstr实现为只读。
     *      raw:大于39个字节的字符串
     *
     *
     * Redis 的字符串并不是'\0'结尾的，而是如下的数据结构，二进制安全
     * https://www.cnblogs.com/ysocean/p/9080942.html
     * struct sdshdr{
     *      //记录buf数组中已使用字节的数量
     *      //等于 SDS 保存字符串的长度
     *      int len;
     *      //记录 buf 数组中未使用字节的数量
     *      int free;
     *      //字节数组，用于保存字符串
     *      char buf[];
     * }
     *
     **/
    public void redisString(){
        redisTemplate.opsForValue().set("key",1);
        System.out.println("redis_string...................."+redisTemplate.opsForValue().get("key"));
    }


    /**
     * hset key field value
     * hmset key [field value]
     * hgetall key
     * hmget key [field...]
     * hget key field
     * 默认临界值：默认64个字节
     * <=64 :ziplist
     * >64:hashtable
     **/
    public void redis_hash(){
        redisTemplate.opsForHash().put("tablename","uname","jzuekk");
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("uname");
        List<Object> tablename = redisTemplate.opsForHash().multiGet("tablename", objects);
        System.out.println("redis_hash:......................"+tablename);
    }

    /**
     * lpush -头部插入
     **/
    public void redis_list(){
        redisTemplate.opsForList().leftPush("listDemo",1);
        redisTemplate.opsForList().leftPush("listDemo",2);
        redisTemplate.opsForList().leftPush("listDemo",3);
        redisTemplate.opsForList().leftPush("listDemo",4);
        redisTemplate.opsForList().rightPush("listDemo",5);
        Object o = redisTemplate.opsForList().leftPop("listDemo");
        Object listDemo = redisTemplate.opsForList().index("listDemo", 2);
        System.out.println("key+lpop search........"+o.toString());
        System.out.println("key+index search.........."+listDemo.toString());
        // start end 如果超过范围就返回所有的值
        List<Object> liestDemo = redisTemplate.opsForList().range("listDemo",0,-1);
        redisTemplate.opsForList().trim("listDemo",0,10);
        List<Object> liestDemo1 = redisTemplate.opsForList().range("listDemo",0,-1);
        System.out.println(liestDemo.toString());
        System.out.println(liestDemo1.toString());
    }

}
