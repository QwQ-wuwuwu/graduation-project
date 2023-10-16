package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration // 如果不写成配置类的话会和spring-boot-starter-data-redis冲突，导致找不到repository，还要注意实体类上标注@Table()
public class JdbcConfig {
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/project?createDataBaseIfNotExist=true&serverTimezone=Asia/Shanghai");
        dataSource.setUsername("root");
        dataSource.setPassword("532129");
        return dataSource;
    }
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
