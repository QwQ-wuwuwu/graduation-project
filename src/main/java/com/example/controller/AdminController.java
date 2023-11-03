package com.example.controller;

import com.example.pojo.StartAndEndTime;
import com.example.dox.Process;
import com.example.dox.Student;
import com.example.dox.Teacher;
import com.example.service.AdminService;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @PutMapping("/time")
    public ResultVo time(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime start,
                         @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime end) {
        StartAndEndTime time = adminService.time(start, end);
        System.out.println(time);
        return ResultVo.success(Code.SUCCESS,"更新成功", Map.of("time",time));
    }
    @PutMapping("/password/{number}")
    public ResultVo updatePassword(@PathVariable("number") String number) {
        Integer integer = adminService.updatePassword(number);
        if (integer <= 0) {
            return ResultVo.error(Code.ERROR,"更新失败");
        }
        return ResultVo.success(Code.SUCCESS,"更新成功");
    }
    @PostMapping("/teachers")
    public ResultVo saveTeachers(@RequestBody List<Teacher> teachers) {
        boolean flag = adminService.saveTeachers(teachers);
        if (!flag) {
            return ResultVo.error(Code.ERROR,"添加失败");
        }
        return ResultVo.success(Code.SUCCESS,"添加成功");
    }
    @PostMapping("/students")
    public ResultVo saveStudents(@RequestBody List<Student> students) {
        boolean flag = adminService.saveStudents(students);
        if (!flag) {
            return ResultVo.error(Code.ERROR,"添加失败");
        }
        return ResultVo.success(Code.SUCCESS,"添加成功");
    }
    @PutMapping("/group")
    public ResultVo updateGroup(@RequestBody Student student) {
        Integer integer = adminService.updateGroup(student.getGroupId(), student.getUserNumber());
        if (integer <= 0) {
            return ResultVo.error(Code.ERROR,"更新失败");
        }
        return ResultVo.success(Code.SUCCESS,"更新成功");
    }
    @PostMapping("/process") // 由管理员来决定是哪个过程哪些打分项以及分值比例
    public ResultVo postProcess(@RequestBody Process process) {
        Process postProcess = adminService.postProcess(process);
        return ResultVo.success(Code.SUCCESS,"操作成功",Map.of("process",postProcess));
    }
}
