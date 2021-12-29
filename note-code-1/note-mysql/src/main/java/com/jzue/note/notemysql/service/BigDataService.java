package com.jzue.note.notemysql.service;

import com.jzue.note.notemysql.entity.BigData;
import com.jzue.note.notemysql.repository.BigDataRepository;
import com.jzue.note.notemysql.util.RandomUtils;
import com.jzue.note.notemysql.util.SnowflakeIdGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: junzexue
 * @Date: 2019/8/19 上午10:20
 * @Description:
 **/
@Service
public class BigDataService {

    @Autowired
    private BigDataRepository bigDataRepository;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private SnowflakeIdGenerator idGenerator;


    /**
     * https://blog.csdn.net/qq_41204714/article/details/85634371
     * 关于批量插入，最好的方法，拼sql，但是需要注意的是String的长度是有限制的，
     * 然后是
     * 合并数据+事务的方法在较小数据量时，性能提高是很明显的，数据量较大时(1千万以上)，性能会急剧下降
     *
     *
     SQL语句是有长度限制，在进行数据合并在同一SQL中务必不能超过SQL长度限制，通过max_allowed_packet配置可以修改，默认是1M，测试时修改为8M。
     事务需要控制大小，事务太大可能会影响执行的效率。MySQL有innodb_log_buffer_size配置项，超过这个值会把innodb的数据刷到磁盘中，这时，效率会有所下降。
     所以比较好的做法是，在数据达到这个这个值前进行事务提交。

     **/

    public void produceData(){

        List<BigData> dataList =new ArrayList();
        for (int i=0;i<200000;i++){
            BigData bigData = new BigData();
            bigData.setId(idGenerator.nextId());
            bigData.setBigDecimal(new BigDecimal(new Random().nextDouble()));
            bigData.setCharType(RandomStringUtils.random(5));
            bigData.setDateTime(RandomUtils.randomDate("2019-01-01","2019-08-15"));
            bigData.setDoubleNum(new Random().nextDouble());
            bigData.setFloatNum(new Random().nextFloat());
            bigData.setIntNum(new Random().nextInt());
            bigData.setVarcharType(RandomStringUtils.random(155));
            bigData.setText(RandomStringUtils.random(500));
//            bigDataRepository.insert(
//                    bigData.getId(),
//                    bigData.getBigDecimal(),
//                    bigData.getCharType(),
//                    bigData.getDateTime(),
//                    bigData.getDoubleNum(),
//                    bigData.getFloatNum(),
//                    bigData.getIntNum(),
//                    bigData.getText(),
//                    bigData.getVarcharType()
//            );
            dataList.add(bigData);
        }
        List<BigData> bigData = bigDataRepository.saveAll(dataList);
        System.out.println(bigData.size());
    }
}
