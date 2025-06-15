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

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok(new ApiResponse(true, "创建用户成功"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return ResponseEntity.ok(new ApiResponse(true, "更新用户成功"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除用户成功"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:query')")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "获取用户成功", user));
    }

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

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponse> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "更新用户状态成功"));
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponse> assignRoles(
            @PathVariable Long id,
            @RequestBody List<Long> roleIds) {
        userService.assignRoles(id, roleIds);
        return ResponseEntity.ok(new ApiResponse(true, "分配角色成功"));
    }

    @GetMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('user:query')")
    public ResponseEntity<ApiResponse> getUserRoles(@PathVariable Long id) {
        List<Long> roleIds = userService.getUserRoleIds(id);
        return ResponseEntity.ok(new ApiResponse(true, "获取用户角色成功", roleIds));
    }
}
