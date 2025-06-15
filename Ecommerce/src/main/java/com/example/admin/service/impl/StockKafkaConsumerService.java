package com.example.admin.service.impl;

import com.example.admin.constant.KafkaConstant;
import com.example.admin.dto.message.StockMessage;
import com.example.admin.entity.Product;
import com.example.admin.entity.ProductSku;
import com.example.admin.mapper.ProductMapper;
import com.example.admin.mapper.ProductSkuMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存Kafka消费者服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockKafkaConsumerService {

    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;
    private final KafkaProducerServiceImpl kafkaProducerService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 处理库存扣减消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_STOCK_DEDUCT, groupId = KafkaConstant.GROUP_STOCK)
    @Transactional
    public void handleStockDeductMessage(String message) {
        try {
            log.info("接收到库存扣减消息: {}", message);
            StockMessage stockMessage = objectMapper.readValue(message, StockMessage.class);
            
            // 扣减商品库存
            if (stockMessage.getSkuId() != null) {
                // 扣减SKU库存
                ProductSku sku = productSkuMapper.selectById(stockMessage.getSkuId());
                if (sku != null) {
                    int newStock = sku.getStock() - stockMessage.getQuantity();
                    if (newStock < 0) {
                        log.error("SKU库存不足: skuId={}, currentStock={}, deductQuantity={}", 
                                stockMessage.getSkuId(), sku.getStock(), stockMessage.getQuantity());
                        return;
                    }
                    
                    sku.setStock(newStock);
                    productSkuMapper.updateById(sku);
                    log.info("SKU库存扣减成功: skuId={}, newStock={}", stockMessage.getSkuId(), newStock);
                    
                    // 检查是否需要发送库存预警
                    if (newStock < 10) {
                        sendStockAlert(stockMessage.getProductId(), stockMessage.getSkuId(), newStock);
                    }
                } else {
                    log.error("SKU不存在: skuId={}", stockMessage.getSkuId());
                }
            }
            
            // 扣减商品总库存
            Product product = productMapper.selectById(stockMessage.getProductId());
            if (product != null) {
                int newStock = product.getStock() - stockMessage.getQuantity();
                if (newStock < 0) {
                    log.error("商品库存不足: productId={}, currentStock={}, deductQuantity={}", 
                            stockMessage.getProductId(), product.getStock(), stockMessage.getQuantity());
                    return;
                }
                
                product.setStock(newStock);
                productMapper.updateById(product);
                log.info("商品库存扣减成功: productId={}, newStock={}", stockMessage.getProductId(), newStock);
                
                // 检查是否需要发送库存预警
                if (newStock < 20) {
                    sendStockAlert(stockMessage.getProductId(), null, newStock);
                }
            } else {
                log.error("商品不存在: productId={}", stockMessage.getProductId());
            }
        } catch (JsonProcessingException e) {
            log.error("处理库存扣减消息失败", e);
        }
    }

    /**
     * 处理库存归还消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_STOCK_RETURN, groupId = KafkaConstant.GROUP_STOCK)
    @Transactional
    public void handleStockReturnMessage(String message) {
        try {
            log.info("接收到库存归还消息: {}", message);
            StockMessage stockMessage = objectMapper.readValue(message, StockMessage.class);
            
            // 归还SKU库存
            if (stockMessage.getSkuId() != null) {
                ProductSku sku = productSkuMapper.selectById(stockMessage.getSkuId());
                if (sku != null) {
                    int newStock = sku.getStock() + stockMessage.getQuantity();
                    sku.setStock(newStock);
                    productSkuMapper.updateById(sku);
                    log.info("SKU库存归还成功: skuId={}, newStock={}", stockMessage.getSkuId(), newStock);
                } else {
                    log.error("SKU不存在: skuId={}", stockMessage.getSkuId());
                }
            }
            
            // 归还商品总库存
            Product product = productMapper.selectById(stockMessage.getProductId());
            if (product != null) {
                int newStock = product.getStock() + stockMessage.getQuantity();
                product.setStock(newStock);
                productMapper.updateById(product);
                log.info("商品库存归还成功: productId={}, newStock={}", stockMessage.getProductId(), newStock);
            } else {
                log.error("商品不存在: productId={}", stockMessage.getProductId());
            }
        } catch (JsonProcessingException e) {
            log.error("处理库存归还消息失败", e);
        }
    }
    
    /**
     * 发送库存预警消息
     */
    private void sendStockAlert(Long productId, Long skuId, int currentStock) {
        StockMessage alertMessage = StockMessage.builder()
                .productId(productId)
                .skuId(skuId)
                .quantity(currentStock)
                .operationType("ALERT")
                .build();
        
        kafkaProducerService.sendStockMessage(KafkaConstant.TOPIC_STOCK_ALERT, alertMessage);
        log.info("发送库存预警消息: productId={}, skuId={}, currentStock={}", productId, skuId, currentStock);
    }
} 