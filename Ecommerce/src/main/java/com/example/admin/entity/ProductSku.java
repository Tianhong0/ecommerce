package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product_sku")
public class ProductSku {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long productId;
    
    private String skuCode;
    
    private String name; // SKU名称
    
    private String attributes; // JSON格式存储规格属性
    
    private BigDecimal price;
    
    private Integer stock;
    
    private Integer status; // 0-禁用 1-启用
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 