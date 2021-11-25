package com.springlearning.dubbobasic.service;

public class DemoServiceImpl implements DemoService {
    public String sayHello(String msg) {
        System.out.println("msg= " + msg);
        return "SUCCESS";
    }
}