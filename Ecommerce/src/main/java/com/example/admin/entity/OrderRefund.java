package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_refund")
public class OrderRefund {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long orderId; // 订单ID
    
    private String refundNo; // 退款单号
    
    private BigDecimal refundAmount; // 退款金额
    
    private Integer status; // 退款状态：0-待处理 1-已同意 2-已拒绝 3-已完成
    
    private String reason; // 退款原因
    
    private String description; // 退款说明
    
    private String proofImages; // 凭证图片（JSON数组）
    
    private String rejectReason; // 拒绝原因
    
    private LocalDateTime applyTime; // 申请时间
    
    private LocalDateTime handleTime; // 处理时间
    
    private LocalDateTime completeTime; // 完成时间
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 