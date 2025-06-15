package com.example.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.dto.OrderDTO;
import com.example.admin.dto.OrderDeliveryDTO;
import com.example.admin.dto.OrderRefundDTO;
import com.example.admin.dto.OrderStatisticsDTO;

import java.util.List;

public interface OrderService {
    // 订单管理
    OrderDTO create(OrderDTO orderDTO);
    
    OrderDTO getById(Long id);
    
    Page<OrderDTO> page(Integer pageNum, Integer pageSize, String orderNo, Integer status);
    
    void updateStatus(Long id, Integer status);
    
    void delete(Long id);
    
    // 物流管理
    OrderDeliveryDTO getDelivery(Long orderId);
    
    void updateDelivery(OrderDeliveryDTO deliveryDTO);
    
    void updateDeliveryStatus(Long orderId, Integer status);
    
    // 退款管理
    OrderRefundDTO getRefund(Long orderId);
    
    void applyRefund(OrderRefundDTO refundDTO);
    
    void handleRefund(Long orderId, Integer status, String rejectReason);
    
    // 订单统计
    List<OrderDTO> getRecentOrders(Integer limit);
    
    Integer getOrderCount(Integer status);
    
    // 新增统计方法
    OrderStatisticsDTO getOrderStatistics();
} 