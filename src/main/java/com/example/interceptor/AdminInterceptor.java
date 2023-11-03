package com.example.interceptor;

import com.example.exception.XException;
import com.example.dox.User;
import com.example.vo.Code;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Integer role = (Integer) request.getAttribute("role");
        if (role.equals(User.ROLE_ADMIN)) {
            return true;
        }
        throw new XException(Code.UNAUTHORIZED,"无管理员权限");
    }
}
