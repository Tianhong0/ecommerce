package com.example.admin.config;

import com.example.admin.security.JwtAuthenticationFilter;
import com.example.admin.security.SecurityExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 安全配置类
 * <p>
 * 配置系统的安全相关设置，包括：
 * - HTTP安全配置
 * - JWT认证过滤器集成
 * - 密码编码器
 * - 认证管理器
 * 
 * 该配置使用JWT进行无状态认证，关闭了CSRF保护（因为使用JWT），
 * 并针对不同的API路径设置了不同的访问权限要求。
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * JWT认证过滤器
     * 用于处理基于JWT的身份认证
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * 安全异常处理器
     * 处理认证和授权过程中的异常
     */
    private final SecurityExceptionHandler securityExceptionHandler;

    /**
     * 配置安全过滤链
     * <p>
     * 定义HTTP安全配置，包括：
     * - 禁用CSRF保护
     * - 启用无状态会话管理
     * - 设置API访问权限
     * - 配置异常处理机制
     * - 添加JWT过滤器
     * </p>
     *
     * @param http HttpSecurity配置对象
     * @return 配置完成的安全过滤链
     * @throws Exception 如果配置过程中出现错误
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 允许所有人访问的接口
                .requestMatchers("/auth/login", "/auth/register","/system/info","/system/cpu").permitAll()
                // 允许产品查询相关接口无需认证访问
                .requestMatchers(
                    "/product", 
                    "/product/**", 
                    "/category", 
                    "/category/**"
                ).permitAll()
                // 其他所有接口都需要认证
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(securityExceptionHandler)
                .accessDeniedHandler(securityExceptionHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 密码编码器
     * <p>
     * 提供密码的加密和验证功能，使用BCrypt算法
     * BCrypt是一种单向哈希函数，专门为密码存储设计，
     * 自动集成盐值，并可调整工作因子以应对硬件性能提升
     * </p>
     *
     * @return BCrypt密码编码器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     * <p>
     * 处理认证请求的核心组件，由Spring Security自动配置
     * </p>
     *
     * @param config 认证配置
     * @return 认证管理器实例
     * @throws Exception 如果获取认证管理器时出错
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
