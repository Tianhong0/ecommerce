package com.example.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.LoginRequest;
import com.example.admin.dto.RegisterRequest;
import com.example.admin.dto.UserDTO;
import com.example.admin.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * <p>
 * 提供用户相关的业务逻辑处理，包括用户认证、用户管理和用户角色管理三个主要部分。
 * 实现了用户的登录、注册、信息获取、创建、更新、删除等功能，
 * 以及用户角色的分配与查询。
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
public interface UserService {
    /**
     * 用户登录
     * <p>
     * 验证用户登录凭证并生成JWT令牌
     * </p>
     *
     * @param request 包含用户名和密码的登录请求对象
     * @return JWT令牌字符串，用于后续接口认证
     */
    String login(LoginRequest request);
    
    /**
     * 用户注册
     * <p>
     * 创建新用户并分配基本角色
     * </p>
     *
     * @param request 包含用户注册信息的请求对象
     */
    void register(RegisterRequest request);
    
    /**
     * 获取当前登录用户
     * <p>
     * 根据当前上下文中的认证信息获取用户实体
     * </p>
     *
     * @return 当前登录用户实体
     */
    User getCurrentUser();
    
    /**
     * 创建用户
     * <p>
     * 管理员创建新用户
     * </p>
     *
     * @param user 用户实体对象，包含用户信息
     */
    void createUser(User user);
    
    /**
     * 更新用户信息
     * <p>
     * 更新已有用户的基本信息
     * </p>
     *
     * @param user 包含更新后用户信息的用户实体
     */
    void updateUser(User user);
    
    /**
     * 删除用户
     * <p>
     * 根据用户ID删除用户记录
     * </p>
     *
     * @param id 要删除的用户ID
     */
    void deleteUser(Long id);
    
    /**
     * 获取用户信息
     * <p>
     * 根据用户ID查询用户详细信息
     * </p>
     *
     * @param id 用户ID
     * @return 用户实体对象
     */
    User getUser(Long id);
    
    /**
     * 分页查询用户列表
     * <p>
     * 支持按多个条件筛选用户，返回用户分页数据
     * </p>
     *
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @param username 用户名（可选，模糊查询）
     * @param nickname 昵称（可选，模糊查询）
     * @param email 邮箱（可选，模糊查询）
     * @param phone 手机号（可选，模糊查询）
     * @param status 状态（可选，精确查询）
     * @return 用户DTO对象的分页结果
     */
    Page<UserDTO> listUsers(Integer pageNum, Integer pageSize, String username, String nickname, String email, String phone, Integer status);
    
    /**
     * 更新用户状态
     * <p>
     * 启用或禁用指定用户
     * </p>
     *
     * @param id 用户ID
     * @param status 用户状态（0-禁用，1-启用）
     */
    void updateUserStatus(Long id, Integer status);
    
    /**
     * 分配用户角色
     * <p>
     * 设置用户拥有的角色，会清除原有角色并重新分配
     * </p>
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void assignRoles(Long userId, List<Long> roleIds);
    
    /**
     * 获取用户角色ID列表
     * <p>
     * 查询指定用户已分配的所有角色ID
     * </p>
     *
     * @param userId 用户ID
     * @return 用户拥有的角色ID列表
     */
    List<Long> getUserRoleIds(Long userId);
} 