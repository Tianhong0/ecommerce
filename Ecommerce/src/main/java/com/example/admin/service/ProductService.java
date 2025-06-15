package com.example.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.ProductDTO;
import com.example.admin.dto.ProductSkuDTO;
import com.example.admin.entity.Product;

import java.util.List;

public interface ProductService {
    ProductDTO create(ProductDTO productDTO);

    ProductDTO update(Long id, ProductDTO productDTO);

    void delete(Long id);

    ProductDTO getById(Long id);

    Page<ProductDTO> list(Integer page, Integer size, String name, Long categoryId);

    void updateStatus(Long id, Integer status);

    void updateStock(Long id, Integer stock);

    List<ProductSkuDTO> getSkuList(Long productId);
}
