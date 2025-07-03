package com.example.admin.constant;

/**
 * Kafka主题与消费者组常量类
 * <p>
 * 定义系统中使用的所有Kafka主题和消费者组常量。
 * 集中管理这些常量有助于统一命名规范，避免重复定义，
 * 并使得主题名称在代码中的使用更加清晰和一致。
 * </p>
 * <p>
 * 主题按照业务领域分组：订单、库存和日志。
 * 遵循命名规范：topic-{domain}-{action}
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
public class KafkaConstant {
    // 订单相关主题
    /**
     * 订单创建主题
     * 用于发布订单创建事件，触发库存检查、日志记录等后续处理
     */
    public static final String TOPIC_ORDER_CREATE = "topic-order-create";
    
    /**
     * 订单支付主题
     * 用于发布订单支付事件，触发发货准备、通知商家等后续处理
     */
    public static final String TOPIC_ORDER_PAY = "topic-order-pay";
    
    /**
     * 订单发货主题
     * 用于发布订单发货事件，触发物流追踪、通知客户等后续处理
     */
    public static final String TOPIC_ORDER_DELIVERY = "topic-order-delivery";
    
    /**
     * 订单完成主题
     * 用于发布订单完成事件，触发评价提醒、售后服务等后续处理
     */
    public static final String TOPIC_ORDER_COMPLETE = "topic-order-complete";
    
    /**
     * 订单取消主题
     * 用于发布订单取消事件，触发库存返还、退款处理等后续处理
     */
    public static final String TOPIC_ORDER_CANCEL = "topic-order-cancel";
    
    // 库存相关主题
    /**
     * 库存扣减主题
     * 用于发布库存扣减事件，确保商品库存实时更新
     */
    public static final String TOPIC_STOCK_DEDUCT = "topic-stock-deduct";
    
    /**
     * 库存返还主题
     * 用于发布库存返还事件，处理订单取消或退货时的库存恢复
     */
    public static final String TOPIC_STOCK_RETURN = "topic-stock-return";
    
    /**
     * 库存预警主题
     * 用于发布库存不足预警事件，提醒商家及时补货
     */
    public static final String TOPIC_STOCK_ALERT = "topic-stock-alert";
    
    // 日志相关主题
    /**
     * 用户行为日志主题
     * 用于记录用户操作行为，如登录、下单、支付等
     */
    public static final String TOPIC_LOG_USER_ACTION = "topic-log-user-action";
    
    /**
     * 系统日志主题
     * 用于记录系统运行状态、性能指标等信息
     */
    public static final String TOPIC_LOG_SYSTEM = "topic-log-system";
    
    /**
     * 错误日志主题
     * 用于记录系统错误和异常信息，便于问题排查
     */
    public static final String TOPIC_LOG_ERROR = "topic-log-error";
    
    // 消费者组
    /**
     * 订单消费者组
     * 处理所有与订单相关的消息
     */
    public static final String GROUP_ORDER = "group-order";
    
    /**
     * 库存消费者组
     * 处理所有与库存相关的消息
     */
    public static final String GROUP_STOCK = "group-stock";
    
    /**
     * 日志消费者组
     * 处理所有与日志相关的消息
     */
    public static final String GROUP_LOG = "group-log";
} 