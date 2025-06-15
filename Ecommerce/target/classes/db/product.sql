-- 商品分类表
CREATE TABLE `category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(50) NOT NULL COMMENT '分类名称',
    `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
    `sort` int NOT NULL DEFAULT '0' COMMENT '排序值',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_sort` (`sort`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE `product` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_id` bigint NOT NULL COMMENT '分类ID',
    `name` varchar(100) NOT NULL COMMENT '商品名称',
    `description` text COMMENT '商品描述',
    `main_image` varchar(255) NOT NULL COMMENT '主图URL',
    `price` decimal(10,2) NOT NULL COMMENT '商品价格',
    `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-下架 1-上架',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 商品SKU表
CREATE TABLE `product_sku` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id` bigint NOT NULL COMMENT '商品ID',
    `sku_code` varchar(50) NOT NULL COMMENT 'SKU编码',
    `attributes` json NOT NULL COMMENT '规格属性JSON',
    `price` decimal(10,2) NOT NULL COMMENT 'SKU价格',
    `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku_code` (`sku_code`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 添加外键约束（可选）
ALTER TABLE `product` 
ADD CONSTRAINT `fk_product_category` 
FOREIGN KEY (`category_id`) REFERENCES `category` (`id`);

ALTER TABLE `product_sku` 
ADD CONSTRAINT `fk_sku_product` 
FOREIGN KEY (`product_id`) REFERENCES `product` (`id`); 