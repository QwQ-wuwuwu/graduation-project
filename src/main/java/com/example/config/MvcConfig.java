package com.example.config;

import com.example.interceptor.AdminInterceptor;
import com.example.interceptor.LoginInterceptor;
import com.example.interceptor.OptionsInterceptor;
import com.example.interceptor.TeacherInterception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
    @Autowired
    private OptionsInterceptor optionsInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(optionsInterceptor).addPathPatterns("/**"); // 解决options请求问题，直接返回200状态码
        registry.addInterceptor(loginInterception).addPathPatterns("/api/**")
                .excludePathPatterns("/api/login");
        registry.addInterceptor(adminInterceptor).addPathPatterns("/api/admin/**");
        registry.addInterceptor(teacherInterception).addPathPatterns("/api/teacher/**");
    }
    @Override // 解决跨域问题
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
