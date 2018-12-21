package com.jzue.study;

import com.jzue.study.kafka.provider.KafkaSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import javax.security.auth.login.Configuration;


@SpringBootApplication
public class StudySrcApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context=SpringApplication.run(StudySrcApplication.class, args);
        KafkaSender sender =context.getBean(KafkaSender.class);
        for (int i = 0; i < 100; i++) {
            //调用消息发送类中的消息发送方法
            Thread.sleep(10000);
            sender.send();
        }
    }
}
