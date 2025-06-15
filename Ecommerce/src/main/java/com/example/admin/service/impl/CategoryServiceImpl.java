package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.CategoryDTO;
import com.example.admin.entity.Category;
import com.example.admin.mapper.CategoryMapper;
import com.example.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryMapper categoryMapper;
    
    @Override
    @Transactional
    public Category create(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setSort(categoryDTO.getSort());
        category.setStatus(categoryDTO.getStatus());
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        
        categoryMapper.insert(category);
        return category;
    }
    
    @Override
    @Transactional
    public Category update(Long id, CategoryDTO categoryDTO) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setSort(categoryDTO.getSort());
        category.setStatus(categoryDTO.getStatus());
        category.setUpdateTime(LocalDateTime.now());
        
        categoryMapper.updateById(category);
        return category;
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
    
    @Override
    public Category getById(Long id) {
        return categoryMapper.selectById(id);
    }
    
    @Override
    public Page<Category> list(Integer page, Integer size, String name) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Category::getName, name.trim());
        }
        wrapper.orderByAsc(Category::getSort);
        
        Page<Category> categoryPage = new Page<>(page, size);
        categoryPage = categoryMapper.selectPage(categoryPage, wrapper);
        
        return categoryPage;
    }
    
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateById(category);
    }
} 