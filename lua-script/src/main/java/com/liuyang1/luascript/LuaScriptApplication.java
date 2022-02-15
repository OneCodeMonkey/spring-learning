package com.liuyang1.luascript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LuaScriptApplication {
    private static final Logger log = LoggerFactory.getLogger(LuaScriptApplication.class);

    @Bean
    public ApplicationRunner runner() {
        return args -> log.info("started success!");
    }

    public static void main(String[] args) {
        SpringApplication.run(LuaScriptApplication.class, args);
    }

}
