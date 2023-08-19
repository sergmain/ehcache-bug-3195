package com.example.sb35718;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleApplication {
    public static void main(String[] args) {
//        TomcatURLStreamHandlerFactory.register();
        SpringApplication.run(SimpleApplication.class, args);
    }
}
