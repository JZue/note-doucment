package com.jzue.study;

import com.jzue.study.kafka.provider.KafkaSender;
import com.jzue.study.socket.udp.Provider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import javax.security.auth.login.Configuration;


@SpringBootApplication
public class StudySrcApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StudySrcApplication.class, args);
    }
}
