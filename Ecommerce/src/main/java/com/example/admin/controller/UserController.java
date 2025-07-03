package com.example.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.UserDTO;
import com.example.admin.entity.User;
import com.example.admin.service.UserService;
import com.example.admin.exception.GlobalExceptionHandler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 * 提供用户CRUD操作、状态管理和角色分配等功能
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * 用户服务
     */
    private final UserService userService;

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建结果响应
     */
    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok(new ApiResponse(true, "创建用户成功"));
    }

    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param user 更新后的用户信息
     * @return 更新结果响应
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return ResponseEntity.ok(new ApiResponse(true, "更新用户成功"));
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 删除结果响应
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除用户成功"));
    }

    /**
     * 获取用户详情
     * 
     * @param id 用户ID
     * @return 用户详情响应
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:query')")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "获取用户成功", user));
    }

    /**
     * 分页查询用户列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param username 用户名（可选，模糊查询）
     * @param nickname 昵称（可选，模糊查询）
     * @param email 邮箱（可选，模糊查询）
     * @param phone 手机号（可选，模糊查询）
     * @param status 状态（可选）
     * @return 用户列表分页结果
     */
    @GetMapping
    @PreAuthorize("hasAuthority('user:query')")
    public ResponseEntity<ApiResponse> listUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status) {
        Page<UserDTO> page = userService.listUsers(pageNum, pageSize, username, nickname, email, phone, status);
        return ResponseEntity.ok(new ApiResponse(true, "获取用户列表成功", page));
    }

    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 新状态（1-启用，0-禁用）
     * @return 更新结果响应
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponse> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "更新用户状态成功"));
    }

    /**
     * 为用户分配角色
     * 
     * @param id 用户ID
     * @param roleIds 角色ID列表
     * @return 分配结果响应
     */
    @PostMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponse> assignRoles(
            @PathVariable Long id,
            @RequestBody List<Long> roleIds) {
        userService.assignRoles(id, roleIds);
        return ResponseEntity.ok(new ApiResponse(true, "分配角色成功"));
    }

    /**
     * 获取用户角色列表
     * 
     * @param id 用户ID
     * @return 用户角色ID列表
     */
    @GetMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('user:query')")
    public ResponseEntity<ApiResponse> getUserRoles(@PathVariable Long id) {
        List<Long> roleIds = userService.getUserRoleIds(id);
        return ResponseEntity.ok(new ApiResponse(true, "获取用户角色成功", roleIds));
    }
}
