package com.example.service;

import com.example.config.PasswordEncodeConfig;
import com.example.pojo.Process;
import com.example.repository.ProcessRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncodeConfig passwordEncodeConfig;
    @Autowired
    private ProcessRepository processRepository;
    @Transactional
    public Integer updatePassword(String uid, String password) {
        String encode = passwordEncodeConfig.passwordEncoder().encode(password);
        Integer integer = userRepository.updatePasswordByUser(uid, encode);
        return integer;
    }
    @Autowired
    private LockService lockService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public List<Process> processList() throws JsonProcessingException {
        String key = "user:service:processList";
        String s = stringRedisTemplate.opsForValue().get(key);
        TypeReference<List<Process>> typeReference = new TypeReference<>() {};
        if (s != null) {
            if (s.equals("null")) {
                return null;
            }
            return objectMapper.readValue(s, typeReference);
        }
        String lock = "lock:user";
        List<Process> all = (List<Process>) processRepository.findAll();
        try {
            boolean flag = lockService.tryLock(lock);
            if (!flag) {
                Thread.sleep(200);
                return processList();
            }
            if (all == null) {
                stringRedisTemplate.opsForValue().set(key,"随意",5, TimeUnit.MINUTES);
                return null;
            }
            String value = objectMapper.writeValueAsString(all);
            stringRedisTemplate.opsForValue().set(key,value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lockService.unlock(lock);
        }
        return all;
    }
}
