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

    @GetMapping("/testLuaInvokeJavaFunc")
    public void testLuaInvokeJavaFunc () {
        luaDemo.loadJavaFuncInLua();
    }

    @GetMapping("/testGetPath")
    public void testGetPath() {
        luaDemo.testGetPath();
    }
}

//class Solution {
//    public int shortestSubarray(int[] nums, int k) {
//        int leftPos = 0, ret = 0, curSum = 0;
//        for (int i = 0; i < nums.length; i++) {
//            curSum += nums[i];
//
//        }
//    }
//}