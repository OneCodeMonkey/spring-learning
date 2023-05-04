package com.liuyang1.impl.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/module/my")
@Slf4j
public class MyController {
    @GetMapping("/myAction")
    public String testAction() {
        System.out.println("request API /module/my/myAction!");
        log.info("request API /module/my/myAction!");
        return String.format("hello, {}, your id is {}", "aaa", 111);
    }
}
