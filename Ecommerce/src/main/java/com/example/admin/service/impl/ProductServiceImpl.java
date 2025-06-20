package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.ProductDTO;
import com.example.admin.dto.ProductSkuDTO;
import com.example.admin.entity.Category;
import com.example.admin.entity.Product;
import com.example.admin.entity.ProductSku;
import com.example.admin.exception.BusinessException;
import com.example.admin.mapper.CategoryMapper;
import com.example.admin.mapper.ProductMapper;
import com.example.admin.mapper.ProductSkuMapper;
import com.example.admin.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.admin.util.CacheUtil;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;
    private final CategoryMapper categoryMapper;
    private final CacheUtil cacheUtil;
    
    @Override
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        // 检查分类状态
        Category category = cacheUtil.getCache(cacheUtil.getCategoryKey(productDTO.getCategoryId()), Category.class);
        if (category == null) {
            category = categoryMapper.selectById(productDTO.getCategoryId());
            if (category != null) {
                cacheUtil.setCache(cacheUtil.getCategoryKey(category.getId()), category);
            }
        }
        
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 检查分类状态，0表示禁用
        if (category.getStatus() == 0) {
            throw new BusinessException("该分类已禁用，不能添加商品");
        }
        
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        
        // 保存SKU信息并计算总库存
        if (productDTO.getSkuList() != null && !productDTO.getSkuList().isEmpty()) {
            int totalStock = 0;
            for (ProductSkuDTO skuDTO : productDTO.getSkuList()) {
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(skuDTO, sku);
                sku.setProductId(product.getId());
                sku.setCreateTime(LocalDateTime.now());
                sku.setUpdateTime(LocalDateTime.now());
                
                productSkuMapper.insert(sku);
                totalStock += sku.getStock();
            }
            product.setStock(totalStock);
        }
        
        productMapper.insert(product);
        
        return getById(product.getId());
    }
    
    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        // 检查分类状态
        Category category = cacheUtil.getCache(cacheUtil.getCategoryKey(productDTO.getCategoryId()), Category.class);
        if (category == null) {
            category = categoryMapper.selectById(productDTO.getCategoryId());
            if (category != null) {
                cacheUtil.setCache(cacheUtil.getCategoryKey(category.getId()), category);
            }
        }
        
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 检查分类状态，0表示禁用
        if (category.getStatus() == 0) {
            throw new BusinessException("该分类已禁用，不能更新商品");
        }
        
        BeanUtils.copyProperties(productDTO, product);
        product.setUpdateTime(LocalDateTime.now());
        
        // 更新SKU信息并重新计算总库存
        if (productDTO.getSkuList() != null && !productDTO.getSkuList().isEmpty()) {
            // 删除原有SKU
            LambdaQueryWrapper<ProductSku> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductSku::getProductId, id);
            productSkuMapper.delete(wrapper);
            
            // 添加新SKU并计算总库存
            int totalStock = 0;
            for (ProductSkuDTO skuDTO : productDTO.getSkuList()) {
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(skuDTO, sku);
                sku.setProductId(id);
                sku.setCreateTime(LocalDateTime.now());
                sku.setUpdateTime(LocalDateTime.now());
                
                productSkuMapper.insert(sku);
                totalStock += sku.getStock();
            }
            product.setStock(totalStock);
        }
        
        productMapper.updateById(product);
        
        // 清除相关缓存
        cacheUtil.clearProductCaches(id);
        
        return getById(id);
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        // 先删除SKU
        LambdaQueryWrapper<ProductSku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSku::getProductId, id);
        productSkuMapper.delete(skuWrapper);
        
        // 再删除商品
        productMapper.deleteById(id);
        
        // 清除相关缓存
        cacheUtil.clearProductCaches(id);
    }
    
    @Override
    public ProductDTO getById(Long id) {
        // 从缓存中获取商品
        String cacheKey = cacheUtil.getProductKey(id);
        Product product = cacheUtil.getCache(cacheKey, Product.class);
        
        // 缓存未命中，从数据库获取
        if (product == null) {
            product = productMapper.selectById(id);
            if (product == null) {
                return null;
            }
            // 存入缓存
            cacheUtil.setCache(cacheKey, product);
        }
        
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        
        // 获取SKU列表
        productDTO.setSkuList(getSkuList(id));
        
        return productDTO;
    }
    
    @Override
    public Page<ProductDTO> list(Integer page, Integer size, String name, Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            // 使用like进行模糊匹配，%name%表示包含name的任意位置
            wrapper.like(Product::getName, name.trim());
        }
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Product::getCreateTime);
        
        // 创建分页对象
        Page<Product> productPage = new Page<>(page, size);
        // 执行分页查询
        productPage = productMapper.selectPage(productPage, wrapper);
        
        // 转换为DTO
        Page<ProductDTO> dtoPage = new Page<>(page, size);
        // 复制分页信息
        dtoPage.setTotal(productPage.getTotal());
        dtoPage.setCurrent(productPage.getCurrent());
        dtoPage.setSize(productPage.getSize());
        
        // 转换记录并缓存
        List<ProductDTO> dtoList = productPage.getRecords().stream().map(product -> {
            // 添加到缓存
            cacheUtil.setCache(cacheUtil.getProductKey(product.getId()), product);
            
            ProductDTO dto = new ProductDTO();
            BeanUtils.copyProperties(product, dto);
            dto.setSkuList(getSkuList(product.getId()));
            return dto;
        }).collect(Collectors.toList());
        
        dtoPage.setRecords(dtoList);
        
        return dtoPage;
    }
    
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        product.setStatus(status);
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
        
        // 清除缓存
        cacheUtil.clearProductCaches(id);
    }
    
    @Override
    @Transactional
    public void updateStock(Long id, Integer stock) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        product.setStock(stock);
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
        
        // 清除缓存
        cacheUtil.clearProductCaches(id);
    }
    
    @Override
    public List<ProductSkuDTO> getSkuList(Long productId) {
        // 从缓存中获取SKU列表
        String cacheKey = "sku:product:" + productId;
        String skuListStr = (String) cacheUtil.getCache(cacheKey, String.class);
        
        List<ProductSku> skuList;
        
        if (skuListStr == null) {
            // 缓存未命中，从数据库获取
            LambdaQueryWrapper<ProductSku> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductSku::getProductId, productId);
            skuList = productSkuMapper.selectList(wrapper);
            
            // 将每个SKU添加到缓存
            for (ProductSku sku : skuList) {
                cacheUtil.setCache(cacheUtil.getSkuKey(sku.getId()), sku);
            }
        } else {
            // 从缓存解析SKU列表
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                skuList = objectMapper.readValue(skuListStr, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductSku.class));
            } catch (Exception e) {
                // 解析失败，从数据库获取
                LambdaQueryWrapper<ProductSku> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ProductSku::getProductId, productId);
                skuList = productSkuMapper.selectList(wrapper);
                
                // 将每个SKU添加到缓存
                for (ProductSku sku : skuList) {
                    cacheUtil.setCache(cacheUtil.getSkuKey(sku.getId()), sku);
                }
            }
        }
        
        return skuList.stream().map(sku -> {
            ProductSkuDTO dto = new ProductSkuDTO();
            BeanUtils.copyProperties(sku, dto);
            return dto;
        }).collect(Collectors.toList());
    }
} 