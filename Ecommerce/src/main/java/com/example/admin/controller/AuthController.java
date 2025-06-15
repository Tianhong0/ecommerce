package com.example.admin.controller;

import com.example.admin.dto.LoginRequest;
import com.example.admin.dto.RegisterRequest;
import com.example.admin.dto.UserInfoDTO;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(new ApiResponse(true, "注册成功"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(new ApiResponse(true, "登录成功", token));
    }
    
    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse> getCurrentUserPermissions() {
        List<String> permissions = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(new ApiResponse(true, "获取当前用户权限成功", permissions));
    }
    
    @GetMapping("/info")
    public ResponseEntity<ApiResponse> getCurrentUserInfo() {
        User user = userService.getCurrentUser();
        List<String> permissions = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        UserInfoDTO userInfo = UserInfoDTO.fromUser(user, permissions);
        return ResponseEntity.ok(new ApiResponse(true, "获取当前用户信息成功", userInfo));
    }
}
