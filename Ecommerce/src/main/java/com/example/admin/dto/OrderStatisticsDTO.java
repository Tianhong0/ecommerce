package com.example.admin.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import com.example.admin.dto.OrderDTO;

@Data
public class OrderStatisticsDTO {
    private BigDecimal totalSales; // 总销售额
    private Integer totalOrderCount; // 总订单数
    private Integer totalProductCount; // 总商品数量
    private BigDecimal totalRefundAmount; // 总退款金额
    private Integer totalRefundCount; // 总退款订单数
    private Map<Integer, Integer> orderStatusCount; // 各状态订单数量
    private Map<Integer, Integer> payTypeCount; // 各支付方式订单数量
    private Map<Integer, BigDecimal> payTypeAmount; // 各支付方式金额
    private Integer pendingPaymentCount; // 待付款订单数
    private Integer pendingDeliveryCount; // 待发货订单数
    private Integer pendingReceiveCount; // 待收货订单数
    private Integer completedCount; // 已完成订单数
    private Integer cancelledCount; // 已取消订单数
    private Integer refundedCount; // 已退款订单数
    private Integer totalCount; // 总订单数
    private List<OrderDTO> recentOrders; // 最近订单列表
} 