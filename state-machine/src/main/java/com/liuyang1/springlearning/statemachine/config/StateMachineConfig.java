package com.liuyang1.springlearning.statemachine.config;

import com.liuyang1.springlearning.statemachine.enums.Events;
import com.liuyang1.springlearning.statemachine.enums.States;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
@Slf4j
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration().autoStartup(true).listener(listener());
    }

//    @Override
//    public void configure(StateMachineConfigBuilder<States, Events> config) throws Exception {
//        super.configure(config);
//    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates().initial(States.SI).states(EnumSet.allOf(States.class));
    }

//    @Override
//    public void configure(StateMachineModelConfigurer<States, Events> model) throws Exception {
//        super.configure(model);
//    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions.withExternal().source(States.SI).target(States.S1).event(Events.E1)
                .and().withExternal().source(States.S1).target(States.S2).event(Events.E2);
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                log.info("state changed from: {}  to: {}", from != null ? from.getId() : null, to.getId());
                super.stateChanged(from, to);
            }

            @Override
            public void stateEntered(State<States, Events> state) {
                log.info("state: {} entered!", state.getId());
                super.stateEntered(state);
            }

            @Override
            public void stateExited(State<States, Events> state) {
                log.info("state: {} exited!", state.getId());
                super.stateExited(state);
            }

            @Override
            public void stateMachineStarted(StateMachine<States, Events> stateMachine) {
                log.info("state machine started!");
                super.stateMachineStarted(stateMachine);
            }

            @Override
            public void stateMachineStopped(StateMachine<States, Events> stateMachine) {
                log.info("state machine stopped!");
                super.stateMachineStopped(stateMachine);
            }

            @Override
            public void stateMachineError(StateMachine<States, Events> stateMachine, Exception exception) {
                log.info("state machine error: {}!!", exception.getMessage());
                super.stateMachineError(stateMachine, exception);
            }
        };
    }
}
