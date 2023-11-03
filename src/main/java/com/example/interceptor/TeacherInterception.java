package com.example.interceptor;

import com.example.exception.XException;
import com.example.dox.User;
import com.example.vo.Code;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TeacherInterception implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Integer role = (Integer) request.getAttribute("role");
        if (role.equals(User.ROLE_TEACHER)) {
            return true;
        }
        throw new XException(Code.UNAUTHORIZED,"无教师权限");
    }
}
