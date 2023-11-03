package com.example.controller;

import com.example.dto.StudentDTO;
import com.example.pojo.StartAndEndTime;
import com.example.dox.File;
import com.example.dox.Teacher;
import com.example.service.StudentService;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StartAndEndTime startAndEndTime;
    @Autowired
    private StudentService studentService;
    @GetMapping("/info")
    public ResultVo getInfo(@RequestAttribute("uid") String uid) {
        LocalDateTime now = LocalDateTime.now();
        if ( startAndEndTime.getStart() == null || now.isBefore(startAndEndTime.getStart()) || now.isAfter(startAndEndTime.getEnd())) {
            return ResultVo.error(Code.NOT_START,"不在系统开放时间内");
        }
        StudentDTO student = studentService.getStudent(uid);
        if (student!=null && student.getTNumber() != null) {
            return ResultVo.success(Code.SUCCESS,"你已选择，不可改选",Map.of("info",student));
        }
        List<Teacher> info = studentService.getInfo();
        return ResultVo.success(Code.SUCCESS,"未被选满的导师", Map.of("teachers",info));
    }
    @PutMapping("/teacher/{tid}")
    public ResultVo selectAndGroup(@RequestAttribute("uid") String uid, @PathVariable("tid") String tid) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startAndEndTime.getStart()) || now.isAfter(startAndEndTime.getEnd()) ||
                startAndEndTime.getStart() == null) {
            return ResultVo.error(Code.NOT_START,"不在系统开放时间内");
        }
        boolean flag = studentService.selectAndGroup(tid, studentService.getNumber(uid));
        if (!flag) {
            return ResultVo.error(Code.ERROR,"选择导师失败，请稍后再试");
        }
        StudentDTO student = studentService.getStudent(uid);
        return ResultVo.success(Code.SUCCESS,"选择成功",Map.of("student",student));
    }
    @Value("${my.upload}")
    private String uploadDirectory;
    @PostMapping("/upload/{pid}") // 上传文档，生成文档数据
    public ResultVo upload(@RequestAttribute("uid") String uid,
                           @PathVariable("pid") String pid,
                           @RequestParam("file") MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        if (startAndEndTime.getStart() == null || now.isBefore(startAndEndTime.getStart()) || now.isAfter(startAndEndTime.getEnd())) {
            return ResultVo.error(Code.NOT_START,"不在系统开放时间内");
        }
        String pName = file.getOriginalFilename();
        uploadDirectory += pName;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(uploadDirectory);
            fos.write(file.getBytes());
            File file1 = new File();
            file1.setStudentNumber(studentService.getNumber(uid));
            file1.setProcessId(pid);
            file1.setDetail(uploadDirectory);
            studentService.saveFile(file1);
            return ResultVo.success(Code.SUCCESS,"上传成功");
        }catch (Exception e) {
            e.printStackTrace();
            return ResultVo.error(Code.ERROR,"上传失败");
        }finally {
            try {
                fos.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
