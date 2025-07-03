package com.example.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求数据传输对象
 * <p>
 * 用于接收前端登录请求的数据。包含登录所需的用户名和密码。
 * 使用Jakarta Validation API进行数据校验，确保必填字段不为空。
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
@Data
public class LoginRequest {
    /**
     * 用户名
     * 不能为空，用于用户身份识别
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 用户密码
     * 不能为空，用于验证用户身份
     */
    @NotBlank(message = "密码不能为空")
    private String password;
} 