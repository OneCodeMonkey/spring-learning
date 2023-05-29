package com.liuyang1.droolsusage;

import com.liuyang1.droolsusage.enums.CustomerType;
import com.liuyang1.droolsusage.request.OrderDiscount;
import com.liuyang1.droolsusage.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class BasicTest {
    @Resource
    private KieContainer kieContainer;

    @Test
    public void testDroolsBasic() {
        OrderDiscount orderDiscount = new OrderDiscount();
        System.out.println("kieContainer: " + (kieContainer == null ? "YES" : "NO"));

        //
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAge(21);
        orderRequest.setAmount(1000);
        orderRequest.setCustomerType(CustomerType.LOYAL);

        calculate(orderRequest, orderDiscount);
        Assertions.assertEquals(orderDiscount.getDiscount(), 5);

        orderRequest.setAge(19);
        calculate(orderRequest, orderDiscount);
        Assertions.assertEquals(orderDiscount.getDiscount(), 15);

        orderRequest.setAge(51);
        calculate(orderRequest, orderDiscount);
        Assertions.assertEquals(orderDiscount.getDiscount(), 15);

        orderRequest.setAge(21);
        orderRequest.setCustomerType(CustomerType.NEW);
        calculate(orderRequest, orderDiscount);
        Assertions.assertEquals(orderDiscount.getDiscount(), 3);

        orderRequest.setCustomerType(CustomerType.DISSATISFIED);
        calculate(orderRequest, orderDiscount);
        Assertions.assertEquals(orderDiscount.getDiscount(), 3);

        orderRequest.setAmount(1001);
        calculate(orderRequest, orderDiscount);
        Assertions.assertEquals(orderDiscount.getDiscount(), 8);

        orderRequest.setAge(19);
        calculate(orderRequest, orderDiscount);
        Assertions.assertEquals(orderDiscount.getDiscount(), 18);
    }

    public void calculate(OrderRequest orderRequest, OrderDiscount orderDiscount) {
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
