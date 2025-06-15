package com.example.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.CategoryDTO;
import com.example.admin.entity.Category;
import com.example.admin.exception.GlobalExceptionHandler.ApiResponse;
import com.example.admin.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('product:category:create')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.create(categoryDTO);
        return ResponseEntity.ok(new ApiResponse(true, "创建成功", category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product:category:update')")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(new ApiResponse(true, "更新成功", category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product:category:delete')")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除成功"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product:category:query')")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", category));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('product:category:query')")
    public ResponseEntity<ApiResponse> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        Page<Category> categoryPage = categoryService.list(page, size, name);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", categoryPage));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('product:category:update')")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        categoryService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "状态更新成功"));
    }
}
