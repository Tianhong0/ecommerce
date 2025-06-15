package com.example.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.LoginRequest;
import com.example.admin.dto.RegisterRequest;
import com.example.admin.dto.UserDTO;
import com.example.admin.entity.User;

import java.util.List;

public interface UserService {
    // 认证相关
    String login(LoginRequest request);
    
    void register(RegisterRequest request);
    
    User getCurrentUser();
    
    // 用户管理
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User getUser(Long id);
    Page<UserDTO> listUsers(Integer pageNum, Integer pageSize, String username, String nickname, String email, String phone, Integer status);
    void updateUserStatus(Long id, Integer status);
    
    // 用户角色管理
    void assignRoles(Long userId, List<Long> roleIds);
    List<Long> getUserRoleIds(Long userId);
} 