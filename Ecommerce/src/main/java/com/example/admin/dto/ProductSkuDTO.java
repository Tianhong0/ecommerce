package com.example.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSkuDTO {
    private Long id;
    
    private Long productId;
    
    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;
    
    @NotBlank(message = "SKU名称不能为空")
    private String name;
    
    @NotBlank(message = "规格属性不能为空")
    private String attributes;
    
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price;
    
    @NotNull(message = "库存不能为空")
    @Positive(message = "库存必须大于0")
    private Integer stock;
    
    @NotNull(message = "状态不能为空")
    private Integer status;
} 