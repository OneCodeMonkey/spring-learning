package com.liuyang1.springlearning.statemachine;

import com.google.gson.Gson;
import com.liuyang1.springlearning.statemachine.enums.Events;
import com.liuyang1.springlearning.statemachine.enums.States;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
@Slf4j
public class StateMachineApplication {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    public static void main(String[] args) {
        SpringApplication.run(StateMachineApplication.class, args);
    }

    @Bean
    ApplicationRunner runner() {
        return args -> {
            log.info("args: {}", new Gson().toJson(args));

            stateMachine.sendEvent(Events.E1);
            stateMachine.sendEvent(Events.E2);
        };
    }

    @Bean
    String runner2() {
        log.info("runner2 args: {}", "aa");

        stateMachine.sendEvent(Events.E1);
//        stateMachine.sendEvent(Events.E2);

        return " ";
    }

}
