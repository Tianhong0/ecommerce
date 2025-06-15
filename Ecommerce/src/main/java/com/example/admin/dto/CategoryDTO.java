package com.example.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private String description;
    
    @NotNull(message = "排序不能为空")
    private Integer sort;
    
    @NotNull(message = "状态不能为空")
    private Integer status;
} 