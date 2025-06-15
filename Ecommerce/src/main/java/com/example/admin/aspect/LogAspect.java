package com.example.admin.aspect;

import com.example.admin.constant.KafkaConstant;
import com.example.admin.dto.message.LogMessage;
import com.example.admin.entity.User;
import com.example.admin.service.KafkaProducerService;
import com.example.admin.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 日志切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final KafkaProducerService kafkaProducerService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    /**
     * 定义切点 - 所有controller包下的方法
     */
    @Pointcut("execution(* com.example.admin.controller.*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 方法执行后记录日志
     */
    @AfterReturning(value = "controllerPointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        try {
            // 记录用户操作日志
            recordUserActionLog(joinPoint, result, null);
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }
    }

    /**
     * 方法抛出异常后记录日志
     */
    @AfterThrowing(value = "controllerPointcut()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex) {
        try {
            // 记录错误日志
            recordErrorLog(joinPoint, ex);
        } catch (Exception e) {
            log.error("记录错误日志失败", e);
        }
    }

    /**
     * 记录用户操作日志
     */
    private void recordUserActionLog(JoinPoint joinPoint, Object result, Exception ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        User currentUser = userService.getCurrentUser();

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        // 构建日志消息
        LogMessage logMessage = LogMessage.builder()
                .logType("USER_ACTION")
                .module(className)
                .action(methodName)
                .userId(currentUser != null ? currentUser.getId() : null)
                .username(currentUser != null ? currentUser.getUsername() : "anonymous")
                .ip(getIpAddress(request))
                .userAgent(request.getHeader("User-Agent"))
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .requestParams(getRequestParams(joinPoint))
                .responseData(getResponseData(result))
                .operateTime(LocalDateTime.now())
                .build();

        // 发送日志消息
        kafkaProducerService.sendLogMessage(KafkaConstant.TOPIC_LOG_USER_ACTION, logMessage);
    }

    /**
     * 记录错误日志
     */
    private void recordErrorLog(JoinPoint joinPoint, Exception ex) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        User currentUser = userService.getCurrentUser();

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        // 构建日志消息
        LogMessage logMessage = LogMessage.builder()
                .logType("ERROR")
                .module(className)
                .action(methodName)
                .userId(currentUser != null ? currentUser.getId() : null)
                .username(currentUser != null ? currentUser.getUsername() : "anonymous")
                .ip(getIpAddress(request))
                .userAgent(request.getHeader("User-Agent"))
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .requestParams(getRequestParams(joinPoint))
                .errorMessage(ex.getMessage())
                .stackTrace(getStackTrace(ex))
                .operateTime(LocalDateTime.now())
                .build();

        // 发送日志消息
        kafkaProducerService.sendLogMessage(KafkaConstant.TOPIC_LOG_ERROR, logMessage);
    }

    /**
     * 获取请求参数
     */
    private String getRequestParams(JoinPoint joinPoint) {
        try {
            return objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (JsonProcessingException e) {
            return Arrays.toString(joinPoint.getArgs());
        }
    }

    /**
     * 获取响应数据
     */
    private String getResponseData(Object result) {
        if (result == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return result.toString();
        }
    }

    /**
     * 获取异常堆栈信息
     */
    private String getStackTrace(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\n");
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append("\tat ").append(element).append("\n");
            if (sb.length() > 1000) {
                sb.append("...");
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 获取客户端IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}