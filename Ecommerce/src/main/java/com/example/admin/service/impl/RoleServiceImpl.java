package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.entity.Role;
import com.example.admin.entity.RolePermission;
import com.example.admin.mapper.RoleMapper;
import com.example.admin.mapper.RolePermissionMapper;
import com.example.admin.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    
    @Override
    @Transactional
    public void createRole(Role role) {
        // 检查角色编码是否已存在
        if (roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .eq(Role::getCode, role.getCode())) > 0) {
            throw new RuntimeException("角色编码已存在");
        }
        
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setStatus(1);
        
        roleMapper.insert(role);
    }
    
    @Override
    @Transactional
    public void updateRole(Role role) {
        // 检查角色是否存在
        Role existingRole = roleMapper.selectById(role.getId());
        if (existingRole == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 检查角色编码是否重复
        if (!existingRole.getCode().equals(role.getCode()) &&
            roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .eq(Role::getCode, role.getCode())) > 0) {
            throw new RuntimeException("角色编码已存在");
        }
        
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateById(role);
    }
    
    @Override
    @Transactional
    public void deleteRole(Long id) {
        // 删除角色权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, id));
        
        // 删除角色
        roleMapper.deleteById(id);
    }
    
    @Override
    public Role getRole(Long id) {
        return roleMapper.selectById(id);
    }
    
    @Override
    public Page<Role> listRoles(Integer pageNum, Integer pageSize, String name) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Role::getName, name);
        }
        wrapper.orderByDesc(Role::getCreateTime);
        
        return roleMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }
    
    @Override
    @Transactional
    public void updateRoleStatus(Long id, Integer status) {
        Role role = new Role();
        role.setId(id);
        role.setStatus(status);
        role.setUpdateTime(LocalDateTime.now());
        
        roleMapper.updateById(role);
    }
    
    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 删除原有权限
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId));
        
        // 添加新权限
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermission.setCreateTime(LocalDateTime.now());
            rolePermissionMapper.insert(rolePermission);
        }
    }
    
    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        return rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId))
                .stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
    }
} 