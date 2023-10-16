package com.example.service;

import com.example.dto.StudentDTO;
import com.example.pojo.File;
import com.example.pojo.Process;
import com.example.pojo.Teacher;
import com.example.repository.FileRepository;
import com.example.repository.ProcessRepository;
import com.example.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    public List<Teacher> getInfo() {
        List<Teacher> info = studentRepository.getInfo();
        return info;
    }
    // 解决缓存穿透和缓存击穿,setnx特性模拟互斥锁
    private boolean tryLock(String key) {
        Boolean absent = stringRedisTemplate.opsForValue().setIfAbsent(key, "随意一个值", 5, TimeUnit.SECONDS);
        return absent;
    }
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
    public StudentDTO getStudent(String id) throws JsonProcessingException {
        String key = "student:service:getStudent"; // 保证唯一
        String redis = stringRedisTemplate.opsForValue().get(key);
        if (redis != null) {
            if (redis.equals("null")) {
                return null;
            }
            StudentDTO studentDTO = objectMapper.readValue(redis, StudentDTO.class);
            return studentDTO;
        }
        StudentDTO studentDTO = studentRepository.getStudent(id);
        String lock = "lock:student";
        try {
            boolean flag = tryLock(lock); // 保证锁id唯一不变
            if (!flag) {
                Thread.sleep(50);
                return getStudent(id);// 存在栈溢出风险
            }
            if (studentDTO == null ) {
                stringRedisTemplate.opsForValue().set(key,"null",5,TimeUnit.SECONDS);
                return null;
            }
            String value = objectMapper.writeValueAsString(studentDTO); // 重建缓存
            stringRedisTemplate.opsForValue().set(key,value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            unlock(lock);
        }
        return studentDTO;
    }
    // 未选择，只会提供未被选满的导师列表，减少逻辑判断
    @Transactional // 保证数据库操作原子性
    public boolean selectAndGroup(String tid,String uid) {
        Integer tGroupId = studentRepository.getGroupId(tid);
        Integer countGroupId = studentRepository.getCountGroupId();
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < countGroupId; i++) {
            integers.add(i,i + 1);
        }
        Random random = new Random();
        Integer index = 0;
        do {
            index = random.nextInt(integers.size());
        }while (integers.get(index).equals(tGroupId));
        Integer groupId = integers.get(index);
        Integer student = studentRepository.updateStudent(uid, tid, groupId);
        stringRedisTemplate.delete("student:service:getStudent"); // 更新学生数据时，删除以前的缓存，以保证数据库和缓存一致
        if (student > 0) {
            Integer teacher = studentRepository.updateTeacher(tid);
            if (teacher > 0) {
                return true;
            }
            else return false;
        }
        return false;
    }
    @Autowired
    private ProcessRepository processRepository;
    public Process saveProcess(StudentDTO studentDTO, String fName) throws JsonProcessingException {
        Process process = new Process();
        String name = studentDTO.getSName();
        String number = studentDTO.getSNumber();
        Map<String, String> json = Map.of("name", name, "number", number);
        String value = objectMapper.writeValueAsString(json);
        process.setStudentDetail(value);
        process.setProcessName(fName);
        return processRepository.save(process);
    }
    @Autowired
    private FileRepository fileRepository;
    public File saveFile(File file) {
        return fileRepository.save(file);
    }
}
