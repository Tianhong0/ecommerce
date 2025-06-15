package com.example.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.entity.Role;
import com.example.admin.service.RoleService;
import com.example.admin.exception.GlobalExceptionHandler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseEntity<ApiResponse> createRole(@Valid @RequestBody Role role) {
        roleService.createRole(role);
        return ResponseEntity.ok(new ApiResponse(true, "创建角色成功"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Long id, @Valid @RequestBody Role role) {
        role.setId(id);
        roleService.updateRole(role);
        return ResponseEntity.ok(new ApiResponse(true, "更新角色成功"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除角色成功"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:query')")
    public ResponseEntity<ApiResponse> getRole(@PathVariable Long id) {
        Role role = roleService.getRole(id);
        return ResponseEntity.ok(new ApiResponse(true, "获取角色成功", role));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('role:query')")
    public ResponseEntity<ApiResponse> listRoles(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<Role> page = roleService.listRoles(pageNum, pageSize, name);
        return ResponseEntity.ok(new ApiResponse(true, "获取角色列表成功", page));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<ApiResponse> updateRoleStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        roleService.updateRoleStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "更新角色状态成功"));
    }

    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<ApiResponse> assignPermissions(
            @PathVariable Long id,
            @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(id, permissionIds);
        return ResponseEntity.ok(new ApiResponse(true, "分配权限成功"));
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role:query')")
    public ResponseEntity<ApiResponse> getRolePermissions(@PathVariable Long id) {
        List<Long> permissionIds = roleService.getRolePermissionIds(id);
        return ResponseEntity.ok(new ApiResponse(true, "获取角色权限成功", permissionIds));
    }
}
