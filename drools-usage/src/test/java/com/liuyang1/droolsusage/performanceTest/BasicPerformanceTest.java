package com.liuyang1.droolsusage.performanceTest;

import com.liuyang1.droolsusage.BasicTest;
import com.liuyang1.droolsusage.DroolsUsageApplication;
import com.liuyang1.droolsusage.enums.CustomerType;
import com.liuyang1.droolsusage.request.OrderDiscount;
import com.liuyang1.droolsusage.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@SpringBootTest(classes = {DroolsUsageApplication.class, BasicTest.class})
@Service
@Slf4j
public class BasicPerformanceTest {
    @Resource
    private KieContainer kieContainer;

    @Test
    public void basicPerformanceTest() {
        OrderDiscount orderDiscount = new OrderDiscount();
        int execTimes = 10000000;
        long totalCost = 0, times = 0, maxTime = 0, minTime = Integer.MAX_VALUE;
        double avgTime = 0;

        for (int i = 0; i < execTimes; i++) {
            long startTime = System.currentTimeMillis();
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setAge(21);
            orderRequest.setAmount(1000);
            orderRequest.setCustomerType(CustomerType.LOYAL);
            execRules(orderRequest, orderDiscount);
            long singleCost = System.currentTimeMillis() - startTime;
            maxTime = Math.max(maxTime, singleCost);
            minTime = Math.min(minTime, singleCost);
            totalCost += singleCost;
            times++;
        }

        avgTime = totalCost * 1.0 / times;

        System.out.printf("times: %d, avg: %f, max: %d, min: %d, total: %d", times, avgTime, maxTime, minTime, totalCost);
    }

    public void execRules(OrderRequest orderRequest, OrderDiscount orderDiscount) {
        // reset
        orderDiscount.setDiscount(0);

        // 开启 KieSession
        KieSession kieSession = kieContainer.newKieSession();
        // set global variable
        kieSession.setGlobal("orderDiscount", orderDiscount);
        // insert object
        kieSession.insert(orderRequest);
        // 触发规则
        kieSession.fireAllRules();
        // end
        kieSession.dispose();
    }
}
