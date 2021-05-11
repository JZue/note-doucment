package com.jzue.note.notemysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NoteMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteMysqlApplication.class, args);
    }

}
