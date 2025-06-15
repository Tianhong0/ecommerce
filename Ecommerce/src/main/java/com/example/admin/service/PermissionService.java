package com.example.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.entity.Permission;

import java.util.List;

public interface PermissionService {
    // 创建权限
    void createPermission(Permission permission);
    
    // 更新权限
    void updatePermission(Permission permission);
    
    // 删除权限
    void deletePermission(Long id);
    
    // 获取权限详情
    Permission getPermission(Long id);
    
    // 分页查询权限列表
    Page<Permission> listPermissions(Integer pageNum, Integer pageSize, String name, String code);
    
    // 获取权限树
    List<Permission> getPermissionTree();
    
    // 更新权限状态
    void updatePermissionStatus(Long id, Integer status);
} 