package com.example.entity;

import lombok.Data;

@Data // 前后端通信令牌
public class MyToken {
    private Long id;
    private String role;
}
