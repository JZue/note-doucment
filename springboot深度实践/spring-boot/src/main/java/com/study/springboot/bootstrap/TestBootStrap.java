package com.study.springboot.bootstrap;

import com.study.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: junzexue
 * @Date: 2018/12/11 下午2:12
 * @Description:
 **/
@ComponentScan(basePackages = "com.study.springboot.service")
public class TestBootStrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context=new SpringApplicationBuilder(TestBootStrap.class)
                .web(WebApplicationType.NONE)
                .run(args);
        TestService tService=context.getBean("tService", TestService.class);
        System.out.println(tService);
        //关闭上下文
        context.close();
    }
}
