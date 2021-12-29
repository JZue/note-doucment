package com.jzue.note.notemysql.util;

import org.hibernate.cache.cfg.internal.AbstractDomainDataCachingConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: junzexue
 * @Date: 2019/8/17 上午9:39
 * @Description: 分布式环境下的唯一id的生成策略
 **/
public class UniqueIdStrategy {





    //==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(0, 0);
        Set<Long> idSet=new HashSet<>();
        for (int i = 0; i < 200000; i++) {
            long id = idWorker.nextId();
            idSet.add(id);
        }
        System.out.println(idSet.size());
    }

}
