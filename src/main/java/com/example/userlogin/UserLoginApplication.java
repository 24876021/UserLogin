package com.example.userlogin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * @author tian
 */
@MapperScan(basePackages = "com.example.userlogin.mapper")
@SpringBootApplication
public class UserLoginApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserLoginApplication.class);}
}
