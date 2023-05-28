package com.liuyang1.liteflowusage.components;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

@Component("action3")
public class ActionThree extends NodeComponent {
    @Override
    public void process() {
        System.out.println("action Three executed!");
    }
}
