package com.example.admin.service.impl;

import com.example.admin.dto.message.LogMessage;
import com.example.admin.dto.message.OrderMessage;
import com.example.admin.dto.message.StockMessage;
import com.example.admin.service.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka生产者服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void sendOrderMessage(String topic, OrderMessage orderMessage) {
        try {
            String message = objectMapper.writeValueAsString(orderMessage);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, orderMessage.getOrderNo(), message);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("订单消息发送成功: topic={}, orderNo={}, partition={}, offset={}",
                            topic, orderMessage.getOrderNo(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                } else {
                    log.error("订单消息发送失败: topic={}, orderNo={}", topic, orderMessage.getOrderNo(), ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("订单消息序列化失败", e);
        }
    }

    @Override
    public void sendStockMessage(String topic, StockMessage stockMessage) {
        try {
            String message = objectMapper.writeValueAsString(stockMessage);
            String key = stockMessage.getProductId() + "-" + stockMessage.getSkuId();
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, message);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("库存消息发送成功: topic={}, productId={}, skuId={}, partition={}, offset={}",
                            topic, stockMessage.getProductId(), stockMessage.getSkuId(), 
                            result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                } else {
                    log.error("库存消息发送失败: topic={}, productId={}, skuId={}", 
                            topic, stockMessage.getProductId(), stockMessage.getSkuId(), ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("库存消息序列化失败", e);
        }
    }

    @Override
    public void sendLogMessage(String topic, LogMessage logMessage) {
        try {
            String message = objectMapper.writeValueAsString(logMessage);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("日志消息发送成功: topic={}, type={}, partition={}, offset={}",
                            topic, logMessage.getLogType(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                } else {
                    log.error("日志消息发送失败: topic={}, type={}", topic, logMessage.getLogType(), ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("日志消息序列化失败", e);
        }
    }
} 