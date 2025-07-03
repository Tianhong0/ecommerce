package com.example.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.ProductDTO;
import com.example.admin.exception.GlobalExceptionHandler.ApiResponse;
import com.example.admin.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理控制器
 * 提供商品的CRUD操作、状态管理和库存管理功能
 */
@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {

    /**
     * 商品服务
     */
    private final ProductService productService;

    /**
     * 创建商品
     * 
     * @param productDTO 商品信息，包含基本信息和SKU信息
     * @return 创建结果响应，包含创建后的商品信息
     */
    @PostMapping
    @PreAuthorize("hasAuthority('product:create')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO result = productService.create(productDTO);
        return ResponseEntity.ok(new ApiResponse(true, "创建成功", result));
    }

    /**
     * 更新商品信息
     * 
     * @param id 商品ID
     * @param productDTO 更新后的商品信息
     * @return 更新结果响应，包含更新后的商品信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO result = productService.update(id, productDTO);
        return ResponseEntity.ok(new ApiResponse(true, "更新成功", result));
    }

    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 删除结果响应
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product:delete')")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除成功"));
    }

    /**
     * 获取商品详情
     * 
     * @param id 商品ID
     * @return 商品详情响应，包含商品基本信息和SKU信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ProductDTO result = productService.getById(id);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    /**
     * 分页查询商品列表
     * 
     * @param page 页码，默认为1
     * @param size 每页大小，默认为10
     * @param name 商品名称（可选，模糊查询）
     * @param categoryId 分类ID（可选，精确匹配）
     * @return 商品列表分页结果
     */
    @GetMapping
    public ResponseEntity<ApiResponse> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId) {
        Page<ProductDTO> productPage = productService.list(page, size, name, categoryId);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", productPage));
    }

    /**
     * 更新商品状态
     * 
     * @param id 商品ID
     * @param status 新状态（1-上架，0-下架）
     * @return 更新结果响应
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        productService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "状态更新成功"));
    }

    /**
     * 更新商品库存
     * 
     * @param id 商品ID
     * @param stock 新库存数量
     * @return 更新结果响应
     */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<ApiResponse> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        productService.updateStock(id, stock);
        return ResponseEntity.ok(new ApiResponse(true, "库存更新成功"));
    }
}
