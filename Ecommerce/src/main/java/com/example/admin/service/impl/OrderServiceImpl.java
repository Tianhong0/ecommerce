package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.constant.KafkaConstant;
import com.example.admin.dto.*;
import com.example.admin.dto.message.OrderMessage;
import com.example.admin.dto.message.StockMessage;
import com.example.admin.entity.Order;
import com.example.admin.entity.OrderDelivery;
import com.example.admin.entity.OrderItem;
import com.example.admin.entity.OrderRefund;
import com.example.admin.mapper.OrderDeliveryMapper;
import com.example.admin.mapper.OrderItemMapper;
import com.example.admin.mapper.OrderMapper;
import com.example.admin.mapper.OrderRefundMapper;
import com.example.admin.service.KafkaProducerService;
import com.example.admin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderDeliveryMapper orderDeliveryMapper;
    private final OrderRefundMapper orderRefundMapper;
    private final KafkaProducerService kafkaProducerService;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        // 生成订单号：ORD + 时间戳 + 4位随机数
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.format("%04d", (int)(Math.random() * 10000));
        String orderNo = "ORD" + timestamp + random;

        // 创建订单
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        order.setOrderNo(orderNo);
        order.setStatus(0); // 待付款
        LocalDateTime now = LocalDateTime.now();
        order.setCreateTime(now);
        order.setUpdateTime(now);

        orderMapper.insert(order);

        // 创建订单项
        List<OrderMessage.OrderItemMessage> orderItemMessages = new ArrayList<>();
        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                OrderItem item = new OrderItem();
                BeanUtils.copyProperties(itemDTO, item);
                item.setOrderId(order.getId());
                item.setCreateTime(now);
                item.setUpdateTime(now);
                orderItemMapper.insert(item);
                
                // 构建订单项消息
                OrderMessage.OrderItemMessage itemMessage = OrderMessage.OrderItemMessage.builder()
                    .id(item.getId())
                    .productId(item.getProductId())
                    .skuId(item.getSkuId())
                    .productName(item.getProductName())
                    .skuName(item.getSkuName())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .totalAmount(item.getTotalAmount())
                    .build();
                orderItemMessages.add(itemMessage);
                
                // 发送库存扣减消息
                sendStockDeductMessage(item);
            }
        }
        
        // 发送订单创建消息
        OrderMessage orderMessage = OrderMessage.builder()
            .id(order.getId())
            .orderNo(order.getOrderNo())
            .userId(order.getUserId())
            .totalAmount(order.getTotalAmount())
            .payAmount(order.getPayAmount())
            .status(order.getStatus())
            .createTime(now)
            .orderItems(orderItemMessages)
            .build();
        
        kafkaProducerService.sendOrderMessage(KafkaConstant.TOPIC_ORDER_CREATE, orderMessage);

        // 返回创建的订单信息
        return getById(order.getId());
    }
    
    /**
     * 发送库存扣减消息
     */
    private void sendStockDeductMessage(OrderItem item) {
        StockMessage stockMessage = StockMessage.builder()
            .productId(item.getProductId())
            .skuId(item.getSkuId())
            .productName(item.getProductName())
            .skuName(item.getSkuName())
            .quantity(item.getQuantity())
            .orderNo(item.getOrderId().toString())
            .orderId(item.getOrderId())
            .operateTime(LocalDateTime.now())
            .operationType("DEDUCT")
            .build();
        
        kafkaProducerService.sendStockMessage(KafkaConstant.TOPIC_STOCK_DEDUCT, stockMessage);
    }

    @Override
    public OrderDTO getById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);

        // 查询订单项
        List<OrderItem> orderItems = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, id));
        List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                .map(item -> {
                    OrderItemDTO dto = new OrderItemDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        orderDTO.setOrderItems(orderItemDTOs);

        // 查询物流信息
        OrderDelivery delivery = orderDeliveryMapper.selectOne(
                new LambdaQueryWrapper<OrderDelivery>()
                        .eq(OrderDelivery::getOrderId, id));
        if (delivery != null) {
            OrderDeliveryDTO deliveryDTO = new OrderDeliveryDTO();
            BeanUtils.copyProperties(delivery, deliveryDTO);
            orderDTO.setOrderDelivery(deliveryDTO);
        }

        // 查询退款信息
        OrderRefund refund = orderRefundMapper.selectOne(
                new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getOrderId, id));
        if (refund != null) {
            OrderRefundDTO refundDTO = new OrderRefundDTO();
            BeanUtils.copyProperties(refund, refundDTO);
            orderDTO.setOrderRefund(refundDTO);
        }

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> page(Integer pageNum, Integer pageSize, String orderNo, Integer status) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(orderNo)) {
            wrapper.like(Order::getOrderNo, orderNo.trim());
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> orderPage = orderMapper.selectPage(page, wrapper);
        Page<OrderDTO> orderDTOPage = new Page<>();
        BeanUtils.copyProperties(orderPage, orderDTOPage, "records");

        List<OrderDTO> records = orderPage.getRecords().stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    BeanUtils.copyProperties(order, orderDTO);
                    
                    // 查询订单项
                    List<OrderItem> orderItems = orderItemMapper.selectList(
                            new LambdaQueryWrapper<OrderItem>()
                                    .eq(OrderItem::getOrderId, order.getId()));
                    List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                            .map(item -> {
                                OrderItemDTO dto = new OrderItemDTO();
                                BeanUtils.copyProperties(item, dto);
                                return dto;
                            })
                            .collect(Collectors.toList());
                    orderDTO.setOrderItems(orderItemDTOs);

                    // 查询物流信息
                    OrderDelivery delivery = orderDeliveryMapper.selectOne(
                            new LambdaQueryWrapper<OrderDelivery>()
                                    .eq(OrderDelivery::getOrderId, order.getId()));
                    if (delivery != null) {
                        OrderDeliveryDTO deliveryDTO = new OrderDeliveryDTO();
                        BeanUtils.copyProperties(delivery, deliveryDTO);
                        orderDTO.setOrderDelivery(deliveryDTO);
                    }

                    // 查询退款信息
                    OrderRefund refund = orderRefundMapper.selectOne(
                            new LambdaQueryWrapper<OrderRefund>()
                                    .eq(OrderRefund::getOrderId, order.getId()));
                    if (refund != null) {
                        OrderRefundDTO refundDTO = new OrderRefundDTO();
                        BeanUtils.copyProperties(refund, refundDTO);
                        orderDTO.setOrderRefund(refundDTO);
                    }

                    return orderDTO;
                })
                .collect(Collectors.toList());
        orderDTOPage.setRecords(records);

        return orderDTOPage;
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 更新订单状态
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        
        // 根据状态更新相应时间
        switch (status) {
            case 1: // 待发货
                order.setPayTime(LocalDateTime.now());
                break;
            case 2: // 待收货
                order.setDeliveryTime(LocalDateTime.now());
                break;
            case 3: // 已完成
                order.setReceiveTime(LocalDateTime.now());
                break;
        }
        
        orderMapper.updateById(order);
        
        // 构建订单消息
        OrderMessage orderMessage = OrderMessage.builder()
            .id(order.getId())
            .orderNo(order.getOrderNo())
            .userId(order.getUserId())
            .totalAmount(order.getTotalAmount())
            .payAmount(order.getPayAmount())
            .status(status)
            .createTime(order.getCreateTime())
            .build();
        
        // 根据状态发送不同的消息
        switch (status) {
            case 1: // 待发货
                kafkaProducerService.sendOrderMessage(KafkaConstant.TOPIC_ORDER_PAY, orderMessage);
                break;
            case 2: // 待收货
                kafkaProducerService.sendOrderMessage(KafkaConstant.TOPIC_ORDER_DELIVERY, orderMessage);
                break;
            case 3: // 已完成
                kafkaProducerService.sendOrderMessage(KafkaConstant.TOPIC_ORDER_COMPLETE, orderMessage);
                break;
            case 4: // 已取消
                kafkaProducerService.sendOrderMessage(KafkaConstant.TOPIC_ORDER_CANCEL, orderMessage);
                // 订单取消时，归还库存
                returnStockForCanceledOrder(order.getId());
                break;
        }
    }
    
    /**
     * 为取消的订单归还库存
     */
    private void returnStockForCanceledOrder(Long orderId) {
        List<OrderItem> orderItems = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId));
        
        for (OrderItem item : orderItems) {
            StockMessage stockMessage = StockMessage.builder()
                .productId(item.getProductId())
                .skuId(item.getSkuId())
                .productName(item.getProductName())
                .skuName(item.getSkuName())
                .quantity(item.getQuantity())
                .orderNo(orderId.toString())
                .orderId(orderId)
                .operateTime(LocalDateTime.now())
                .operationType("RETURN")
                .build();
            
            kafkaProducerService.sendStockMessage(KafkaConstant.TOPIC_STOCK_RETURN, stockMessage);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 删除订单项
        orderItemMapper.delete(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, id));

        // 删除物流信息
        orderDeliveryMapper.delete(new LambdaQueryWrapper<OrderDelivery>()
                .eq(OrderDelivery::getOrderId, id));

        // 删除退款信息
        orderRefundMapper.delete(new LambdaQueryWrapper<OrderRefund>()
                .eq(OrderRefund::getOrderId, id));

        // 删除订单
        orderMapper.deleteById(id);
    }

    @Override
    public OrderDeliveryDTO getDelivery(Long orderId) {
        OrderDelivery delivery = orderDeliveryMapper.selectOne(
                new LambdaQueryWrapper<OrderDelivery>()
                        .eq(OrderDelivery::getOrderId, orderId));
        if (delivery == null) {
            return null;
        }

        OrderDeliveryDTO deliveryDTO = new OrderDeliveryDTO();
        BeanUtils.copyProperties(delivery, deliveryDTO);
        return deliveryDTO;
    }

    @Override
    @Transactional
    public void updateDelivery(OrderDeliveryDTO deliveryDTO) {
        OrderDelivery delivery = orderDeliveryMapper.selectOne(
                new LambdaQueryWrapper<OrderDelivery>()
                        .eq(OrderDelivery::getOrderId, deliveryDTO.getOrderId()));

        if (delivery == null) {
            delivery = new OrderDelivery();
            BeanUtils.copyProperties(deliveryDTO, delivery);
            delivery.setCreateTime(LocalDateTime.now());
            orderDeliveryMapper.insert(delivery);
        } else {
            BeanUtils.copyProperties(deliveryDTO, delivery);
            delivery.setUpdateTime(LocalDateTime.now());
            orderDeliveryMapper.updateById(delivery);
        }
    }

    @Override
    @Transactional
    public void updateDeliveryStatus(Long orderId, Integer status) {
        OrderDelivery delivery = orderDeliveryMapper.selectOne(
                new LambdaQueryWrapper<OrderDelivery>()
                        .eq(OrderDelivery::getOrderId, orderId));
        if (delivery == null) {
            throw new RuntimeException("物流信息不存在");
        }

        delivery.setStatus(status);
        delivery.setUpdateTime(LocalDateTime.now());

        if (status == 1) { // 已发货
            delivery.setDeliveryTime(LocalDateTime.now());
            
            // 发送订单发货消息
            Order order = orderMapper.selectById(orderId);
            if (order != null) {
                OrderMessage orderMessage = OrderMessage.builder()
                    .id(order.getId())
                    .orderNo(order.getOrderNo())
                    .userId(order.getUserId())
                    .status(2) // 更新为待收货状态
                    .build();
                
                kafkaProducerService.sendOrderMessage(KafkaConstant.TOPIC_ORDER_DELIVERY, orderMessage);
                
                // 同时更新订单状态为待收货
                updateStatus(orderId, 2);
            }
        } else if (status == 2) { // 已签收
            delivery.setReceiveTime(LocalDateTime.now());
            
            // 发送订单完成消息
            Order order = orderMapper.selectById(orderId);
            if (order != null) {
                OrderMessage orderMessage = OrderMessage.builder()
                    .id(order.getId())
                    .orderNo(order.getOrderNo())
                    .userId(order.getUserId())
                    .status(3) // 更新为已完成状态
                    .build();
                
                kafkaProducerService.sendOrderMessage(KafkaConstant.TOPIC_ORDER_COMPLETE, orderMessage);
                
                // 同时更新订单状态为已完成
                updateStatus(orderId, 3);
            }
        }

        orderDeliveryMapper.updateById(delivery);
    }

    @Override
    public OrderRefundDTO getRefund(Long orderId) {
        OrderRefund refund = orderRefundMapper.selectOne(
                new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getOrderId, orderId));
        if (refund == null) {
            return null;
        }

        OrderRefundDTO refundDTO = new OrderRefundDTO();
        BeanUtils.copyProperties(refund, refundDTO);
        return refundDTO;
    }

    @Override
    @Transactional
    public void applyRefund(OrderRefundDTO refundDTO) {
        // 检查订单是否已存在退款记录
        OrderRefund existingRefund = orderRefundMapper.selectOne(
                new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getOrderId, refundDTO.getOrderId()));
        if (existingRefund != null) {
            throw new RuntimeException("该订单已存在退款记录");
        }

        // 检查订单是否存在
        Order order = orderMapper.selectById(refundDTO.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 创建退款记录
        OrderRefund refund = new OrderRefund();
        BeanUtils.copyProperties(refundDTO, refund);
        LocalDateTime now = LocalDateTime.now();
        refund.setStatus(0); // 待处理
        refund.setApplyTime(now);
        refund.setCreateTime(now);
        refund.setUpdateTime(now);

        // 生成退款单号：RF + 时间戳 + 4位随机数
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.format("%04d", (int)(Math.random() * 10000));
        String refundNo = "RF" + timestamp + random;
        refund.setRefundNo(refundNo);

        orderRefundMapper.insert(refund);

        // 更新订单状态为已退款
        order.setStatus(5); // 已退款
        order.setUpdateTime(now);
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void handleRefund(Long orderId, Integer status, String rejectReason) {
        OrderRefund refund = orderRefundMapper.selectOne(
                new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getOrderId, orderId));
        if (refund == null) {
            throw new RuntimeException("退款申请不存在");
        }

        refund.setStatus(status);
        refund.setHandleTime(LocalDateTime.now());
        refund.setUpdateTime(LocalDateTime.now());

        if (status == 2) { // 已拒绝
            refund.setRejectReason(rejectReason);
        } else if (status == 3) { // 已完成
            refund.setCompleteTime(LocalDateTime.now());
        }

        orderRefundMapper.updateById(refund);
    }

    @Override
    public List<OrderDTO> getRecentOrders(Integer limit) {
        List<Order> orders = orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .orderByDesc(Order::getCreateTime)
                        .last("LIMIT " + limit));

        return orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    BeanUtils.copyProperties(order, orderDTO);
                    return orderDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getOrderCount(Integer status) {
        Long count = orderMapper.selectCount(
                new LambdaQueryWrapper<Order>()
                        .eq(status != null, Order::getStatus, status));
        return count != null ? count.intValue() : 0;
    }

    @Override
    public OrderStatisticsDTO getOrderStatistics() {
        OrderStatisticsDTO statistics = new OrderStatisticsDTO();

        // 查询所有订单
        List<Order> orders = orderMapper.selectList(null);

        // 计算总销售额和订单数
        BigDecimal totalSales = BigDecimal.ZERO;
        Map<Integer, Integer> orderStatusCount = new HashMap<>();
        Map<Integer, Integer> payTypeCount = new HashMap<>();
        Map<Integer, BigDecimal> payTypeAmount = new HashMap<>();

        for (Order order : orders) {
            // 只统计已完成的订单
            if (order.getStatus() == 3) {
                totalSales = totalSales.add(order.getPayAmount());
            }

            // 统计各状态订单数量
            orderStatusCount.merge(order.getStatus(), 1, Integer::sum);

            // 统计各支付方式订单数量和金额
            payTypeCount.merge(order.getPayType(), 1, Integer::sum);
            payTypeAmount.merge(order.getPayType(), order.getPayAmount(), BigDecimal::add);
        }

        // 查询订单项统计商品数量
        List<OrderItem> orderItems = orderItemMapper.selectList(null);
        int totalProductCount = orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        // 查询退款统计
        List<OrderRefund> refunds = orderRefundMapper.selectList(
                new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getStatus, 3)); // 只统计已完成的退款

        BigDecimal totalRefundAmount = refunds.stream()
                .map(OrderRefund::getRefundAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 设置统计结果
        statistics.setTotalSales(totalSales);
        statistics.setTotalOrderCount(orders.size());
        statistics.setTotalProductCount(totalProductCount);
        statistics.setTotalRefundAmount(totalRefundAmount);
        statistics.setTotalRefundCount(refunds.size());
        statistics.setOrderStatusCount(orderStatusCount);
        statistics.setPayTypeCount(payTypeCount);
        statistics.setPayTypeAmount(payTypeAmount);

        return statistics;
    }
}
