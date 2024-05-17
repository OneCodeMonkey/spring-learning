package com.liuyang1.spring_learning.mybatis.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private String username;
    private String intro;
    private String avatar;
}
