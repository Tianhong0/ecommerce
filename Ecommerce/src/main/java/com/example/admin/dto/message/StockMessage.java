package com.example.admin.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 库存消息模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMessage {
    private Long productId;
    private Long skuId;
    private String productName;
    private String skuName;
    private Integer quantity;
    private String orderNo;
    private Long orderId;
    private LocalDateTime operateTime;
    private String operationType; // DEDUCT:扣减, RETURN:归还, ALERT:预警
} 