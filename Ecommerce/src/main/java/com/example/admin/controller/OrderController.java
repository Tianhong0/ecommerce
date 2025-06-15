package com.example.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.OrderDTO;
import com.example.admin.dto.OrderDeliveryDTO;
import com.example.admin.dto.OrderRefundDTO;
import com.example.admin.dto.OrderStatisticsDTO;
import com.example.admin.exception.GlobalExceptionHandler.ApiResponse;
import com.example.admin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('order:create')")
    public ResponseEntity<ApiResponse> create(@RequestBody OrderDTO orderDTO) {
        OrderDTO result = orderService.create(orderDTO);
        return ResponseEntity.ok(new ApiResponse(true, "创建成功", result));
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('order:query')")
    public ResponseEntity<ApiResponse> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status) {
        Page<OrderDTO> result = orderService.page(pageNum, pageSize, orderNo, status);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('order:query')")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        OrderDTO result = orderService.getById(id);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('order:update')")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        orderService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "状态更新成功"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('order:delete')")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok(new ApiResponse(true, "删除成功"));
    }

    @GetMapping("/{id}/delivery")
    @PreAuthorize("hasAuthority('order:query')")
    public ResponseEntity<ApiResponse> getDelivery(@PathVariable Long id) {
        OrderDeliveryDTO result = orderService.getDelivery(id);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    @PutMapping("/{id}/delivery")
    @PreAuthorize("hasAuthority('order:update')")
    public ResponseEntity<ApiResponse> updateDelivery(@PathVariable Long id, @RequestBody OrderDeliveryDTO deliveryDTO) {
        deliveryDTO.setOrderId(id);
        orderService.updateDelivery(deliveryDTO);
        return ResponseEntity.ok(new ApiResponse(true, "物流信息更新成功"));
    }

    @PutMapping("/{id}/delivery/status")
    @PreAuthorize("hasAuthority('order:update')")
    public ResponseEntity<ApiResponse> updateDeliveryStatus(@PathVariable Long id, @RequestParam Integer status) {
        orderService.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "物流状态更新成功"));
    }

    @GetMapping("/{id}/refund")
    @PreAuthorize("hasAuthority('order:query')")
    public ResponseEntity<ApiResponse> getRefund(@PathVariable Long id) {
        OrderRefundDTO result = orderService.getRefund(id);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasAuthority('order:refund')")
    public ResponseEntity<ApiResponse> applyRefund(@PathVariable Long id, @RequestBody OrderRefundDTO refundDTO) {
        refundDTO.setOrderId(id);
        orderService.applyRefund(refundDTO);
        return ResponseEntity.ok(new ApiResponse(true, "退款申请提交成功"));
    }

    @PutMapping("/{id}/refund")
    @PreAuthorize("hasAuthority('order:refund')")
    public ResponseEntity<ApiResponse> handleRefund(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String rejectReason) {
        orderService.handleRefund(id, status, rejectReason);
        return ResponseEntity.ok(new ApiResponse(true, "退款处理成功"));
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAuthority('order:query')")
    public ResponseEntity<ApiResponse> getRecentOrders(@RequestParam(defaultValue = "10") Integer limit) {
        List<OrderDTO> result = orderService.getRecentOrders(limit);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('order:query')")
    public ResponseEntity<ApiResponse> getOrderCount(@RequestParam(required = false) Integer status) {
        Integer result = orderService.getOrderCount(status);
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('order:query')")
    public ResponseEntity<ApiResponse> getOrderStatistics() {
        OrderStatisticsDTO result = orderService.getOrderStatistics();
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }
}
