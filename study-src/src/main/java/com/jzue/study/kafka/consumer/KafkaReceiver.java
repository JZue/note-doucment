package com.jzue.study.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;

/**
 * @Author: junzexue
 * @Date: 2018/11/15 下午4:46
 * @Description:
 **/
@Slf4j
@Component
public class KafkaReceiver {
    @KafkaListener(topics = {"5ecd841bda214500bc1c7e38fffd8362__ivc_botai_telemetrydata_up"})
    public void listen(ConsumerRecord<?, ?> record){
        log.info("=============================");
    }
}
