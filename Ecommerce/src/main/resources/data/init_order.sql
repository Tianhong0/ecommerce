-- 插入订单数据
INSERT INTO `t_order` (`order_no`, `user_id`, `total_amount`, `pay_amount`, `freight_amount`, `pay_type`, `status`, 
    `receiver_name`, `receiver_phone`, `receiver_address`, `note`, `pay_time`, `delivery_time`, `receive_time`, 
    `create_time`, `update_time`) VALUES
('202403150001', 1, 299.00, 299.00, 0.00, 1, 3, '张三', '13800138001', '北京市朝阳区xxx街道', '请尽快发货', 
    '2024-03-15 10:00:00', '2024-03-15 14:00:00', '2024-03-17 16:00:00', '2024-03-15 09:30:00', '2024-03-17 16:00:00'),
('202403150002', 1, 599.00, 599.00, 0.00, 2, 2, '张三', '13800138001', '北京市朝阳区xxx街道', NULL, 
    '2024-03-15 11:00:00', '2024-03-15 15:00:00', NULL, '2024-03-15 10:30:00', '2024-03-15 15:00:00'),
('202403150003', 2, 199.00, 199.00, 0.00, 1, 1, '李四', '13800138002', '上海市浦东新区xxx路', '周末送货', 
    '2024-03-15 13:00:00', NULL, NULL, '2024-03-15 12:30:00', '2024-03-15 13:00:00'),
('202403150004', 2, 899.00, 899.00, 0.00, 3, 0, '李四', '13800138002', '上海市浦东新区xxx路', NULL, 
    NULL, NULL, NULL, '2024-03-15 14:30:00', '2024-03-15 14:30:00'),
('202403150005', 3, 399.00, 399.00, 0.00, 1, 4, '王五', '13800138003', '广州市天河区xxx路', '取消订单', 
    NULL, NULL, NULL, '2024-03-15 15:30:00', '2024-03-15 16:00:00');

-- 插入订单项数据
INSERT INTO `order_item` (`order_id`, `product_id`, `sku_id`, `product_name`, `sku_name`, `product_pic`, 
    `price`, `quantity`, `total_amount`, `create_time`, `update_time`) VALUES
(1, 1, 1, 'iPhone 15', 'iPhone 15 128GB 黑色', '/images/products/iphone15.jpg', 299.00, 1, 299.00, 
    '2024-03-15 09:30:00', '2024-03-15 09:30:00'),
(2, 2, 2, 'MacBook Pro', 'MacBook Pro 14英寸 M3', '/images/products/macbook.jpg', 599.00, 1, 599.00, 
    '2024-03-15 10:30:00', '2024-03-15 10:30:00'),
(3, 3, 3, 'AirPods Pro', 'AirPods Pro 2代', '/images/products/airpods.jpg', 199.00, 1, 199.00, 
    '2024-03-15 12:30:00', '2024-03-15 12:30:00'),
(4, 4, 4, 'iPad Pro', 'iPad Pro 12.9英寸 M2', '/images/products/ipad.jpg', 899.00, 1, 899.00, 
    '2024-03-15 14:30:00', '2024-03-15 14:30:00'),
(5, 5, 5, 'Apple Watch', 'Apple Watch Series 9', '/images/products/watch.jpg', 399.00, 1, 399.00, 
    '2024-03-15 15:30:00', '2024-03-15 15:30:00');

-- 插入订单物流数据
INSERT INTO `order_delivery` (`order_id`, `delivery_company`, `delivery_sn`, `receiver_name`, `receiver_phone`, 
    `receiver_address`, `status`, `delivery_info`, `delivery_time`, `receive_time`, `create_time`, `update_time`) VALUES
(1, '顺丰速运', 'SF1234567890', '张三', '13800138001', '北京市朝阳区xxx街道', 2, 
    '[{"time":"2024-03-15 14:00:00","info":"快件已发出"},{"time":"2024-03-16 10:00:00","info":"快件已到达北京转运中心"},{"time":"2024-03-17 16:00:00","info":"快件已签收"}]', 
    '2024-03-15 14:00:00', '2024-03-17 16:00:00', '2024-03-15 14:00:00', '2024-03-17 16:00:00'),
(2, '京东物流', 'JD1234567890', '张三', '13800138001', '北京市朝阳区xxx街道', 1, 
    '[{"time":"2024-03-15 15:00:00","info":"快件已发出"},{"time":"2024-03-16 09:00:00","info":"快件已到达北京转运中心"}]', 
    '2024-03-15 15:00:00', NULL, '2024-03-15 15:00:00', '2024-03-15 15:00:00'),
(3, '中通快递', 'ZT1234567890', '李四', '13800138002', '上海市浦东新区xxx路', 0, NULL, 
    NULL, NULL, '2024-03-15 13:00:00', '2024-03-15 13:00:00');

-- 插入订单退款数据
INSERT INTO `order_refund` (`order_id`, `refund_no`, `refund_amount`, `status`, `reason`, `description`, 
    `proof_images`, `reject_reason`, `apply_time`, `handle_time`, `complete_time`, `create_time`, `update_time`) VALUES
(5, 'RF202403150001', 399.00, 3, '商品价格过高', '发现其他平台价格更优惠', 
    '["/images/refund/proof1.jpg","/images/refund/proof2.jpg"]', NULL, 
    '2024-03-15 15:45:00', '2024-03-15 15:50:00', '2024-03-15 16:00:00', 
    '2024-03-15 15:45:00', '2024-03-15 16:00:00'); 