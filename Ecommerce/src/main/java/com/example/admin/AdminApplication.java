package com.example.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 电商后台管理系统应用程序入口类
 * <p>
 * 该类是Spring Boot应用程序的启动类，使用@SpringBootApplication注解，
 * 它结合了@Configuration, @EnableAutoConfiguration和@ComponentScan。
 * 系统启动时，将从这里开始初始化Spring容器、加载配置、扫描组件等。
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
@SpringBootApplication
public class AdminApplication {
    
    /**
     * 应用程序主入口方法
     * <p>
     * 启动Spring Boot应用程序，初始化Spring容器，
     * 加载所有组件和配置
     * </p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
} 