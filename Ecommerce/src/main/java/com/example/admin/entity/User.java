package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * <p>
 * 对应数据库中的sys_user表，用于存储系统用户信息。
 * 包含用户基本信息、状态和时间戳信息。
 * 使用MyBatis-Plus的注解进行ORM映射。
 * </p>
 * 
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
@Data
@TableName("sys_user")
public class User {
    /**
     * 用户ID，自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，用于登录系统
     */
    private String username;

    /**
     * 密码，存储加密后的密码值
     */
    private String password;

    /**
     * 用户昵称，用于显示
     */
    private String nickname;

    /**
     * 用户电子邮箱
     */
    private String email;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户头像URL地址
     */
    private String avatar;

    /**
     * 用户状态
     * 0: 禁用
     * 1: 正常
     */
    private Integer status;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录最后更新时间
     */
    private LocalDateTime updateTime;
}
