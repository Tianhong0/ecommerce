package com.example.admin.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRefundDTO {
    private Long id;
    
    private Long orderId;
    
    private String refundNo;
    
    private BigDecimal refundAmount;
    
    private Integer status;
    
    private String reason;
    
    private String description;
    
    private String proofImages;
    
    private String rejectReason;
    
    private LocalDateTime applyTime;
    
    private LocalDateTime handleTime;
    
    private LocalDateTime completeTime;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 