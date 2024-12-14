package com.liuyang1.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.liuyang1"})
public class ImplApplication {
    public static void main(String[] args) {
        System.out.println("hello world");
        SpringApplication.run(ImplApplication.class, args);
    }
}
