package com.liuyang1.springlearning.statemachine;

import com.liuyang1.springlearning.statemachine.enums.Events;
import com.liuyang1.springlearning.statemachine.enums.States;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestStateMachine {
    @Autowired
    StateMachine<States, Events> stateMachine;

    @PostConstruct
    public void testMachine() {
        log.info("testMachine, send some events.");
        stateMachine.sendEvent(Events.E1);
        stateMachine.sendEvent(Events.E2);
    }
}
