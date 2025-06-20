package com.example.admin.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单消息模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage {
    private Long id;
    private String orderNo;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private LocalDateTime createTime;
    private List<OrderItemMessage> orderItems;
    
    /**
     * 订单项消息模型
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemMessage {
        private Long id;
        private Long productId;
        private Long skuId;
        private String productName;
        private String skuName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal totalAmount;
    }
} 