package com.example.service;

import com.example.dox.Process;
import com.example.dto.StudentDTO;
import com.example.dox.ProcessScore;
import com.example.dox.Student;
import com.example.dox.Teacher;
import com.example.repository.ProcessScoreRepository;
import com.example.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;
    public String getTeacherId(String uid) {
        return teacherRepository.getTeacherId(uid);
    }
    public List<Student> getSelectStudents(String id) {
        List<Student> students = teacherRepository.students1(id);
        return students;
    }
    public List<Student> getUnSelect() {
        List<Student> students = teacherRepository.students2();
        return students;
    }
    public List<StudentDTO> getSelect() {
        List<StudentDTO> students = teacherRepository.students3();
        return students;
    }
    public Teacher getTeacher(String id) {
        return teacherRepository.findById(id).orElse(null);
    }
    @Autowired
    private ProcessScoreRepository processScoreRepository;
    public List<ProcessScore> getProcessScores(int groupId, String pid) {
        return processScoreRepository.getProcessScores(groupId,pid);
    }
    public List<ProcessScore> getProcessScores(String tid, String pid) {
        return processScoreRepository.getProcessScores(tid,pid);
    }
    @Transactional
    public Integer updateProcessScore(ProcessScore processScore) {
        if (processScore.getId() != null) {
            return processScoreRepository.updateProcessScore(String.valueOf(processScore.getId()), processScore.getDetail());
        }
        processScoreRepository.save(processScore);
        return 1;
    }
    public String getFileDetail(String number,String pid) {
        return teacherRepository.getFileDetail(number,pid);
    }
    public List<Process> getProcesses() {
        return teacherRepository.getProcesses();
    }
}
