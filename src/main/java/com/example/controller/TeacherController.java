package com.example.controller;

import com.example.dto.StudentDTO;
import com.example.dox.Process;
import com.example.dox.ProcessScore;
import com.example.dox.Student;
import com.example.dox.Teacher;
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
    @GetMapping("/students3")
    public ResultVo getSelect() { // 查看所有已选学生
        List<StudentDTO> select = teacherService.getSelect();
        if (select == null) {
            return ResultVo.error(Code.ERROR,"操作失败");
        }
        if (select.size() == 0) {
            return ResultVo.success(Code.SUCCESS,"没有同学已选导师");
        }
        return ResultVo.success(Code.SUCCESS,"操作成功",Map.of("students",select));
    }
    @GetMapping("/process/{pid}/role/{role}")
    public ResultVo listProcess(@PathVariable("pid") String pid, @PathVariable String role,
                                @RequestAttribute("uid") String uid) {
        Teacher teacher = teacherService.getTeacher(uid);
        Integer groupId = teacher.getGroupId(); // 老师所在的评审小组就是学生加入的评审小组
        if (role.equals(Process.CHECK)) { // 如果是审查老师，应该展示这个组的所有同学的过程分数
            List<ProcessScore> processScores = teacherService.getProcessScores(groupId, pid);
            return ResultVo.success(Code.SUCCESS,"",Map.of("processScore",processScores));
        }
        List<ProcessScore> processScores = teacherService.getProcessScores(uid, pid);
        return ResultVo.success(Code.SUCCESS,"",Map.of("processScore",processScores));
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
        }
        return null; // 已经将文件以二进制形式响应了，导致resultvo无法正常转换为json
    }
}
