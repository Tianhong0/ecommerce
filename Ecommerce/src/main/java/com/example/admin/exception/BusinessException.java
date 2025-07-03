package com.example.admin.exception;

/**
 * 业务异常类
 * <p>
 * 表示在业务逻辑处理过程中发生的异常情况。
 * 这类异常通常是由于用户操作不当、数据状态异常或业务规则限制导致的，
 * 如资源不存在、数据重复、权限不足等。
 * </p>
 * <p>
 * 与系统异常不同，业务异常通常不表示系统出现了错误，
 * 而是表示业务流程中出现了预期内的异常情况，需要向用户展示明确的错误信息。
 * 在全局异常处理器中，业务异常通常会被转换为友好的错误消息返回给前端。
 * </p>
 * <p>
 * 该异常继承自RuntimeException，因此是一个非受检异常，
 * 不需要在方法签名中声明throws子句。
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 * @see GlobalExceptionHandler 全局异常处理器，用于处理此类异常
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 构造一个包含错误信息的业务异常
     * <p>
     * 用于简单地指出业务处理中的错误原因
     * </p>
     *
     * @param message 描述业务错误的信息
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * 构造一个包含错误信息和原因异常的业务异常
     * <p>
     * 用于在业务处理中封装底层异常，同时提供更清晰的业务错误描述
     * </p>
     *
     * @param message 描述业务错误的信息
     * @param cause 导致此业务异常的原始异常
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 