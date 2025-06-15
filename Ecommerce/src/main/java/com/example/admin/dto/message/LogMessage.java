package com.example.admin.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 日志消息模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {
    private String logType; // USER_ACTION, SYSTEM, ERROR
    private String module;
    private String action;
    private Long userId;
    private String username;
    private String ip;
    private String userAgent;
    private String requestUrl;
    private String requestMethod;
    private String requestParams;
    private String responseData;
    private String errorMessage;
    private String stackTrace;
    private LocalDateTime operateTime;
} 