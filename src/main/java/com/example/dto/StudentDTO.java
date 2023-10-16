package com.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "student_dto") // 查询结果映射类
public class StudentDTO {
    @Id
    @JsonIgnore
    private Long id;
    private String sName;
    private String sNumber;
    private Integer groupId;
    private String tName;
    private String tNumber;
}
