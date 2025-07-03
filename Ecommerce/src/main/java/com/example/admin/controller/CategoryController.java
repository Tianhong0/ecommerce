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

/**
 * 商品分类控制器
 * <p>
 * 负责处理与商品分类相关的HTTP请求，提供商品分类的增删改查操作和状态管理功能。
 * 包括创建分类、更新分类信息、删除分类、查询分类详情、分页查询分类列表以及
 * 更新分类状态等功能。该控制器使用Spring Security进行权限控制，确保只有
 * 具备相应权限的用户才能执行特定操作。
 * </p>
 * <p>
 * API基础路径: /category
 * </p>
 * 
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {

    /**
     * 分类服务
     * 注入的CategoryService实例，用于处理业务逻辑
     */
    private final CategoryService categoryService;

    /**
     * 创建商品分类
     * <p>
     * 接收商品分类数据，创建新的分类记录。
     * 需要用户具有'product:category:create'权限。
     * </p>
     * 
     * @param categoryDTO 分类信息传输对象，包含分类名称、父ID等信息
     * @return 创建结果响应，包含创建后的分类信息
     */
    @PostMapping
    @PreAuthorize("hasAuthority('product:category:create')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.create(categoryDTO);
        return ResponseEntity.ok(new ApiResponse(true, "创建成功", category));
    }

    /**
     * 更新商品分类
     * <p>
     * 根据分类ID更新分类信息。
     * 需要用户具有'product:category:update'权限。
     * </p>
     * 
     * @param id 分类ID，路径变量
     * @param categoryDTO 更新后的分类信息传输对象
     * @return 更新结果响应，包含更新后的分类信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product:category:update')")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(new ApiResponse(true, "更新成功", category));
    }

    /**
     * 删除商品分类
     * <p>
     * 根据分类ID删除分类记录。
     * 需要用户具有'product:category:delete'权限。
     * </p>
     * 
     * @param id 分类ID，路径变量
     * @return 删除结果响应
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product:category:delete')")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除成功"));
    }

    /**
     * 获取商品分类详情
     * <p>
     * 根据分类ID查询分类详细信息。
     * 该接口不需要特殊权限，所有已认证用户均可访问。
     * </p>
     * 
     * @param id 分类ID，路径变量
     * @return 分类详情响应，包含分类完整信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", category));
    }

    /**
     * 分页查询商品分类列表
     * <p>
     * 支持按分类名称模糊查询，返回分页结果。
     * 该接口不需要特殊权限，所有已认证用户均可访问。
     * </p>
     * 
     * @param page 页码，默认为1
     * @param size 每页大小，默认为10
     * @param name 分类名称（可选，模糊查询）
     * @return 分类列表分页结果，包含分页信息和分类数据
     */
    @GetMapping
    public ResponseEntity<ApiResponse> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        Page<Category> categoryPage = categoryService.list(page, size, name);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", categoryPage));
    }

    /**
     * 更新商品分类状态
     * <p>
     * 根据分类ID更新分类的启用/禁用状态。
     * 需要用户具有'product:category:update'权限。
     * </p>
     * 
     * @param id 分类ID，路径变量
     * @param status 新状态（1-启用，0-禁用）
     * @return 更新结果响应
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('product:category:update')")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        categoryService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "更新状态成功"));
    }
}
