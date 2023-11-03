package com.example.pojo;

import lombok.Data;

@Data // 前后端通信令牌
public class MyToken {
    private String id;
    private Integer role;
}
