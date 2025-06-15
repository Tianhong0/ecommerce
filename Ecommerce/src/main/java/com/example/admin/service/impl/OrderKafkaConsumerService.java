package com.example.admin.service.impl;

import com.example.admin.constant.KafkaConstant;
import com.example.admin.dto.message.OrderMessage;
import com.example.admin.entity.Order;
import com.example.admin.mapper.OrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 订单Kafka消费者服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderKafkaConsumerService {

    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 处理订单支付消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_ORDER_PAY, groupId = KafkaConstant.GROUP_ORDER)
    @Transactional
    public void handleOrderPayMessage(String message) {
        try {
            log.info("接收到订单支付消息: {}", message);
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
            
            // 更新订单状态为待发货
            Order order = orderMapper.selectById(orderMessage.getId());
            if (order != null && order.getStatus() == 0) { // 待付款状态
                order.setStatus(1); // 待发货
                order.setPayTime(LocalDateTime.now());
                order.setUpdateTime(LocalDateTime.now());
                orderMapper.updateById(order);
                log.info("订单状态更新为待发货: orderId={}, orderNo={}", order.getId(), order.getOrderNo());
            } else {
                log.warn("订单状态更新失败，订单不存在或状态不是待付款: orderId={}", orderMessage.getId());
            }
        } catch (JsonProcessingException e) {
            log.error("处理订单支付消息失败", e);
        }
    }

    /**
     * 处理订单发货消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_ORDER_DELIVERY, groupId = KafkaConstant.GROUP_ORDER)
    @Transactional
    public void handleOrderDeliveryMessage(String message) {
        try {
            log.info("接收到订单发货消息: {}", message);
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
            
            // 更新订单状态为待收货
            Order order = orderMapper.selectById(orderMessage.getId());
            if (order != null && order.getStatus() == 1) { // 待发货状态
                order.setStatus(2); // 待收货
                order.setDeliveryTime(LocalDateTime.now());
                order.setUpdateTime(LocalDateTime.now());
                orderMapper.updateById(order);
                log.info("订单状态更新为待收货: orderId={}, orderNo={}", order.getId(), order.getOrderNo());
            } else {
                log.warn("订单状态更新失败，订单不存在或状态不是待发货: orderId={}", orderMessage.getId());
            }
        } catch (JsonProcessingException e) {
            log.error("处理订单发货消息失败", e);
        }
    }

    /**
     * 处理订单完成消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_ORDER_COMPLETE, groupId = KafkaConstant.GROUP_ORDER)
    @Transactional
    public void handleOrderCompleteMessage(String message) {
        try {
            log.info("接收到订单完成消息: {}", message);
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
            
            // 更新订单状态为已完成
            Order order = orderMapper.selectById(orderMessage.getId());
            if (order != null && order.getStatus() == 2) { // 待收货状态
                order.setStatus(3); // 已完成
                order.setReceiveTime(LocalDateTime.now());
                order.setUpdateTime(LocalDateTime.now());
                orderMapper.updateById(order);
                log.info("订单状态更新为已完成: orderId={}, orderNo={}", order.getId(), order.getOrderNo());
            } else {
                log.warn("订单状态更新失败，订单不存在或状态不是待收货: orderId={}", orderMessage.getId());
            }
        } catch (JsonProcessingException e) {
            log.error("处理订单完成消息失败", e);
        }
    }

    /**
     * 处理订单取消消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_ORDER_CANCEL, groupId = KafkaConstant.GROUP_ORDER)
    @Transactional
    public void handleOrderCancelMessage(String message) {
        try {
            log.info("接收到订单取消消息: {}", message);
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
            
            // 更新订单状态为已取消
            Order order = orderMapper.selectById(orderMessage.getId());
            if (order != null && (order.getStatus() == 0 || order.getStatus() == 1)) { // 待付款或待发货状态
                order.setStatus(4); // 已取消
                order.setUpdateTime(LocalDateTime.now());
                orderMapper.updateById(order);
                log.info("订单状态更新为已取消: orderId={}, orderNo={}", order.getId(), order.getOrderNo());
            } else {
                log.warn("订单状态更新失败，订单不存在或状态不允许取消: orderId={}", orderMessage.getId());
            }
        } catch (JsonProcessingException e) {
            log.error("处理订单取消消息失败", e);
        }
    }
} 