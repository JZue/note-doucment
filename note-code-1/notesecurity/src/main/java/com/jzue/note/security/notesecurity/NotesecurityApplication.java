package com.jzue.note.security.notesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@SpringBootApplication
@RestController
public class NotesecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesecurityApplication.class, args);
    }

    @GetMapping("/demo1")
    public void runnableDemo(){
        new Runnable(){
            @Override
            public void run() {
                System.out.println("runnable");
            }
        };
    }

    @GetMapping("/demo2")
    public Callable<String> callableDemo(){
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("callable"+Thread.currentThread().getName());
                Thread.sleep(5000);
                return "ssss";
            }
        };
        return callable;
    }


}
