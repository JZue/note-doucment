package com.jzue.study.remotedianose.controller;

import com.jzue.study.kafka.provider.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: junzexue
 * @Date: 2019/1/24 上午9:34
 * @Description:
 **/
@RestController
@Slf4j
public class Test {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;



    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void test(@RequestParam String command){
            log.info("+++++++++++++++++++++  message = {}", command);
            kafkaTemplate.send("5ecd841bda214500bc1c7e38fffd8362__ivc_botai_command_up", command);
    }
}
