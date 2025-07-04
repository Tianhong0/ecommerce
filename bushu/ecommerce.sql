/*
 Navicat Premium Dump SQL

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : ecommerce

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 28/06/2025 16:40:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '手机数码', '智能手机、平板电脑等数码产品', 1, 1, '2025-06-01 20:14:43', '2025-06-28 14:58:56');
INSERT INTO `category` VALUES (2, '电脑办公', '笔记本电脑、台式机、办公设备等', 2, 1, '2025-06-01 20:14:43', '2025-06-01 20:15:10');
INSERT INTO `category` VALUES (3, '家用电器', '电视、冰箱、洗衣机等家电产品', 3, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (4, '服装鞋包', '男装、女装、箱包、鞋靴等', 4, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (5, '美妆个护', '护肤、彩妆、个人护理等', 5, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (6, '食品生鲜', '零食、生鲜、粮油等', 6, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (7, '图书音像', '图书、电子书、音乐等', 7, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (8, '运动户外', '运动服饰、健身器材、户外装备等', 8, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (9, '家居家装', '家具、家纺、家装建材等', 9, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (10, '母婴玩具', '奶粉、纸尿裤、玩具等', 10, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (11, '珠宝配饰', '黄金、钻石、时尚配饰等', 11, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (12, '汽车用品', '汽车配件、车载用品等', 12, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (13, '医药保健', '药品、保健品、医疗器械等', 13, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (14, '宠物用品', '宠物食品、玩具、清洁用品等', 14, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (15, '虚拟服务', '游戏点卡、话费充值等', 15, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (16, '礼品鲜花', '礼品、鲜花、蛋糕等', 16, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (17, '钟表眼镜', '手表、眼镜、太阳镜等', 17, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (18, '厨具餐具', '锅具、餐具、厨房小电等', 18, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (19, '家纺家饰', '床上用品、窗帘、装饰品等', 19, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');
INSERT INTO `category` VALUES (20, '生活服务', '家政服务、维修服务等', 20, 1, '2025-06-01 20:14:43', '2025-06-01 20:14:43');

-- ----------------------------
-- Table structure for order_delivery
-- ----------------------------
DROP TABLE IF EXISTS `order_delivery`;
CREATE TABLE `order_delivery`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `delivery_company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流公司',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流单号',
  `receiver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货地址',
  `status` tinyint NOT NULL COMMENT '物流状态：0-待发货 1-已发货 2-已签收',
  `delivery_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '物流信息（JSON格式）',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '签收时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_delivery_sn`(`delivery_sn` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单物流表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_delivery
-- ----------------------------
INSERT INTO `order_delivery` VALUES (1, 1, '顺丰速运', 'SF1234567890', '张三', '13800138001', '北京市朝阳区xxx街道', 2, '[{\"time\":\"2024-03-15 14:00:00\",\"info\":\"快件已发出\"},{\"time\":\"2024-03-16 10:00:00\",\"info\":\"快件已到达北京转运中心\"},{\"time\":\"2024-03-17 16:00:00\",\"info\":\"快件已签收\"}]', '2024-03-15 14:00:00', '2024-03-17 16:00:00', '2024-03-15 14:00:00', '2024-03-17 16:00:00');
INSERT INTO `order_delivery` VALUES (2, 2, '京东物流', 'JD1234567890', '张三', '13800138001', '北京市朝阳区xxx街道', 2, '[{\"time\":\"2024-03-15 15:00:00\",\"info\":\"快件已发出\"},{\"time\":\"2024-03-16 09:00:00\",\"info\":\"快件已到达北京转运中心\"}]', '2024-03-15 15:00:00', '2025-06-20 20:01:23', '2024-03-15 15:00:00', '2025-06-20 20:01:23');
INSERT INTO `order_delivery` VALUES (5, 9, '11', '11', '收货人姓名', '收货人电话', '收货地址', 2, '[{\"time\":\"2025/6/3 19:43:42\",\"info\":\"快件已发出\"}]', '2025-06-20 20:21:02', '2025-06-20 20:21:03', '2025-06-03 19:43:42', '2025-06-20 20:21:03');
INSERT INTO `order_delivery` VALUES (6, 17, '11', '11', 'qq', 'qq', 'qq', 2, '[{\"time\":\"2025/6/3 21:43:42\",\"info\":\"快件已发出\"}]', '2025-06-03 21:43:43', '2025-06-03 21:43:47', '2025-06-03 21:43:43', '2025-06-03 21:43:47');
INSERT INTO `order_delivery` VALUES (7, 2651, '1', '1', '李四', '13800138002', '上海市浦东新区xxx路', 2, '[{\"time\":\"2025/6/20 19:11:45\",\"info\":\"快件已发出\"}]', '2025-06-20 19:11:46', '2025-06-20 19:11:48', '2025-06-20 19:11:46', '2025-06-20 19:11:48');
INSERT INTO `order_delivery` VALUES (8, 2656, '1', '1', '李四', '13800138002', '上海市浦东新区xxx路', 2, '[{\"time\":\"2025/6/20 19:28:52\",\"info\":\"快件已发出\"}]', '2025-06-20 19:28:52', '2025-06-20 19:28:53', '2025-06-20 19:28:52', '2025-06-20 19:28:53');
INSERT INTO `order_delivery` VALUES (12, 2661, '1', '1', '李四', '13800138002', '上海市浦东新区xxx路', 2, '[{\"time\":\"2025/6/20 19:49:29\",\"info\":\"快件已发出\"}]', '2025-06-20 19:49:30', '2025-06-20 19:49:48', '2025-06-20 19:49:30', '2025-06-20 19:49:48');
INSERT INTO `order_delivery` VALUES (13, 6, '11', '1', '1', '1', '1', 2, '[{\"time\":\"2025/6/20 19:56:10\",\"info\":\"快件已发出\"}]', '2025-06-20 19:56:11', '2025-06-20 19:56:15', '2025-06-20 19:56:11', '2025-06-20 19:56:15');
INSERT INTO `order_delivery` VALUES (14, 3, '1', '1', '李四', '13800138002', '上海市浦东新区xxx路', 2, '[{\"time\":\"2025/6/20 20:01:26\",\"info\":\"快件已发出\"}]', '2025-06-20 20:01:26', '2025-06-20 20:01:28', '2025-06-20 20:01:26', '2025-06-20 20:01:28');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU名称',
  `product_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品图片',
  `price` decimal(10, 2) NOT NULL COMMENT '商品单价',
  `quantity` int NOT NULL COMMENT '购买数量',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '总金额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2655 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (2649, 1, 1, 1, 'iPhone 15', 'iPhone 15 128GB 黑色', '/images/products/iphone15.jpg', 299.00, 1, 299.00, '2024-03-15 09:30:00', '2024-03-15 09:30:00');
INSERT INTO `order_item` VALUES (2650, 2, 2, 2, 'MacBook Pro', 'MacBook Pro 14英寸 M3', '/images/products/macbook.jpg', 599.00, 1, 599.00, '2024-03-15 10:30:00', '2024-03-15 10:30:00');
INSERT INTO `order_item` VALUES (2651, 3, 3, 3, 'AirPods Pro', 'AirPods Pro 2代', '/images/products/airpods.jpg', 199.00, 1, 199.00, '2024-03-15 12:30:00', '2024-03-15 12:30:00');
INSERT INTO `order_item` VALUES (2652, 4, 4, 4, 'iPad Pro', 'iPad Pro 12.9英寸 M2', '/images/products/ipad.jpg', 899.00, 1, 899.00, '2024-03-15 14:30:00', '2024-03-15 14:30:00');
INSERT INTO `order_item` VALUES (2653, 5, 5, 5, 'Apple Watch', 'Apple Watch Series 9', '/images/products/watch.jpg', 399.00, 1, 399.00, '2024-03-15 15:30:00', '2024-03-15 15:30:00');
INSERT INTO `order_item` VALUES (2654, 12, 1, 1, 'iPhone 15 Pro', 'iPhone 15 Pro 256GB 黑色', 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/caaea5fd-2e76-4000-be3a-666ac9213031.jpg', 7999.00, 1, 7999.00, '2025-06-28 15:13:30', '2025-06-28 15:13:30');

-- ----------------------------
-- Table structure for order_refund
-- ----------------------------
DROP TABLE IF EXISTS `order_refund`;
CREATE TABLE `order_refund`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退款ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `refund_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款单号',
  `refund_amount` decimal(10, 2) NOT NULL COMMENT '退款金额',
  `status` tinyint NOT NULL COMMENT '退款状态：0-待处理 1-已同意 2-已拒绝 3-已完成',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款原因',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款说明',
  `proof_images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '凭证图片（JSON数组）',
  `reject_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `apply_time` datetime NOT NULL COMMENT '申请时间',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_id`(`order_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_refund_no`(`refund_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退款表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_refund
-- ----------------------------
INSERT INTO `order_refund` VALUES (22, 5, 'RF202403150001', 399.00, 3, '商品价格过高', '发现其他平台价格更优惠', '[\"/images/refund/proof1.jpg\",\"/images/refund/proof2.jpg\"]', NULL, '2024-03-15 15:45:00', '2024-03-15 15:50:00', '2024-03-15 16:00:00', '2024-03-15 15:45:00', '2024-03-15 16:00:00');
INSERT INTO `order_refund` VALUES (23, 2661, 'RF17504202114523719', 199.00, 2, '1', '1', '[\"https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/e6e9d5ba-927b-4919-a087-c152a248b035.jpg\"]', '2', '2025-06-20 19:50:11', '2025-06-20 19:50:29', NULL, '2025-06-20 19:50:11', '2025-06-20 19:50:29');
INSERT INTO `order_refund` VALUES (24, 6, 'RF17504205816332601', 7999.00, 2, '1', '1', '[]', '33', '2025-06-20 19:56:22', '2025-06-20 20:02:36', NULL, '2025-06-20 19:56:22', '2025-06-20 20:02:36');
INSERT INTO `order_refund` VALUES (25, 3, 'RF17504208934735911', 199.00, 1, '1', '1', '[\"https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/faf8f407-ab3d-4472-8c32-cdff20aa04a6.jpg\"]', NULL, '2025-06-20 20:01:33', '2025-06-20 20:01:41', NULL, '2025-06-20 20:01:33', '2025-06-20 20:01:41');
INSERT INTO `order_refund` VALUES (26, 2, 'RF17504219072622203', 599.00, 2, '2', '2', '[]', '2', '2025-06-20 20:18:27', '2025-06-20 20:19:10', NULL, '2025-06-20 20:18:27', '2025-06-20 20:19:10');
INSERT INTO `order_refund` VALUES (27, 1, 'RF17504219149552102', 299.00, 2, '2', '2', '[]', '2', '2025-06-20 20:18:35', '2025-06-20 20:18:38', NULL, '2025-06-20 20:18:35', '2025-06-20 20:18:38');
INSERT INTO `order_refund` VALUES (28, 7, 'RF17504220276163823', 299.00, 2, '3', '3', '[\"https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/7c97c720-e10d-4b63-90a1-6e9762a512c9.jpg\"]', '3', '2025-06-20 20:20:28', '2025-06-20 20:20:31', NULL, '2025-06-20 20:20:28', '2025-06-20 20:20:31');
INSERT INTO `order_refund` VALUES (29, 9, 'RF17504228792993181', 199.00, 2, '12', '1', '[\"https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/480fb396-fa3b-4dec-a9f7-b15e3144efcf.jpg\"]', '2', '2025-06-20 20:34:39', '2025-06-20 20:34:43', NULL, '2025-06-20 20:34:39', '2025-06-20 20:34:43');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限编码',
  `type` tinyint NOT NULL COMMENT '类型：1-菜单，2-按钮',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级ID',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '系统管理', 'system', 1, NULL, '/system', 'setting', 1, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (2, '用户管理', 'system:user', 1, 1, '/system/user', 'user', 1, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (3, '角色管理', 'system:role', 1, 1, '/system/role', 'team', 2, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (4, '权限管理', 'system:permission', 1, 1, '/system/permission', 'safety', 3, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (5, '商品管理', 'product', 1, NULL, '/product', 'shopping', 2, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (6, '商品分类', 'product:category', 1, 5, '/product/category', 'appstore', 1, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (7, '商品列表', 'product:list', 1, 5, '/product/list', 'shopping-cart', 2, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (8, '用户查询', 'user:query', 2, 2, NULL, NULL, 1, 1, '2025-06-01 21:39:12', '2025-06-08 22:44:43');
INSERT INTO `permission` VALUES (9, '用户创建', 'user:create', 2, 2, NULL, NULL, 2, 1, '2025-06-01 21:39:12', '2025-06-08 22:44:47');
INSERT INTO `permission` VALUES (10, '用户编辑', 'user:update', 2, 2, NULL, NULL, 3, 1, '2025-06-01 21:39:12', '2025-06-08 22:44:55');
INSERT INTO `permission` VALUES (11, '用户删除', 'user:delete', 2, 2, NULL, NULL, 4, 1, '2025-06-01 21:39:12', '2025-06-08 22:44:58');
INSERT INTO `permission` VALUES (12, '角色查询', 'role:query', 2, 3, NULL, NULL, 1, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:02');
INSERT INTO `permission` VALUES (13, '角色创建', 'role:create', 2, 3, NULL, NULL, 2, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:04');
INSERT INTO `permission` VALUES (14, '角色编辑', 'role:update', 2, 3, NULL, NULL, 3, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:06');
INSERT INTO `permission` VALUES (15, '角色删除', 'role:delete', 2, 3, NULL, NULL, 4, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:08');
INSERT INTO `permission` VALUES (16, '角色权限分配', 'role:assign', 2, 3, NULL, NULL, 5, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:10');
INSERT INTO `permission` VALUES (17, '权限查询', 'permission:query', 2, 4, NULL, NULL, 1, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:15');
INSERT INTO `permission` VALUES (18, '权限创建', 'permission:create', 2, 4, NULL, NULL, 2, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:18');
INSERT INTO `permission` VALUES (19, '权限编辑', 'permission:update', 2, 4, NULL, NULL, 3, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:23');
INSERT INTO `permission` VALUES (20, '权限删除', 'permission:delete', 2, 4, NULL, NULL, 4, 1, '2025-06-01 21:39:12', '2025-06-08 22:45:26');
INSERT INTO `permission` VALUES (21, '商品查询', 'product:query', 2, 5, NULL, NULL, 1, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (22, '商品创建', 'product:create', 2, 5, NULL, NULL, 2, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (23, '商品编辑', 'product:update', 2, 5, NULL, NULL, 3, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (24, '商品删除', 'product:delete', 2, 5, NULL, NULL, 4, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (25, '分类查询', 'product:category:query', 2, 6, NULL, NULL, 1, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (26, '分类创建', 'product:category:create', 2, 6, NULL, NULL, 2, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (27, '分类编辑', 'product:category:update', 2, 6, NULL, NULL, 3, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (28, '分类删除', 'product:category:delete', 2, 6, NULL, NULL, 4, 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `permission` VALUES (29, '订单管理', 'order:manage', 1, NULL, '/orders', 'shopping-cart', 3, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (30, '订单列表', 'order:list', 2, 29, '/orders/list', NULL, 1, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (31, '订单详情', 'order:detail', 2, 29, '/orders/detail', NULL, 2, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (32, '订单发货', 'order:ship', 2, 29, '/orders/ship', NULL, 3, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (33, '订单退款', 'order:refund', 2, 29, '/orders/refund', NULL, 4, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (34, '数据统计', 'statistics:manage', 1, NULL, '/statistics', 'bar-chart', 4, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (35, '销售统计', 'statistics:sales', 2, 34, '/statistics/sales', NULL, 1, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (36, '商品统计', 'statistics:product', 2, 34, '/statistics/product', NULL, 2, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (37, '用户统计', 'statistics:user', 2, 34, '/statistics/user', NULL, 3, 1, '2025-06-08 21:50:23', '2025-06-08 21:50:23');
INSERT INTO `permission` VALUES (39, '系统信息', 'system:monitor', 2, 1, NULL, NULL, 0, 1, '2025-06-08 22:36:39', '2025-06-08 22:36:57');
INSERT INTO `permission` VALUES (40, '创建订单', 'order:create', 2, 29, NULL, NULL, 0, 1, '2025-06-08 22:38:58', '2025-06-08 22:39:27');
INSERT INTO `permission` VALUES (41, '订单查询', 'order:query', 2, 29, NULL, NULL, 0, 1, '2025-06-08 22:40:05', '2025-06-08 22:40:05');
INSERT INTO `permission` VALUES (42, '订单更新', 'order:update', 2, 29, NULL, NULL, 0, 1, '2025-06-08 22:40:37', '2025-06-08 22:40:37');
INSERT INTO `permission` VALUES (43, '订单删除', 'order:delete', 2, 29, NULL, NULL, 0, 1, '2025-06-08 22:41:14', '2025-06-08 22:41:14');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品描述',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `main_image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主图URL',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 'iPhone 15 Pro', 'Apple最新旗舰手机，搭载A17 Pro芯片1', 1, 7999.10, 52, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/caaea5fd-2e76-4000-be3a-666ac9213031.jpg', '2025-06-01 20:14:49', '2025-06-27 22:47:47');
INSERT INTO `product` VALUES (2, '华为 Mate 60 Pro', '华为旗舰手机，搭载麒麟9000S芯片', 1, 6999.00, 100, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/7ddf394a-4375-44b7-9694-25f47eb70315.jpg', '2025-06-01 20:14:49', '2025-06-20 19:32:37');
INSERT INTO `product` VALUES (3, 'MacBook Pro 14', 'Apple专业级笔记本电脑，M3 Pro芯片', 2, 14999.00, 50, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/3f69c79e-bf38-4374-8d91-07b82ac81f50.jpg', '2025-06-01 20:14:49', '2025-06-20 19:33:02');
INSERT INTO `product` VALUES (4, '联想 ThinkPad X1', '商务办公笔记本电脑', 2, 9999.00, 50, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/0396a4cc-8eda-4372-b4b9-c2ad168c26f2.jpg', '2025-06-01 20:14:49', '2025-06-20 19:33:47');
INSERT INTO `product` VALUES (5, '索尼 65英寸 OLED电视', '4K HDR OLED智能电视', 3, 12999.00, 30, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/1e4b6e2e-e53a-4d7a-8765-b463bc1da687.jpg', '2025-06-01 20:14:49', '2025-06-20 19:34:13');
INSERT INTO `product` VALUES (6, '海尔 变频冰箱', '智能变频节能冰箱', 3, 4999.00, 40, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/6665208d-f153-4d3c-bb7a-2d283caa06b5.jpg', '2025-06-01 20:14:49', '2025-06-20 19:34:33');
INSERT INTO `product` VALUES (7, 'Nike Air Force 1', '经典运动鞋', 4, 899.00, 200, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/9eee4d39-5e04-47d4-a86e-45a942725eba.jpg', '2025-06-01 20:14:49', '2025-06-20 19:34:51');
INSERT INTO `product` VALUES (8, 'Adidas 运动套装', '休闲运动套装', 4, 599.10, 150, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/e54b12d0-733f-418a-87ee-a213431f7899.jpg', '2025-06-01 20:14:49', '2025-06-27 22:36:53');
INSERT INTO `product` VALUES (9, 'SK-II 护肤套装', '高端护肤套装', 5, 2999.00, 80, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/6a36f5ad-4586-4c82-bc37-832cc3d1cd7e.jpg', '2025-06-01 20:14:49', '2025-06-20 19:35:38');
INSERT INTO `product` VALUES (10, '雅诗兰黛 小棕瓶', '明星精华液', 5, 999.00, 100, 1, 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/272d4ff3-19f1-484a-a332-2200a46c9060.jpg', '2025-06-01 20:14:49', '2025-06-20 19:35:58');
INSERT INTO `product` VALUES (11, '三只松鼠 坚果礼盒', '混合坚果零食礼盒', 6, 199.00, 300, 1, 'https://example.com/images/sanzhisongshu.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (12, '五粮液 52度', '浓香型白酒', 6, 1099.00, 100, 1, 'https://example.com/images/wuliangye.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (13, 'Kindle Paperwhite', '电子书阅读器', 7, 999.00, 50, 1, 'https://example.com/images/kindle.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (14, '索尼 WH-1000XM5', '无线降噪耳机', 7, 2999.00, 50, 1, 'https://example.com/images/sonyxm5.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (15, 'Nike 跑步鞋', '专业跑步鞋', 8, 799.00, 100, 1, 'https://example.com/images/nikerun.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (16, '李宁 运动套装', '专业运动套装', 8, 399.00, 150, 1, 'https://example.com/images/lining.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (17, '宜家 沙发', '北欧风格三人沙发', 9, 2999.00, 20, 1, 'https://example.com/images/ikeasofa.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (18, '小米 智能台灯', '护眼智能台灯', 9, 199.00, 100, 1, 'https://example.com/images/milight.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (19, '飞利浦 电动牙刷', '声波电动牙刷', 10, 399.00, 200, 1, 'https://example.com/images/philips.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');
INSERT INTO `product` VALUES (20, '乐高 积木玩具', '创意积木玩具', 10, 599.00, 100, 1, 'https://example.com/images/lego.jpg', '2025-06-01 20:14:49', '2025-06-01 20:14:49');

-- ----------------------------
-- Table structure for product_sku
-- ----------------------------
DROP TABLE IF EXISTS `product_sku`;
CREATE TABLE `product_sku`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU名称',
  `attributes` json NULL COMMENT 'SKU属性',
  `price` decimal(10, 2) NOT NULL COMMENT 'SKU价格',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `product_sku_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品SKU表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_sku
-- ----------------------------
INSERT INTO `product_sku` VALUES (1, 1, 'IP15P-001', 'iPhone 15 Pro 256GB 黑色', '{\"color\": \"黑色\", \"storage\": \"256GB\"}', 7999.00, 2, 1, '2025-06-27 22:47:47', '2025-06-27 22:47:47');
INSERT INTO `product_sku` VALUES (2, 1, 'IP15P-002', 'iPhone 15 Pro 512GB 白色', '{\"color\": \"白色\", \"storage\": \"512GB\"}', 8999.00, 50, 1, '2025-06-27 22:47:47', '2025-06-27 22:47:47');
INSERT INTO `product_sku` VALUES (3, 2, 'M60P-001', 'Mate 60 Pro 256GB 黑色', '{\"color\": \"黑色\", \"storage\": \"256GB\"}', 6999.00, 49, 1, '2025-06-20 19:32:37', '2025-06-20 19:32:37');
INSERT INTO `product_sku` VALUES (4, 2, 'M60P-002', 'Mate 60 Pro 512GB 白色', '{\"color\": \"白色\", \"storage\": \"512GB\"}', 7999.00, 50, 1, '2025-06-20 19:32:37', '2025-06-20 19:32:37');
INSERT INTO `product_sku` VALUES (5, 3, 'MBP14-001', 'MacBook Pro 14 M3 Pro 16GB 512GB', '{\"memory\": \"16GB\", \"storage\": \"512GB\", \"processor\": \"M3 Pro\"}', 14999.00, 25, 1, '2025-06-20 19:33:02', '2025-06-20 19:33:02');
INSERT INTO `product_sku` VALUES (6, 3, 'MBP14-002', 'MacBook Pro 14 M3 Pro 32GB 1TB', '{\"memory\": \"32GB\", \"storage\": \"1TB\", \"processor\": \"M3 Pro\"}', 17999.00, 25, 1, '2025-06-20 19:33:02', '2025-06-20 19:33:02');
INSERT INTO `product_sku` VALUES (7, 4, 'TPX1-001', 'ThinkPad X1 i7 16GB 512GB', '{\"memory\": \"16GB\", \"storage\": \"512GB\", \"processor\": \"i7\"}', 9999.00, 25, 1, '2025-06-20 19:33:47', '2025-06-20 19:33:47');
INSERT INTO `product_sku` VALUES (8, 4, 'TPX1-002', 'ThinkPad X1 i7 32GB 1TB', '{\"memory\": \"32GB\", \"storage\": \"1TB\", \"processor\": \"i7\"}', 11999.00, 25, 1, '2025-06-20 19:33:47', '2025-06-20 19:33:47');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', 'ADMIN', '系统超级管理员', 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');
INSERT INTO `role` VALUES (2, '普通用户', 'USER', '普通用户', 1, '2025-06-01 21:39:12', '2025-06-01 21:39:12');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_permission`(`role_id` ASC, `permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (162, 3, 39, '2025-06-08 22:37:30');
INSERT INTO `role_permission` VALUES (163, 3, 40, '2025-06-08 22:43:14');
INSERT INTO `role_permission` VALUES (164, 3, 41, '2025-06-08 22:43:18');
INSERT INTO `role_permission` VALUES (165, 3, 42, '2025-06-08 22:43:22');
INSERT INTO `role_permission` VALUES (166, 3, 43, '2025-06-08 22:43:27');
INSERT INTO `role_permission` VALUES (190, 2, 39, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (191, 2, 5, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (192, 2, 6, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (193, 2, 25, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (194, 2, 26, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (195, 2, 27, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (196, 2, 28, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (197, 2, 21, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (198, 2, 7, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (199, 2, 22, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (200, 2, 23, '2025-06-14 19:46:05');
INSERT INTO `role_permission` VALUES (201, 2, 24, '2025-06-14 19:46:05');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像URL',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'zs', '$2a$10$v4rv6/qxTN0qPAnP2rZgUONiNPrbNV2DLnMLX.2viRGFUjjNX792m', 'aass', '111111111@qq.com', '13345678900', 1, '2025-05-25 21:10:45', '2025-05-25 21:10:45', NULL);
INSERT INTO `sys_user` VALUES (2, 'aaa', '$2a$10$uVQDlXfIAooUJLOHSXeCj.2rfwk5e8ppNuQ8/yp.mqWTdmzqQ8qxa', 'aass', '111111111@qq.com', '13345678900', 1, '2025-05-27 22:05:33', '2025-06-28 15:08:57', 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/1ad2ebc6-febb-4433-a79e-ee505304b5d8.png');
INSERT INTO `sys_user` VALUES (3, 'admin', '$2a$10$v4rv6/qxTN0qPAnP2rZgUONiNPrbNV2DLnMLX.2viRGFUjjNX792m', '系统管理员', 'admin111@example.com', '13999999998', 1, '2025-06-01 21:39:21', '2025-06-28 15:01:43', 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/925cedc9-3ac8-4e59-a2a7-0981500f89a7.jpg');
INSERT INTO `sys_user` VALUES (4, 'bbb', '$2a$10$uVQDlXfIAooUJLOHSXeCj.2rfwk5e8ppNuQ8/yp.mqWTdmzqQ8qxa', 'bbb', 'bbb@qq.com', '13333333333', 1, '2025-06-28 14:17:28', '2025-06-28 15:58:49', 'https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/1ad2ebc6-febb-4433-a79e-ee505304b5d8.png');
INSERT INTO `sys_user` VALUES (5, 'ccc', '$2a$10$L3OEQr.qZ1Wo0uLMsNoT3.s2/nU1ayzt51zYheDaU0p/h6w/I/zLu', 'ccc', 'bbb@qq.com', '13999999998', 1, '2025-06-28 14:22:45', '2025-06-28 14:22:45', NULL);

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '实付金额',
  `freight_amount` decimal(10, 2) NOT NULL COMMENT '运费金额',
  `pay_type` tinyint NOT NULL COMMENT '支付方式：1-支付宝 2-微信 3-银联',
  `status` tinyint NOT NULL COMMENT '订单状态：0-待付款 1-待发货 2-待收货 3-已完成 4-已取消 5-已退款',
  `receiver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货地址',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '收货时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (7, '202403150001', 299.00, 299.00, 0.00, 1, 4, '张三', '13800138001', '北京市朝阳区xxx街道', '请尽快发货', '2024-03-15 10:00:00', '2024-03-15 14:00:00', '2024-03-17 16:00:00', '2024-03-15 09:30:00', '2025-06-20 20:20:31', '111');
INSERT INTO `t_order` VALUES (8, '202403150002', 599.00, 599.00, 0.00, 2, 3, '张三', '13800138001', '北京市朝阳区xxx街道', NULL, '2024-03-15 11:00:00', '2024-03-15 15:00:00', '2025-06-28 15:39:57', '2024-03-15 10:30:00', '2025-06-28 15:39:57', '11');
INSERT INTO `t_order` VALUES (9, '202403150003', 199.00, 199.00, 0.00, 1, 4, '李四', '13800138002', '上海市浦东新区xxx路', '周末送货', '2024-03-15 13:00:00', '2025-06-20 20:21:03', '2025-06-20 20:21:04', '2024-03-15 12:30:00', '2025-06-20 20:34:43', '11');
INSERT INTO `t_order` VALUES (10, '202403150004', 899.00, 899.00, 0.00, 3, 4, '李四', '13800138002', '上海市浦东新区xxx路', NULL, NULL, NULL, NULL, '2024-03-15 14:30:00', '2025-06-28 15:39:51', '11');
INSERT INTO `t_order` VALUES (11, '202403150005', 399.00, 399.00, 0.00, 1, 4, '王五', '13800138003', '广州市天河区xxx路', '取消订单', NULL, NULL, NULL, '2024-03-15 15:30:00', '2024-03-15 16:00:00', '11');
INSERT INTO `t_order` VALUES (12, 'ORD17510948103361154', 7999.01, 7999.01, 0.01, 1, 1, '111', '111', '111', '', '2025-06-28 15:39:41', NULL, NULL, '2025-06-28 15:13:30', '2025-06-28 15:39:41', '11');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (5, 2, 2, '2025-06-08 22:07:21');
INSERT INTO `user_role` VALUES (6, 3, 1, '2025-06-08 22:07:25');
INSERT INTO `user_role` VALUES (7, 1, 1, '2025-06-08 22:07:29');
INSERT INTO `user_role` VALUES (8, 4, 1, '2025-06-28 15:12:14');

SET FOREIGN_KEY_CHECKS = 1;
