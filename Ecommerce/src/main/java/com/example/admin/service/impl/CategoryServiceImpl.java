package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.CategoryDTO;
import com.example.admin.entity.Category;
import com.example.admin.exception.BusinessException;
import com.example.admin.mapper.CategoryMapper;
import com.example.admin.service.CategoryService;
import com.example.admin.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryMapper categoryMapper;
    private final CacheUtil cacheUtil;
    
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
            throw new BusinessException("分类不存在");
        }
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setSort(categoryDTO.getSort());
        category.setStatus(categoryDTO.getStatus());
        category.setUpdateTime(LocalDateTime.now());
        
        categoryMapper.updateById(category);
        
        // 更新缓存
        cacheUtil.deleteCache(cacheUtil.getCategoryKey(id));
        
        return category;
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        categoryMapper.deleteById(id);
        
        // 删除缓存
        cacheUtil.deleteCache(cacheUtil.getCategoryKey(id));
    }
    
    @Override
    public Category getById(Long id) {
        // 先从缓存中获取
        String cacheKey = cacheUtil.getCategoryKey(id);
        Category category = cacheUtil.getCache(cacheKey, Category.class);
        
        // 缓存未命中，从数据库获取
        if (category == null) {
            category = categoryMapper.selectById(id);
            // 存入缓存
            if (category != null) {
                cacheUtil.setCache(cacheKey, category);
            }
        }
        
        return category;
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
        
        // 将结果添加到缓存
        for (Category category : categoryPage.getRecords()) {
            cacheUtil.setCache(cacheUtil.getCategoryKey(category.getId()), category);
        }
        
        return categoryPage;
    }
    
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateById(category);
        
        // 更新缓存
        cacheUtil.deleteCache(cacheUtil.getCategoryKey(id));
    }
} 