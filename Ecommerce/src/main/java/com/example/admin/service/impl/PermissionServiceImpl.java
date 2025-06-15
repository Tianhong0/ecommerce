package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.entity.Permission;
import com.example.admin.mapper.PermissionMapper;
import com.example.admin.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    
    private final PermissionMapper permissionMapper;
    
    @Override
    @Transactional
    public void createPermission(Permission permission) {
        // 检查权限编码是否已存在
        if (permissionMapper.selectCount(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getCode, permission.getCode())) > 0) {
            throw new RuntimeException("权限编码已存在");
        }
        
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        permission.setStatus(1);
        
        permissionMapper.insert(permission);
    }
    
    @Override
    @Transactional
    public void updatePermission(Permission permission) {
        // 检查权限是否存在
        Permission existingPermission = permissionMapper.selectById(permission.getId());
        if (existingPermission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        // 检查权限编码是否重复
        if (!existingPermission.getCode().equals(permission.getCode()) &&
            permissionMapper.selectCount(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getCode, permission.getCode())) > 0) {
            throw new RuntimeException("权限编码已存在");
        }
        
        permission.setUpdateTime(LocalDateTime.now());
        permissionMapper.updateById(permission);
    }
    
    @Override
    @Transactional
    public void deletePermission(Long id) {
        // TODO: 检查权限是否被角色使用
        permissionMapper.deleteById(id);
    }
    
    @Override
    public Permission getPermission(Long id) {
        return permissionMapper.selectById(id);
    }
    
    @Override
    public Page<Permission> listPermissions(Integer pageNum, Integer pageSize, String name, String code) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(name)) {
            wrapper.like(Permission::getName, name);
        }
        
        if (StringUtils.hasText(code)) {
            wrapper.like(Permission::getCode, code);
        }
        
        wrapper.orderByAsc(Permission::getSort);
        
        return permissionMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }
    
    @Override
    public List<Permission> getPermissionTree() {
        // 获取所有权限
        List<Permission> allPermissions = permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                .orderByAsc(Permission::getSort));
        
        // 构建权限树
        Map<Long, List<Permission>> parentMap = allPermissions.stream()
                .collect(Collectors.groupingBy(p -> p.getParentId() == null ? 0L : p.getParentId()));
        
        return buildTree(0L, parentMap);
    }
    
    private List<Permission> buildTree(Long parentId, Map<Long, List<Permission>> parentMap) {
        List<Permission> children = parentMap.get(parentId);
        if (children == null) {
            return new ArrayList<>();
        }
        
        for (Permission child : children) {
            List<Permission> grandChildren = buildTree(child.getId(), parentMap);
            if (!grandChildren.isEmpty()) {
                child.setChildren(grandChildren);
            }
        }
        
        return children;
    }
    
    @Override
    @Transactional
    public void updatePermissionStatus(Long id, Integer status) {
        Permission permission = new Permission();
        permission.setId(id);
        permission.setStatus(status);
        permission.setUpdateTime(LocalDateTime.now());
        
        permissionMapper.updateById(permission);
    }
} 