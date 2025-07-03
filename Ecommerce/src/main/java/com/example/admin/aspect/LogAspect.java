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
 * 日志记录切面
 * <p>
 * 通过AOP方式实现系统操作日志和错误日志的自动记录，主要功能包括：
 * - 拦截所有控制器方法的调用
 * - 记录用户操作日志，包括请求信息、用户信息和操作结果
 * - 捕获并记录系统异常，包括异常类型、堆栈信息等
 * - 通过Kafka消息队列异步存储日志信息，减少对主业务流程的影响
 * </p>
 * <p>
 * 该切面使用@AfterReturning和@AfterThrowing注解，分别处理方法正常返回和
 * 异常情况，确保系统行为被完整记录，便于问题排查和用户行为分析。
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 * @see Aspect Spring AOP切面
 * @see KafkaProducerService Kafka消息生产者服务
 * @see LogMessage 日志消息数据传输对象
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    /**
     * Kafka消息生产者服务
     * 用于发送日志消息到Kafka队列
     */
    private final KafkaProducerService kafkaProducerService;
    
    /**
     * 用户服务
     * 用于获取当前登录用户信息
     */
    private final UserService userService;
    
    /**
     * JSON对象映射器
     * 用于序列化请求参数和响应结果
     */
    private final ObjectMapper objectMapper;

    /**
     * 定义切点 - 拦截所有controller包下的方法
     * <p>
     * 该切点表达式匹配com.example.admin.controller包及其所有子包中的所有方法。
     * 这意味着系统中所有控制器的API调用都会被记录。
     * </p>
     */
    @Pointcut("execution(* com.example.admin.controller.*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 方法正常返回后记录操作日志
     * <p>
     * 在控制器方法成功执行并返回结果后触发，记录用户操作日志。
     * 该方法会捕获请求上下文、用户信息、请求参数和响应结果等信息，
     * 并通过Kafka异步发送到日志处理服务。
     * </p>
     *
     * @param joinPoint AOP连接点，包含被拦截方法的信息
     * @param result 方法执行结果
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
     * 方法抛出异常后记录错误日志
     * <p>
     * 在控制器方法执行过程中抛出异常时触发，记录详细的错误信息。
     * 该方法会捕获异常信息、堆栈跟踪以及请求上下文等信息，
     * 通过Kafka异步发送到错误日志处理服务，便于问题排查。
     * </p>
     *
     * @param joinPoint AOP连接点，包含被拦截方法的信息
     * @param ex 抛出的异常对象
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
     * <p>
     * 收集请求信息、用户信息和操作结果，构建日志消息对象，
     * 并通过Kafka消息队列发送到日志处理服务。
     * </p>
     *
     * @param joinPoint AOP连接点
     * @param result 方法执行结果
     * @param ex 可能的异常对象，正常情况下为null
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
     * 记录系统错误日志
     * <p>
     * 收集异常信息、堆栈跟踪、请求信息和用户信息，
     * 构建错误日志消息对象，并通过Kafka消息队列发送到错误日志处理服务。
     * </p>
     *
     * @param joinPoint AOP连接点
     * @param ex 捕获的异常对象
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
     * 获取请求参数的JSON字符串表示
     * <p>
     * 将方法参数序列化为JSON格式。如果序列化失败，
     * 则使用简单的字符串表示作为后备方案。
     * </p>
     *
     * @param joinPoint AOP连接点
     * @return 参数的JSON字符串表示
     */
    private String getRequestParams(JoinPoint joinPoint) {
        try {
            return objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (JsonProcessingException e) {
            return Arrays.toString(joinPoint.getArgs());
        }
    }

    /**
     * 获取响应数据的JSON字符串表示
     * <p>
     * 将方法返回结果序列化为JSON格式。如果序列化失败，
     * 则使用对象的toString方法作为后备方案。
     * </p>
     *
     * @param result 方法返回结果
     * @return 结果的JSON字符串表示，如果结果为null则返回null
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
     * 获取异常堆栈信息的字符串表示
     * <p>
     * 提取异常的堆栈跟踪信息，并限制长度以避免日志过大。
     * 如果堆栈信息过长，则截断并添加省略号。
     * </p>
     *
     * @param ex 异常对象
     * @return 格式化的堆栈跟踪字符串
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
     * 获取客户端真实IP地址
     * <p>
     * 考虑了各种代理服务器场景，尝试从不同的请求头中
     * 提取客户端的真实IP地址。
     * </p>
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
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