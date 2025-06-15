package com.example.admin.constant;

/**
 * Kafka主题常量类
 */
public class KafkaConstant {
    // 订单相关主题
    public static final String TOPIC_ORDER_CREATE = "topic-order-create";
    public static final String TOPIC_ORDER_PAY = "topic-order-pay";
    public static final String TOPIC_ORDER_DELIVERY = "topic-order-delivery";
    public static final String TOPIC_ORDER_COMPLETE = "topic-order-complete";
    public static final String TOPIC_ORDER_CANCEL = "topic-order-cancel";
    
    // 库存相关主题
    public static final String TOPIC_STOCK_DEDUCT = "topic-stock-deduct";
    public static final String TOPIC_STOCK_RETURN = "topic-stock-return";
    public static final String TOPIC_STOCK_ALERT = "topic-stock-alert";
    
    // 日志相关主题
    public static final String TOPIC_LOG_USER_ACTION = "topic-log-user-action";
    public static final String TOPIC_LOG_SYSTEM = "topic-log-system";
    public static final String TOPIC_LOG_ERROR = "topic-log-error";
    
    // 消费者组
    public static final String GROUP_ORDER = "group-order";
    public static final String GROUP_STOCK = "group-stock";
    public static final String GROUP_LOG = "group-log";
} 