package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_delivery")
public class OrderDelivery {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long orderId; // 订单ID
    
    private String deliveryCompany; // 物流公司
    
    private String deliverySn; // 物流单号
    
    private String receiverName; // 收货人姓名
    
    private String receiverPhone; // 收货人电话
    
    private String receiverAddress; // 收货地址
    
    private Integer status; // 物流状态：0-待发货 1-已发货 2-已签收
    
    private String deliveryInfo; // 物流信息（JSON格式）
    
    private LocalDateTime deliveryTime; // 发货时间
    
    private LocalDateTime receiveTime; // 签收时间
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 