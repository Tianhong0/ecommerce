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

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('product:create')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO result = productService.create(productDTO);
        return ResponseEntity.ok(new ApiResponse(true, "创建成功", result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO result = productService.update(id, productDTO);
        return ResponseEntity.ok(new ApiResponse(true, "更新成功", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product:delete')")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除成功"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product:query')")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ProductDTO result = productService.getById(id);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('product:query')")
    public ResponseEntity<ApiResponse> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId) {
        Page<ProductDTO> productPage = productService.list(page, size, name, categoryId);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", productPage));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        productService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "状态更新成功"));
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<ApiResponse> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        productService.updateStock(id, stock);
        return ResponseEntity.ok(new ApiResponse(true, "库存更新成功"));
    }
}
