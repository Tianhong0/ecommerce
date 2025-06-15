package com.example.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.entity.Permission;
import com.example.admin.service.PermissionService;
import com.example.admin.exception.GlobalExceptionHandler.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasAuthority('permission:create')")
    public ResponseEntity<ApiResponse> createPermission(@Valid @RequestBody Permission permission) {
        permissionService.createPermission(permission);
        return ResponseEntity.ok(new ApiResponse(true, "创建权限成功"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseEntity<ApiResponse> updatePermission(@PathVariable Long id, @Valid @RequestBody Permission permission) {
        permission.setId(id);
        permissionService.updatePermission(permission);
        return ResponseEntity.ok(new ApiResponse(true, "更新权限成功"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public ResponseEntity<ApiResponse> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除权限成功"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:query')")
    public ResponseEntity<ApiResponse> getPermission(@PathVariable Long id) {
        Permission permission = permissionService.getPermission(id);
        return ResponseEntity.ok(new ApiResponse(true, "获取权限成功", permission));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('permission:query')")
    public ResponseEntity<ApiResponse> listPermissions(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {
        Page<Permission> page = permissionService.listPermissions(pageNum, pageSize, name, code);
        return ResponseEntity.ok(new ApiResponse(true, "获取权限列表成功", page));
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('permission:query')")
    public ResponseEntity<ApiResponse> getPermissionTree() {
        List<Permission> tree = permissionService.getPermissionTree();
        return ResponseEntity.ok(new ApiResponse(true, "获取权限树成功", tree));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseEntity<ApiResponse> updatePermissionStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        permissionService.updatePermissionStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "更新权限状态成功"));
    }
}
