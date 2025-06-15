package com.example.admin.config;

import com.example.admin.constant.KafkaConstant;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka配置类
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Kafka管理员客户端配置
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /**
     * 订单创建主题
     */
    @Bean
    public NewTopic orderCreateTopic() {
        return new NewTopic(KafkaConstant.TOPIC_ORDER_CREATE, 3, (short) 1);
    }

    /**
     * 订单支付主题
     */
    @Bean
    public NewTopic orderPayTopic() {
        return new NewTopic(KafkaConstant.TOPIC_ORDER_PAY, 3, (short) 1);
    }

    /**
     * 订单发货主题
     */
    @Bean
    public NewTopic orderDeliveryTopic() {
        return new NewTopic(KafkaConstant.TOPIC_ORDER_DELIVERY, 3, (short) 1);
    }

    /**
     * 订单完成主题
     */
    @Bean
    public NewTopic orderCompleteTopic() {
        return new NewTopic(KafkaConstant.TOPIC_ORDER_COMPLETE, 3, (short) 1);
    }

    /**
     * 订单取消主题
     */
    @Bean
    public NewTopic orderCancelTopic() {
        return new NewTopic(KafkaConstant.TOPIC_ORDER_CANCEL, 3, (short) 1);
    }

    /**
     * 库存扣减主题
     */
    @Bean
    public NewTopic stockDeductTopic() {
        return new NewTopic(KafkaConstant.TOPIC_STOCK_DEDUCT, 3, (short) 1);
    }

    /**
     * 库存归还主题
     */
    @Bean
    public NewTopic stockReturnTopic() {
        return new NewTopic(KafkaConstant.TOPIC_STOCK_RETURN, 3, (short) 1);
    }

    /**
     * 库存预警主题
     */
    @Bean
    public NewTopic stockAlertTopic() {
        return new NewTopic(KafkaConstant.TOPIC_STOCK_ALERT, 3, (short) 1);
    }

    /**
     * 用户行为日志主题
     */
    @Bean
    public NewTopic userActionLogTopic() {
        return new NewTopic(KafkaConstant.TOPIC_LOG_USER_ACTION, 3, (short) 1);
    }

    /**
     * 系统日志主题
     */
    @Bean
    public NewTopic systemLogTopic() {
        return new NewTopic(KafkaConstant.TOPIC_LOG_SYSTEM, 3, (short) 1);
    }

    /**
     * 错误日志主题
     */
    @Bean
    public NewTopic errorLogTopic() {
        return new NewTopic(KafkaConstant.TOPIC_LOG_ERROR, 3, (short) 1);
    }
} 