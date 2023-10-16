package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LockService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public boolean tryLock(String key) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, "随意一个值", 5, TimeUnit.SECONDS);
    }
    public void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
