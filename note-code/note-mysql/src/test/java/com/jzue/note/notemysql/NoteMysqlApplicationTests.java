package com.jzue.note.notemysql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteMysqlApplicationTests {

    @Autowired
    private Controller controller;

    @Test
    public void contextLoads() {
    }

    @Test
    public void exmaple1(){
        controller.pessimisticLock();
    }
}
