package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long orderId; // 订单ID
    
    private Long productId; // 商品ID
    
    private Long skuId; // SKU ID
    
    private String productName; // 商品名称
    
    private String skuName; // SKU名称
    
    private String productPic; // 商品图片
    
    private BigDecimal price; // 商品单价
    
    private Integer quantity; // 购买数量
    
    private BigDecimal totalAmount; // 总金额
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 