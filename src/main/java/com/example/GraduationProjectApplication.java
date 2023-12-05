package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.text.SimpleDateFormat;

@SpringBootApplication
@EnableJdbcRepositories(basePackages = "com.example.repository")
public class GraduationProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(GraduationProjectApplication.class, args);
	}
	@Bean
	public ObjectMapper objectMapper() { // 序列化工具
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // 设置时间戳格式
		objectMapper.findAndRegisterModules(); // 解决java8不支持序列化LocalDateTime的问题
		return objectMapper;
	}
}
