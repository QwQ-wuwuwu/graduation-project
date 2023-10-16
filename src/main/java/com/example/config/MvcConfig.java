package com.example.config;

import com.example.interceptor.AdminInterceptor;
import com.example.interceptor.LoginInterceptor;
import com.example.interceptor.TeacherInterception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterception;
    @Autowired
    private AdminInterceptor adminInterceptor;
    @Autowired
    private TeacherInterception teacherInterception;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterception).addPathPatterns("/api/**")
                .excludePathPatterns("/api/login");
        registry.addInterceptor(adminInterceptor).addPathPatterns("/api/admin/**");
        registry.addInterceptor(teacherInterception).addPathPatterns("/api/teacher/**");
    }
}
