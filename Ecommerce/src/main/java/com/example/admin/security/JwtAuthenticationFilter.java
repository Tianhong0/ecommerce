package com.example.admin.security;

import com.example.admin.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * <p>
 * 该过滤器实现了Spring Security的OncePerRequestFilter抽象类，
 * 用于拦截每个HTTP请求，从请求头中提取JWT令牌并验证。
 * 如果令牌有效，则将用户身份信息和权限设置到Spring Security的上下文中，
 * 使后续的权限检查和业务处理能够获取到用户信息。
 * </p>
 * <p>
 * 工作流程：
 * 1. 从HTTP请求头中提取JWT令牌
 * 2. 验证令牌有效性
 * 3. 从令牌中提取用户名
 * 4. 加载用户详情
 * 5. 创建认证对象并设置到安全上下文
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT工具类
     * 用于验证和解析JWT令牌
     */
    private final JwtUtil jwtUtil;
    
    /**
     * 用户详情服务
     * 用于根据用户名加载用户详情
     */
    private final UserDetailsService userDetailsService;

    /**
     * 内部过滤方法
     * <p>
     * 处理每个HTTP请求，提取并验证JWT令牌，设置认证信息。
     * 如果令牌验证失败或不存在，则继续过滤链处理，交由后续安全机制处理。
     * </p>
     *
     * @param request 当前HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("用户 {} 已通过认证，权限：{}", username, userDetails.getAuthorities());
            }
        } catch (Exception e) {
            log.error("认证失败：{}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 从HTTP请求中提取JWT令牌
     * <p>
     * 从请求头的Authorization字段中提取JWT令牌。
     * 令牌格式必须为"Bearer [token]"。
     * </p>
     *
     * @param request HTTP请求
     * @return JWT令牌字符串，如果不存在或格式不正确则返回null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        
        return null;
    }
} 