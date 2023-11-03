package com.example.service;

import com.example.config.PasswordEncodeConfig;
import com.example.exception.XException;
import com.example.dox.Process;
import com.example.repository.ProcessRepository;
import com.example.repository.UserRepository;
import com.example.vo.Code;
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
        return userRepository.updatePasswordByUser(uid, encode);
    }
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public List<Process> processList() {
        String key = "user:service:processList";
        String s = stringRedisTemplate.opsForValue().get(key);
        TypeReference<List<Process>> typeReference = new TypeReference<>() {};
        String value = null;
        List<Process> all = null;
        try {
            if (s != null) {
                return objectMapper.readValue(s, typeReference);
            }
            all = (List<Process>) processRepository.findAll();
            value = objectMapper.writeValueAsString(all);
        } catch (JsonProcessingException e) {
            throw new XException(Code.JSONERROR,e + "序列化失败");
        }
        stringRedisTemplate.opsForValue().set(key,value,5,TimeUnit.SECONDS);
        return all;
    }
}
