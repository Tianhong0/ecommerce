-- 订单表
CREATE TABLE `t_order` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` varchar(64) NOT NULL COMMENT '订单编号',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
    `pay_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
    `freight_amount` decimal(10,2) NOT NULL COMMENT '运费金额',
    `pay_type` tinyint NOT NULL COMMENT '支付方式：1-支付宝 2-微信 3-银联',
    `status` tinyint NOT NULL COMMENT '订单状态：0-待付款 1-待发货 2-待收货 3-已完成 4-已取消 5-已退款',
    `receiver_name` varchar(100) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` varchar(32) NOT NULL COMMENT '收货人电话',
    `receiver_address` varchar(200) NOT NULL COMMENT '收货地址',
    `note` varchar(500) DEFAULT NULL COMMENT '订单备注',
    `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
    `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
    `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单项表
CREATE TABLE `order_item` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
    `order_id` bigint NOT NULL COMMENT '订单ID',
    `product_id` bigint NOT NULL COMMENT '商品ID',
    `sku_id` bigint NOT NULL COMMENT 'SKU ID',
    `product_name` varchar(200) NOT NULL COMMENT '商品名称',
    `sku_name` varchar(200) NOT NULL COMMENT 'SKU名称',
    `product_pic` varchar(500) NOT NULL COMMENT '商品图片',
    `price` decimal(10,2) NOT NULL COMMENT '商品单价',
    `quantity` int NOT NULL COMMENT '购买数量',
    `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- 订单物流表
CREATE TABLE `order_delivery` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物流ID',
    `order_id` bigint NOT NULL COMMENT '订单ID',
    `delivery_company` varchar(64) NOT NULL COMMENT '物流公司',
    `delivery_sn` varchar(64) NOT NULL COMMENT '物流单号',
    `receiver_name` varchar(100) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` varchar(32) NOT NULL COMMENT '收货人电话',
    `receiver_address` varchar(200) NOT NULL COMMENT '收货地址',
    `status` tinyint NOT NULL COMMENT '物流状态：0-待发货 1-已发货 2-已签收',
    `delivery_info` text COMMENT '物流信息（JSON格式）',
    `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
    `receive_time` datetime DEFAULT NULL COMMENT '签收时间',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_delivery_sn` (`delivery_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单物流表';

-- 订单退款表
CREATE TABLE `order_refund` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退款ID',
    `order_id` bigint NOT NULL COMMENT '订单ID',
    `refund_no` varchar(64) NOT NULL COMMENT '退款单号',
    `refund_amount` decimal(10,2) NOT NULL COMMENT '退款金额',
    `status` tinyint NOT NULL COMMENT '退款状态：0-待处理 1-已同意 2-已拒绝 3-已完成',
    `reason` varchar(200) NOT NULL COMMENT '退款原因',
    `description` varchar(500) DEFAULT NULL COMMENT '退款说明',
    `proof_images` varchar(1000) DEFAULT NULL COMMENT '凭证图片（JSON数组）',
    `reject_reason` varchar(200) DEFAULT NULL COMMENT '拒绝原因',
    `apply_time` datetime NOT NULL COMMENT '申请时间',
    `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
    `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    UNIQUE KEY `uk_refund_no` (`refund_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单退款表'; 