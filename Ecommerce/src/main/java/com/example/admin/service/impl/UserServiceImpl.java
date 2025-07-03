package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.LoginRequest;
import com.example.admin.dto.RegisterRequest;
import com.example.admin.dto.UserDTO;
import com.example.admin.entity.User;
import com.example.admin.entity.UserRole;
import com.example.admin.exception.BusinessException;
import com.example.admin.mapper.UserMapper;
import com.example.admin.mapper.UserRoleMapper;
import com.example.admin.security.UserDetailsImpl;
import com.example.admin.service.UserService;
import com.example.admin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 提供用户认证、用户管理和用户角色管理相关功能
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    /**
     * 用户数据访问对象
     */
    private final UserMapper userMapper;
    
    /**
     * 用户角色关联数据访问对象
     */
    private final UserRoleMapper userRoleMapper;
    
    /**
     * 密码加密工具
     */
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 认证管理器
     */
    private final AuthenticationManager authenticationManager;
    
    /**
     * JWT工具类
     */
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     * 
     * @param request 登录请求，包含用户名和密码
     * @return JWT令牌字符串
     */
    @Override
    public String login(LoginRequest request) {
        // 使用Spring Security进行身份验证
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // 将认证信息存入安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成JWT令牌
        return jwtUtil.generateToken(authentication);
    }

    /**
     * 用户注册
     * 
     * @param request 注册请求，包含用户基本信息
     * @throws RuntimeException 当用户名已存在时抛出异常
     */
    @Override
    @Transactional
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户对象并设置属性
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 密码加密存储
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStatus(1); // 设置状态为启用
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 保存用户信息到数据库
        userMapper.insert(user);
    }

    /**
     * 获取当前登录用户信息
     * 
     * @return 当前登录用户实体，如果未登录则返回null
     */
    @Override
    public User getCurrentUser() {
        // 从安全上下文中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        }
        return null;
    }
    
    /**
     * 创建新用户（管理员操作）
     * 
     * @param user 用户信息
     * @throws BusinessException 当用户名已存在时抛出异常
     */
    @Override
    @Transactional
    public void createUser(User user) {
        // 检查用户名是否已存在
        if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(1); // 设置状态为启用
        
        // 保存用户信息到数据库
        userMapper.insert(user);
    }
    
    /**
     * 更新用户信息
     * 
     * @param user 更新后的用户信息
     * @throws BusinessException 当用户不存在或用户名重复时抛出异常
     */
    @Override
    @Transactional
    public void updateUser(User user) {
        // 检查用户是否存在
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户名是否重复（仅当用户名发生变化时检查）
        if (!existingUser.getUsername().equals(user.getUsername()) &&
            userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 如果密码不为空，则更新密码（加密处理）
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null); // 不更新密码字段
        }
        
        // 更新时间
        user.setUpdateTime(LocalDateTime.now());
        // 更新用户信息
        userMapper.updateById(user);
    }
    
    /**
     * 删除用户
     * 
     * @param id 用户ID
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        // 删除用户角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, id));
        
        // 删除用户
        userMapper.deleteById(id);
    }
    
    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户实体
     */
    @Override
    public User getUser(Long id) {
        return userMapper.selectById(id);
    }
    
    /**
     * 分页查询用户列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param username 用户名（模糊查询）
     * @param nickname 昵称（模糊查询）
     * @param email 邮箱（模糊查询）
     * @param phone 手机号（模糊查询）
     * @param status 状态
     * @return 用户DTO分页对象
     */
    @Override
    public Page<UserDTO> listUsers(Integer pageNum, Integer pageSize, String username, String nickname, String email, String phone, Integer status) {
        // 查询用户列表
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(username)) {
            wrapper.like(User::getUsername, username);
        }
        if (StringUtils.hasText(nickname)) {
            wrapper.like(User::getNickname, nickname);
        }
        if (StringUtils.hasText(email)) {
            wrapper.like(User::getEmail, email);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(User::getPhone, phone);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        
        // 按创建时间倒序排序
        wrapper.orderByDesc(User::getCreateTime);
        
        // 执行分页查询
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        
        // 获取所有用户ID
        List<Long> userIds = userPage.getRecords().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        
        // 查询用户角色关联
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getUserId, userIds));
        
        // 按用户ID分组角色ID
        Map<Long, List<Long>> userRoleMap = userRoles.stream()
                .collect(Collectors.groupingBy(
                        UserRole::getUserId,
                        Collectors.mapping(UserRole::getRoleId, Collectors.toList())
                ));
        
        // 转换为UserDTO
        Page<UserDTO> dtoPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserDTO> dtoList = userPage.getRecords().stream().map(user -> {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            dto.setRoleIds(userRoleMap.getOrDefault(user.getId(), List.of()));
            return dto;
        }).collect(Collectors.toList());
        
        dtoPage.setRecords(dtoList);
        return dtoPage;
    }
    
    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 新状态（1-启用，0-禁用）
     */
    @Override
    @Transactional
    public void updateUserStatus(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        
        userMapper.updateById(user);
    }
    
    /**
     * 为用户分配角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        // 删除原有角色
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId));
        
        // 添加新角色
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateTime(LocalDateTime.now());
            userRoleMapper.insert(userRole);
        }
    }
    
    /**
     * 获取用户的角色ID列表
     * 
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId))
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }
} 