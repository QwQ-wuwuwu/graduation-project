package com.example.service;

import com.example.dto.StudentDTO;
import com.example.exception.XException;
import com.example.dox.File;
import com.example.dox.Process;
import com.example.dox.Teacher;
import com.example.repository.FileRepository;
import com.example.repository.ProcessRepository;
import com.example.repository.StudentRepository;
import com.example.vo.Code;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ObjectMapper objectMapper;
    public List<Teacher> getInfo() {
        List<Teacher> info = studentRepository.getInfo();
        return info;
    }
    public StudentDTO getStudent(String id) {
        StudentDTO studentDTO = null;
        String value = null;
        try {
            String number = studentRepository.getNumber(id);
            studentDTO = studentRepository.getStudent(number);
            value = objectMapper.writeValueAsString(studentDTO);
        } catch (JsonProcessingException e) {
            throw new XException(Code.JSONERROR,e + "序列化失败");
        }
        return studentDTO;
    }
    // 未选择，只会提供未被选满的导师列表，减少逻辑判断
    @Transactional // 保证数据库操作原子性
    public boolean selectAndGroup(String tid,String sNumber) {
        Integer tGroupId = studentRepository.getGroupId(tid);
        Integer countGroupId = studentRepository.getCountGroupId();
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < countGroupId; i++) {
            integers.add(i,i + 1);
        }
        Random random = new Random();
        int index = 0;
        do {
            index = random.nextInt(integers.size());
        }while (integers.get(index).equals(tGroupId));
        Integer groupId = integers.get(index);
        LocalDateTime now = LocalDateTime.now();
        Integer student = studentRepository.updateStudent(sNumber, tid, groupId,now);
        if (student > 0) {
            Integer teacher = studentRepository.updateTeacher(tid);
            if (teacher > 0) {
                return true;
            }
            else return false;
        }
        return true;
    }
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private FileRepository fileRepository;
    public File saveFile(File file) {
        return fileRepository.save(file);
    }
    public List<Process> getAllProcess() {
        return processRepository.getAllProcess();
    }
    public String getNumber(String id) {
        return studentRepository.getNumber(id);
    }
}
