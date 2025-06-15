package com.example.admin.service;

import com.example.admin.dto.message.LogMessage;
import com.example.admin.dto.message.OrderMessage;
import com.example.admin.dto.message.StockMessage;

/**
 * Kafka生产者服务接口
 */
public interface KafkaProducerService {
    /**
     * 发送订单消息
     * @param topic 主题
     * @param orderMessage 订单消息
     */
    void sendOrderMessage(String topic, OrderMessage orderMessage);
    
    /**
     * 发送库存消息
     * @param topic 主题
     * @param stockMessage 库存消息
     */
    void sendStockMessage(String topic, StockMessage stockMessage);
    
    /**
     * 发送日志消息
     * @param topic 主题
     * @param logMessage 日志消息
     */
    void sendLogMessage(String topic, LogMessage logMessage);
} 