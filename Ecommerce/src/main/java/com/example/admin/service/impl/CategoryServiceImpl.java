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

/**
 * 商品分类服务实现类
 * <p>
 * 实现{@link CategoryService}接口，提供商品分类的全部业务逻辑，包括：
 * - 分类的创建、更新、删除和查询功能
 * - 分类状态管理
 * - 分页查询与条件过滤
 * </p>
 * <p>
 * 该实现采用以下技术和策略：
 * - 使用MyBatis-Plus进行数据访问
 * - 结合Redis缓存提高查询性能
 * - 使用事务注解确保数据一致性
 * - 实现缓存与数据库的双写一致性策略
 * </p>
 *
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 * @see CategoryService 分类服务接口
 * @see Category 分类实体类
 * @see CategoryMapper 分类数据访问接口
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    /**
     * 分类数据访问对象
     * 注入的CategoryMapper实例，用于操作分类数据表
     */
    private final CategoryMapper categoryMapper;
    
    /**
     * 缓存工具类
     * 封装了对Redis缓存的操作，用于提高查询性能
     */
    private final CacheUtil cacheUtil;
    
    /**
     * 创建商品分类
     * <p>
     * 将DTO中的数据转换为实体对象，设置创建和更新时间，
     * 然后保存到数据库中。该方法在事务中执行，确保数据一致性。
     * </p>
     * 
     * @param categoryDTO 分类数据传输对象，包含分类名称、描述等信息
     * @return 创建后的分类实体，包含自动生成的ID
     */
    @Override
    @Transactional
    public Category create(CategoryDTO categoryDTO) {
        // 创建分类实体并设置属性
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setSort(categoryDTO.getSort());
        category.setStatus(categoryDTO.getStatus());
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        
        // 保存到数据库
        categoryMapper.insert(category);
        return category;
    }
    
    /**
     * 更新商品分类
     * <p>
     * 根据ID查询分类，更新其属性，并同步到数据库。
     * 同时删除相关缓存，保证缓存与数据库的一致性。
     * 如果分类不存在，则抛出业务异常。
     * </p>
     * 
     * @param id 分类ID，用于标识要更新的分类
     * @param categoryDTO 更新后的分类数据，包含分类的新属性值
     * @return 更新后的分类实体
     * @throws BusinessException 当指定ID的分类不存在时抛出
     */
    @Override
    @Transactional
    public Category update(Long id, CategoryDTO categoryDTO) {
        // 检查分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 更新分类属性
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setSort(categoryDTO.getSort());
        category.setStatus(categoryDTO.getStatus());
        category.setUpdateTime(LocalDateTime.now());
        
        // 更新数据库
        categoryMapper.updateById(category);
        
        // 更新缓存（删除旧缓存，后续访问时会重新生成）
        cacheUtil.deleteCache(cacheUtil.getCategoryKey(id));
        
        return category;
    }
    
    /**
     * 删除商品分类
     * <p>
     * 根据ID从数据库中删除分类，并删除相关缓存。
     * 采用物理删除而非逻辑删除，会彻底移除数据。
     * </p>
     * 
     * @param id 要删除的分类ID
     */
    @Override
    @Transactional
    public void delete(Long id) {
        // 从数据库删除
        categoryMapper.deleteById(id);
        
        // 删除缓存
        cacheUtil.deleteCache(cacheUtil.getCategoryKey(id));
    }
    
    /**
     * 根据ID获取分类详情
     * <p>
     * 实现了"缓存优先"策略：
     * 1. 先尝试从缓存获取数据
     * 2. 如缓存未命中，则从数据库查询
     * 3. 将数据库查询结果写入缓存
     * 这种策略能显著提高频繁访问数据的响应速度
     * </p>
     * 
     * @param id 分类ID
     * @return 分类实体对象，如果不存在则返回null
     */
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
    
    /**
     * 分页查询分类列表
     * <p>
     * 支持按名称模糊查询，并按照分类的sort字段升序排序。
     * 查询结果会被缓存，提高后续访问性能。
     * </p>
     * 
     * @param page 页码，从1开始
     * @param size 每页记录数
     * @param name 分类名称（可选，用于模糊查询）
     * @return 分类分页对象，包含分页信息和当前页的分类列表
     */
    @Override
    public Page<Category> list(Integer page, Integer size, String name) {
        // 构建查询条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Category::getName, name.trim());
        }
        // 按排序字段升序排列
        wrapper.orderByAsc(Category::getSort);
        
        // 执行分页查询
        Page<Category> categoryPage = new Page<>(page, size);
        categoryPage = categoryMapper.selectPage(categoryPage, wrapper);
        
        // 将结果添加到缓存
        for (Category category : categoryPage.getRecords()) {
            cacheUtil.setCache(cacheUtil.getCategoryKey(category.getId()), category);
        }
        
        return categoryPage;
    }
    
    /**
     * 更新分类状态
     * <p>
     * 允许启用或禁用指定分类，同时更新最后修改时间。
     * 如果分类不存在，则抛出业务异常。
     * 更新后会删除相关缓存，确保数据一致性。
     * </p>
     * 
     * @param id 分类ID
     * @param status 新状态值（1-启用，0-禁用）
     * @throws BusinessException 当指定ID的分类不存在时抛出
     */
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        // 检查分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 更新状态
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateById(category);
        
        // 更新缓存（删除旧缓存）
        cacheUtil.deleteCache(cacheUtil.getCategoryKey(id));
    }
} 