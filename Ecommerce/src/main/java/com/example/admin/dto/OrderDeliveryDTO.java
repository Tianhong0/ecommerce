package com.example.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDeliveryDTO {
    private Long id;
    
    private Long orderId;
    
    private String deliveryCompany;
    
    private String deliverySn;
    
    private String receiverName;
    
    private String receiverPhone;
    
    private String receiverAddress;
    
    private Integer status;
    
    private String deliveryInfo;
    
    private LocalDateTime deliveryTime;
    
    private LocalDateTime receiveTime;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 