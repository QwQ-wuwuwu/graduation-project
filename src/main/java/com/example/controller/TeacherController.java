package com.example.controller;

import com.example.dto.StudentDTO;
import com.example.dox.Process;
import com.example.dox.ProcessScore;
import com.example.dox.Student;
import com.example.dox.Teacher;
import com.example.repository.TeacherRepository;
import com.example.service.StudentService;
import com.example.service.TeacherService;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @GetMapping("/students1") // 查看已选自己的学生
    public ResultVo getStudents(@RequestAttribute("uid") String uid) {
        String teacherId = teacherService.getTeacherId(uid);
        List<Student> selectStudents = teacherService.getSelectStudents(teacherId);
        if (selectStudents == null) {
            return ResultVo.error(Code.ERROR,"操作失败");
        }
        if (selectStudents.size() == 0) {
            return ResultVo.error(Code.SUCCESS,"该导师目前没有学生选择");
        }
        return ResultVo.success(Code.SUCCESS,"操作成功", Map.of("students",selectStudents));
    }
    @GetMapping("/students2") // 查看所有未选学生情况
    public ResultVo getUnSelect() {
        List<Student> unSelect = teacherService.getUnSelect();
        if (unSelect == null) {
            return ResultVo.error(Code.ERROR,"操作失败");
        }
        if (unSelect.size() == 0) {
            return ResultVo.success(Code.SUCCESS,"没有同学未选导师");
        }
        return ResultVo.success(Code.SUCCESS,"操作成功",Map.of("students",unSelect));
    }
    @GetMapping("/group")
    public ResultVo listGroupStudent(@RequestAttribute("uid") String uid) {
        return ResultVo.builder()
                .code(Code.SUCCESS)
                .message("")
                .data(Map.of("students",teacherService.getStudentByGroup(uid)))
                .build();
    }
    @GetMapping("/process/{pid}")
    public ResultVo getProcessById(@PathVariable("pid") String pid) {
        return ResultVo.builder()
                .code(Code.SUCCESS)
                .message("操作成功")
                .data(Map.of("process", teacherService.getProcessById(pid)))
                .build();
    }
    @GetMapping("/name")
    public ResultVo getTeacherName(@RequestAttribute("uid") String uid) {
        return ResultVo.builder()
                .code(Code.SUCCESS)
                .message("")
                .data(Map.of("teacherName",teacherService.getTeacherName(uid))).build();
    }
    @PutMapping("/processscore/role/{role}") // 打分
    public ResultVo updateProcessScore(@RequestBody ProcessScore processScore,
                                       @RequestAttribute("uid") String uid) {
        String studentId = String.valueOf(processScore.getStudentId());
        StudentDTO student = new StudentService().getStudent(studentId);
        Teacher teacher = teacherService.getTeacher(uid);
        if (student.getGroupId() != teacher.getGroupId()) {
            return ResultVo.error(Code.ERROR,"不可操作该学生");
        }
        Integer integer = teacherService.updateProcessScore(processScore);
        if (integer <= 0) {
            return ResultVo.error(Code.ERROR,"操作失败");
        }
        return ResultVo.success(Code.SUCCESS,"打分成功");
    }
    @GetMapping("/download/{studentNumber}")
    public ResultVo downloadFile(@RequestParam("pid") String pid, @PathVariable("studentNumber") String number,
                                 HttpServletResponse response) {
        String path = teacherService.getFileDetail(number,pid);
        File file = new File(path);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=file.txt");
            try (OutputStream outputStream = response.getOutputStream()) {
                byte[] bytes = new byte[1024];
                int length;
                while ((length = inputStream.read(bytes)) > 0) {
                    outputStream.write(bytes,0,length);
                }
                outputStream.flush();
            }
        }catch (IOException e) {
            e.printStackTrace();
            return ResultVo.error(Code.ERROR,"下载失败");
        }
        return null; // 已经将文件以二进制形式响应了，导致resultvo无法正常转换为json
    }
    @GetMapping("/process")
    public ResultVo getProcess() {
        List<Process> processes = teacherService.getProcesses();
        return ResultVo.success(Code.SUCCESS,"",Map.of("processes",processes));
    }
}
