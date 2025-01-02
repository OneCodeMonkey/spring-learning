package com.liuyang1.impl.facade;

import com.liuyang1.impl.annotation.AutoLog;
import com.liuyang1.impl.service.Ip2RegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/module/my")
@Slf4j
public class MyController {
    @Autowired
    private Ip2RegionService ip2RegionService;

    @AutoLog
    @GetMapping("/myAction")
    public String testAction() throws Exception {
        System.out.println("request API /module/my/myAction!");
        log.info("request API /module/my/myAction!");

        // 此处验证，即使抛出异常，切面的 after() 方法仍能执行到
//        throw new Exception("some error");

        return String.format("hello, {}, your id is {}", "aaa", 111);
    }

    @AutoLog
    @GetMapping("/testIp")
    public String testAction(@RequestParam(name = "ip") String ip) {
        return ip2RegionService.searchIp(ip);
    }
}
