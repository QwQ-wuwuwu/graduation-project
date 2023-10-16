package com.example.interceptor;

import com.example.entity.MyToken;
import com.example.exception.XException;
import com.example.pojo.User;
import com.example.vo.Code;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TeacherInterception implements HandlerInterceptor {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = response.getHeader("token");
        MyToken myToken = objectMapper.readValue(token, MyToken.class);
        Integer role = Integer.valueOf(myToken.getRole());
        if (role.equals(User.ROLE_TEACHER)) {
            return true;
        }
        throw new XException(Code.UNAUTHORIZED,"无教师权限");
    }
}
