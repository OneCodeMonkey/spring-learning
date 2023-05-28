package com.liuyang1.liteflowusage.components;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

@Component("action2")
public class ActionTwo extends NodeComponent {
    @Override
    public void process() {
        System.out.println("action Two executed!");
    }
}
