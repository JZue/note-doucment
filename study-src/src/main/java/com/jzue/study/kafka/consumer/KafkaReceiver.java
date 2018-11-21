package com.jzue.study.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author: junzexue
 * @Date: 2018/11/15 下午4:46
 * @Description:
 **/
@Slf4j
@Component
public class KafkaReceiver {
    @KafkaListener(topics = {"zhisheng"})
    public void listen(ConsumerRecord<?, ?> record){
        log.info("=============================");
    }
}
