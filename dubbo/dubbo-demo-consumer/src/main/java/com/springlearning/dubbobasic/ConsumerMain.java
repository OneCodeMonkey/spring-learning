package com.springlearning.dubbobasic;

import com.springlearning.dubbobasic.service.DemoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerMain {
    public static void main(String[] args) {
        DemoService demoService = null;
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/application.xml");
        demoService = context.getBean(DemoService.class);
        // 调用服务
        System.out.println(demoService.sayHello("ABCD123456"));
    }
}
