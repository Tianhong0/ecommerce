package com.example.admin.controller;

import com.example.admin.dto.LoginRequest;
import com.example.admin.dto.RegisterRequest;
import com.example.admin.dto.UserInfoDTO;
import com.example.admin.dto.UserUpdateRequest;
import com.example.admin.entity.User;
import com.example.admin.exception.GlobalExceptionHandler.ApiResponse;
import com.example.admin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证控制器
 * 处理用户注册、登录、权限查询和个人信息管理等认证相关操作
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * 用户服务
     */
    private final UserService userService;

    /**
     * 用户注册
     * 
     * @param request 注册请求，包含用户名、密码等基本信息
     * @return 注册结果响应
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(new ApiResponse(true, "注册成功"));
    }

    /**
     * 用户登录
     * 
     * @param request 登录请求，包含用户名和密码
     * @return 登录结果响应，成功时返回JWT令牌
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(new ApiResponse(true, "登录成功", token));
    }
    
    /**
     * 获取当前登录用户的权限列表
     * 
     * @return 当前用户的权限编码列表
     */
    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse> getCurrentUserPermissions() {
        // 从安全上下文中获取当前用户权限
        List<String> permissions = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(new ApiResponse(true, "获取当前用户权限成功", permissions));
    }
    
    /**
     * 获取当前登录用户的详细信息
     * 
     * @return 当前用户的详细信息，包括基本信息和权限列表
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse> getCurrentUserInfo() {
        // 获取当前用户基本信息
        User user = userService.getCurrentUser();
        // 获取当前用户权限列表
        List<String> permissions = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        // 将用户基本信息和权限列表组装成前端所需的格式
        UserInfoDTO userInfo = UserInfoDTO.fromUser(user, permissions);
        return ResponseEntity.ok(new ApiResponse(true, "获取当前用户信息成功", userInfo));
    }
    
    /**
     * 更新当前登录用户的个人信息
     * 
     * @param request 用户信息更新请求
     * @return 更新结果响应
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCurrentUser(@Valid @RequestBody UserUpdateRequest request) {
        // 获取当前登录用户
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "用户未登录"));
        }
        
        // 创建用户对象并设置要更新的字段
        User user = new User();
        user.setId(currentUser.getId());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        
        // 更新用户信息
        userService.updateUser(user);
        
        return ResponseEntity.ok(new ApiResponse(true, "更新个人信息成功"));
    }
}
