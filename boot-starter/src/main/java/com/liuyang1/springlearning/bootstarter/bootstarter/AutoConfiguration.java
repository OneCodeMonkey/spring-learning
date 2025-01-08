package com.liuyang1.springlearning.bootstarter.bootstarter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(Properties.class)
public class AutoConfiguration {
    // 注册bean到容器
    @Bean
    public MyService myService(Properties properties) {
        MyService myService1 = new MyService();
        myService1.setName(properties.getName());
        myService1.setVersion(properties.getVersion());

        return myService1;
    }
}
