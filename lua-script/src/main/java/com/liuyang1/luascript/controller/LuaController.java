package com.liuyang1.luascript.controller;

import com.liuyang1.luascript.LuaDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/lua")
public class LuaController {
    @Autowired
    LuaDemo luaDemo;

    @GetMapping("/test")
    public void test() {
        luaDemo.execute();
    }
}
