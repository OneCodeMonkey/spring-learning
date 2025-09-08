package com.liuyang1.impl;

import com.liuyang1.impl.utils.DataDesensitizationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.liuyang1"})
public class ImplApplication {
    private static final Logger log = LoggerFactory.getLogger(ImplApplication.class);

    public static void main(String[] args) {
        System.out.println("hello world");
        SpringApplication.run(ImplApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        Map<String, Object> data = getData();
        // 测试 yaml 运用到数据脱敏上
        if (data.containsKey("txHeader") && data.get("txHeader") instanceof Map) {
            String servNo = ((Map<String, String>) data.get("txHeader")).get("servNo");
            DataDesensitizationUtils.parseData(data.get("txEntity"), servNo, "");
        }

        return args -> log.info("hello!");
    }

    public static Map<String, Object> getData() {
        HashMap<String, Object> phone = new HashMap<>();
        phone.put("phone", "17631007015");

        HashMap<String, Object> phone2 = new HashMap<>();
        phone2.put("phone", "17631007015");

        List<HashMap<String, Object>> list = new ArrayList<>();
        list.add(phone);
        list.add(phone2);

        HashMap<String, Object> txEntity = new HashMap<>();
        txEntity.put("name", "张三疯");
        txEntity.put("idCard", "130428197001180384");
        txEntity.put("list", list);

        HashMap<String, Object> result = new HashMap<>();

        result.put("txEntity", txEntity);

        HashMap<String, Object> txHeader = new HashMap<>();
        txHeader.put("servNo", "Y3801");
        result.put("txHeader", txHeader);

        return result;
    }
}
