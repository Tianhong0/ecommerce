package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long categoryId;
    
    private String name;
    
    private String description;
    
    private String mainImage;
    
    private BigDecimal price;
    
    private Integer stock;
    
    private Integer status; // 0-下架 1-上架
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 