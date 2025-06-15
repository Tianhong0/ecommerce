package com.example.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.entity.Role;

import java.util.List;

public interface RoleService {
    // 创建角色
    void createRole(Role role);
    
    // 更新角色
    void updateRole(Role role);
    
    // 删除角色
    void deleteRole(Long id);
    
    // 获取角色详情
    Role getRole(Long id);
    
    // 分页查询角色列表
    Page<Role> listRoles(Integer pageNum, Integer pageSize, String name);
    
    // 更新角色状态
    void updateRoleStatus(Long id, Integer status);
    
    // 分配角色权限
    void assignPermissions(Long roleId, List<Long> permissionIds);
    
    // 获取角色权限ID列表
    List<Long> getRolePermissionIds(Long roleId);
} 