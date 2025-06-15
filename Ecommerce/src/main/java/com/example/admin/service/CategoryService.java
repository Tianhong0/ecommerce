package com.example.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.CategoryDTO;
import com.example.admin.entity.Category;

public interface CategoryService {
    Category create(CategoryDTO categoryDTO);
    
    Category update(Long id, CategoryDTO categoryDTO);
    
    void delete(Long id);
    
    Category getById(Long id);
    
    Page<Category> list(Integer page, Integer size, String name);
    
    void updateStatus(Long id, Integer status);
} 