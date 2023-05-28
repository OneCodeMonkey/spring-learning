package com.liuyang1.liteflowusage.components;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

@Component("action1")
public class ActionOne extends NodeComponent {
    @Override
    public void process() {
        System.out.println("action One executed!");
    }
}
