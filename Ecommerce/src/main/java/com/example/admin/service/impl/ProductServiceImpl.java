package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.ProductDTO;
import com.example.admin.dto.ProductSkuDTO;
import com.example.admin.entity.Product;
import com.example.admin.entity.ProductSku;
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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;
    
    @Override
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        
        productMapper.insert(product);
        
        // 保存SKU信息
        if (productDTO.getSkuList() != null && !productDTO.getSkuList().isEmpty()) {
            for (ProductSkuDTO skuDTO : productDTO.getSkuList()) {
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(skuDTO, sku);
                sku.setProductId(product.getId());
                sku.setCreateTime(LocalDateTime.now());
                sku.setUpdateTime(LocalDateTime.now());
                
                productSkuMapper.insert(sku);
            }
        }
        
        return getById(product.getId());
    }
    
    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        BeanUtils.copyProperties(productDTO, product);
        product.setUpdateTime(LocalDateTime.now());
        
        productMapper.updateById(product);
        
        // 更新SKU信息
        if (productDTO.getSkuList() != null && !productDTO.getSkuList().isEmpty()) {
            // 删除原有SKU
            LambdaQueryWrapper<ProductSku> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductSku::getProductId, id);
            productSkuMapper.delete(wrapper);
            
            // 添加新SKU
            for (ProductSkuDTO skuDTO : productDTO.getSkuList()) {
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(skuDTO, sku);
                sku.setProductId(id);
                sku.setCreateTime(LocalDateTime.now());
                sku.setUpdateTime(LocalDateTime.now());
                
                productSkuMapper.insert(sku);
            }
        }
        
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
    }
    
    @Override
    public ProductDTO getById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return null;
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
        
        // 转换记录
        List<ProductDTO> dtoList = productPage.getRecords().stream().map(product -> {
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
            throw new RuntimeException("商品不存在");
        }
        
        product.setStatus(status);
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
    }
    
    @Override
    @Transactional
    public void updateStock(Long id, Integer stock) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        product.setStock(stock);
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
    }
    
    @Override
    public List<ProductSkuDTO> getSkuList(Long productId) {
        LambdaQueryWrapper<ProductSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSku::getProductId, productId);
        List<ProductSku> skuList = productSkuMapper.selectList(wrapper);
        
        return skuList.stream().map(sku -> {
            ProductSkuDTO dto = new ProductSkuDTO();
            BeanUtils.copyProperties(sku, dto);
            return dto;
        }).collect(Collectors.toList());
    }
} 