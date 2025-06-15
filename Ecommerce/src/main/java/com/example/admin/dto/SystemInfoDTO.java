package com.example.admin.dto;

import lombok.Data;

@Data
public class SystemInfoDTO {
    private String osName; // 操作系统名称
    private String osVersion; // 操作系统版本
    private String osArch; // 操作系统架构
    private String javaVersion; // Java版本
    private String javaVendor; // Java供应商
    private String javaHome; // Java安装路径
    private String userDir; // 用户目录
    private String userHome; // 用户主目录
    private String userName; // 用户名
} 