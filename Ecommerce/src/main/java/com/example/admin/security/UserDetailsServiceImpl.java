package com.example.admin.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.admin.entity.Permission;
import com.example.admin.entity.RolePermission;
import com.example.admin.entity.User;
import com.example.admin.entity.UserRole;
import com.example.admin.mapper.PermissionMapper;
import com.example.admin.mapper.RolePermissionMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 获取用户角色ID列表
        List<Long> roleIds = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, user.getId()))
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());

        // 如果是超级管理员（假设角色ID为1的是超级管理员），直接返回所有权限
        if (roleIds.contains(1L)) {
            List<String> allPermissions = permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                    .eq(Permission::getStatus, 1))
                    .stream()
                    .map(Permission::getCode)
                    .collect(Collectors.toList());
            return new UserDetailsImpl(user, allPermissions);
        }

        // 如果用户没有角色，返回空权限列表
        if (roleIds.isEmpty()) {
            return new UserDetailsImpl(user, new ArrayList<>());
        }

        // 获取角色权限ID列表
        List<Long> permissionIds = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>()
                .in(RolePermission::getRoleId, roleIds))
                .stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        // 如果角色没有权限，返回空权限列表
        if (permissionIds.isEmpty()) {
            return new UserDetailsImpl(user, new ArrayList<>());
        }

        // 获取权限编码列表
        List<String> permissions = permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                .in(Permission::getId, permissionIds)
                .eq(Permission::getStatus, 1))
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toList());

        return new UserDetailsImpl(user, permissions);
    }
} 