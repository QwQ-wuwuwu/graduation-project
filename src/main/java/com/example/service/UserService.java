package com.example.service;

import com.example.config.PasswordEncodeConfig;
import com.example.dox.Process;
import com.example.repository.ProcessRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<Process> processList() {
        return null;
    }
}
