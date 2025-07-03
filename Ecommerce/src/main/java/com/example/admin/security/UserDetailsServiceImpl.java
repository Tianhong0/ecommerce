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

/**
 * Spring Security的UserDetailsService实现类
 * 负责根据用户名加载用户详情，包括用户基本信息和权限列表
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 用户数据访问对象
     */
    private final UserMapper userMapper;
    
    /**
     * 用户角色关联数据访问对象
     */
    private final UserRoleMapper userRoleMapper;
    
    /**
     * 角色权限关联数据访问对象
     */
    private final RolePermissionMapper rolePermissionMapper;
    
    /**
     * 权限数据访问对象
     */
    private final PermissionMapper permissionMapper;

    /**
     * 根据用户名加载用户详情
     * 包括用户基本信息和该用户拥有的所有权限
     *
     * @param username 用户名
     * @return UserDetails对象，包含用户信息和权限列表
     * @throws UsernameNotFoundException 当用户不存在时抛出异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        // 用户不存在则抛出异常
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
            // 获取所有启用状态的权限
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

        // 获取权限编码列表（只获取状态为启用的权限）
        List<String> permissions = permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                .in(Permission::getId, permissionIds)
                .eq(Permission::getStatus, 1))
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toList());

        // 返回自定义的UserDetails实现
        return new UserDetailsImpl(user, permissions);
    }
} 