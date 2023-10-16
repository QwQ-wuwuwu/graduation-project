package com.example.controller;

import com.example.dto.StudentDTO;
import com.example.entity.StartAndEndTime;
import com.example.pojo.File;
import com.example.pojo.Process;
import com.example.pojo.Student;
import com.example.pojo.Teacher;
import com.example.service.StudentService;
import com.example.vo.Code;
import com.example.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResultVo getInfo(@RequestAttribute("uid") String uid) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startAndEndTime.getStart()) || now.isAfter(startAndEndTime.getEnd())) {
            return ResultVo.error(Code.NOT_START,"不在系统开放时间内");
        }
        StudentDTO student = studentService.getStudent(uid);
        if (student.getTNumber() != null) {
            return ResultVo.success(Code.SUCCESS,"你已选择，不可改选",Map.of("info",student));
        }
        List<Teacher> info = studentService.getInfo();
        return ResultVo.success(Code.SUCCESS,"未被选满的导师", Map.of("teachers",info));
    }
    @PutMapping("/teacher/{tid}")
    public ResultVo selectAndGroup(@RequestAttribute("uid") String uid, @PathVariable("tid") String tid) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startAndEndTime.getStart()) || now.isAfter(startAndEndTime.getEnd())) {
            return ResultVo.error(Code.NOT_START,"不在系统开放时间内");
        }
        boolean flag = studentService.selectAndGroup(tid, uid);
        if (!flag) {
            return ResultVo.error(Code.ERROR,"选择导师失败，请稍后再试");
        }
        StudentDTO student = studentService.getStudent(uid);
        return ResultVo.success(Code.SUCCESS,"选择成功",Map.of("student",student));
    }
    @Value("${my.upload}")
    private String uploadDirectory;
    @PostMapping("/upload/{pid}") // 上传文档，生成文档数据
    public ResultVo upload(@RequestPart String fName,
                           @RequestAttribute("uid") String uid,
                           @RequestPart String pid, //可以由前端提供选项
                           MultipartFile file) throws JsonProcessingException { // @RequestPart常用来处理多表数据和上传文档
        try {
            String fileName = file.getOriginalFilename();
            File file1 = new File();
            file1.setStudentId(Long.valueOf(uid));
            file1.setProcessId(Long.valueOf(pid));
            file1.setDetail(Paths.get(fName).resolve(fileName).toString());
            Path p = Paths.get(uploadDirectory).resolve(fName); // 构建文件上传的目标路径
            if (!Files.exists(p)) { // 检查目标目录是否存在，如果不存在则创建目录
                Files.createDirectories(p);
            }
            Path finPath = p.resolve(fileName); // 构建最终的文件路径，包括目标目录和文件名
            file.transferTo(finPath.toFile()); // 将上传的文件保存到目标路径
            studentService.saveFile(file1);
            return ResultVo.success(Code.SUCCESS,"上传成功");
        } catch (IOException e) {
            return ResultVo.error(Code.ERROR,"文件上传错误" + e.getMessage());
        }
    }
}
