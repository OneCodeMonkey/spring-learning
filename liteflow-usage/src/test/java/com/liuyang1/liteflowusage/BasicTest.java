package com.liuyang1.liteflowusage;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.flow.entity.CmpStep;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@Slf4j
public class BasicTest {
    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testSequenceExecution() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getExecuteStepQueue().size() > 1);

        List<String> executionSequence = new LinkedList<>();
        for (CmpStep step : response.getExecuteStepQueue()) {
            log.info("step: {},{},{},{},{}", step.getNodeName(), step.getNodeId(), step.getStepType(), step.getTag(), step.getTimeSpent());
            executionSequence.add(step.getNodeId());
        }
        Assertions.assertEquals(executionSequence.size(), 3);
        Assertions.assertArrayEquals(executionSequence.toArray(), new String[]{"action1", "action2", "action3"});
    }
}
