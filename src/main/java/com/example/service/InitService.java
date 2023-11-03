package com.example.service;

import com.example.config.PasswordEncodeConfig;
import com.example.dox.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InitService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncodeConfig passwordEncodeConfig;
    @EventListener(classes = ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        User user = userRepository.findByRole(User.ROLE_ADMIN);
        if (user == null) {
            User newUser = new User();
            newUser.setNumber("0000000000");
            newUser.setPassword(passwordEncodeConfig.passwordEncoder().encode("0000000000"));
            newUser.setRole(User.ROLE_ADMIN);
            newUser.setDescription("超级管理员");
            userRepository.save(newUser);
        }
    }
}
