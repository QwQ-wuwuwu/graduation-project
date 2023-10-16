package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    public static final Integer ROLE_STUDENT = 0;
    public static final Integer ROLE_TEACHER = 1;
    public static final Integer ROLE_ADMIN = 2;
    @Id
    @CreatedBy
    private Long id; // 主键不适合建立索引，mysql建立时会自动建立主键唯一索引
    private String number;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 只许序列化不可以反序列化
    private String password;
    @JsonIgnore
    private Integer role;
    private String description;
    @ReadOnlyProperty // 不能持久化到数据库，使用数据库设置的默认值
    @JsonIgnore
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    @JsonIgnore
    private LocalDateTime updateTime;
}
