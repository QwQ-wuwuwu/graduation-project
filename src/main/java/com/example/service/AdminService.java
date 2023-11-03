package com.example.service;

import com.example.config.PasswordEncodeConfig;
import com.example.pojo.StartAndEndTime;
import com.example.dox.Process;
import com.example.dox.Student;
import com.example.dox.Teacher;
import com.example.dox.User;
import com.example.repository.ProcessRepository;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private StartAndEndTime startAndEndTime;
    public StartAndEndTime time(LocalDateTime start, LocalDateTime end) {
        this.startAndEndTime.setStart(start);
        this.startAndEndTime.setEnd(end);
        return this.startAndEndTime;
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncodeConfig passwordEncodeConfig;
    @Transactional
    public Integer updatePassword(String number) {
        String encode = passwordEncodeConfig.passwordEncoder().encode(number);
        Integer integer = userRepository.updatePassword(number,encode);
        return integer;
    }
    @Autowired
    private TeacherRepository teacherRepository;
    @Transactional
    public boolean saveTeachers(List<Teacher> teachers) {
        int number = 0;
        for (Teacher teacher : teachers) {
            teacherRepository.save(teacher);
            String encode = passwordEncodeConfig.passwordEncoder().encode(teacher.getUserNumber());
            User user = new User();
            user.setNumber(teacher.getUserNumber());
            user.setPassword(encode);
            user.setRole(User.ROLE_TEACHER);
            user.setDescription("导师");
            userRepository.save(user);
            number++;
        }
        if (number != teachers.size()) {
            return false;
        }
        return true;
    }
    @Autowired
    private StudentRepository studentRepository;
    @Transactional
    public boolean saveStudents(List<Student> students) {
        int x = 0;
        for (Student student : students) {
            studentRepository.save(student);
            User user = new User();
            String encode = passwordEncodeConfig.passwordEncoder().encode(student.getUserNumber());
            user.setNumber(student.getUserNumber());
            user.setPassword(encode);
            user.setRole(User.ROLE_TEACHER);
            user.setDescription("学生");
            userRepository.save(user);
        }
        if (x != students.size()) {
            return false;
        }
        return true;
    }
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Transactional
    public Integer updateGroup(Integer groupId,String number) {
        Integer integer = studentRepository.updateGroup(groupId, number);
        stringRedisTemplate.delete("student:service:getStudent");
        return integer;
    }
    @Autowired
    private ProcessRepository processRepository;
    public Process postProcess(Process process) {
        return processRepository.save(process);
    }
}
