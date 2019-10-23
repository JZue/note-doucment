package com.jzue.note.boot.annotationdrive;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jzue
 * @date 2019/9/26 上午11:15
 **/
@Configuration
@ConditionalOnClass(User.class)
@EnableConfigurationProperties(UserProperties.class)
public class UserAutoConfiguration {

    @Bean public User user(UserProperties demoProperties){
        User demo=new User();
        demo.setName(demoProperties.getName());
        demo.setAge(demoProperties.getAge());
        return demo;
    }
}
