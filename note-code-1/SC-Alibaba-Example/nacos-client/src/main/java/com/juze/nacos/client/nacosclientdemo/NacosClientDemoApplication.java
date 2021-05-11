package com.juze.nacos.client.nacosclientdemo;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NacosClientDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosClientDemoApplication.class, args);
	}

}
