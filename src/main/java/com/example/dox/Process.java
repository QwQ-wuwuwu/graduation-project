package com.example.dox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "process")
public class Process {
    public static final String CHECK = "qwq"; // 审查人员
    public static final String TEACHER = "QWQ";  // 指导老师
    @Id
    @CreatedBy
    private String id;
    private String studentDetail;
    private String processName;
    private String items;
    @JsonIgnore
    private String teacherId;
    @ReadOnlyProperty
    @JsonIgnore
    private LocalDateTime insertTime;
    @ReadOnlyProperty
    @JsonIgnore
    private LocalDateTime updateTime;
}
