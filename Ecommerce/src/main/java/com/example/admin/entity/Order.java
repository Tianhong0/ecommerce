package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo; // 订单编号
    
    private BigDecimal totalAmount; // 订单总金额
    
    private BigDecimal payAmount; // 实付金额
    
    private BigDecimal freightAmount; // 运费金额
    
    private Integer payType; // 支付方式：1-支付宝 2-微信 3-银联
    
    private Integer status; // 订单状态：0-待付款 1-待发货 2-待收货 3-已完成 4-已取消 5-已退款
    
    private String receiverName; // 收货人姓名
    
    private String receiverPhone; // 收货人电话
    
    private String receiverAddress; // 收货地址
    
    private String note; // 订单备注
    
    private LocalDateTime payTime; // 支付时间
    
    private LocalDateTime deliveryTime; // 发货时间
    
    private LocalDateTime receiveTime; // 收货时间
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 