# 电商系统

## 项目介绍
这是一个基于Spring Boot的电商系统，实现了用户权限管理、商品管理、订单管理等核心功能。

## Kafka消息队列的应用

本项目使用Kafka消息队列解决以下问题：

### 1. 异步处理订单流程
- 订单创建后通过Kafka异步处理状态变更
- 订单支付、发货、完成等状态变更通过消息队列异步通知
- 解耦了订单处理的各个环节，提高系统响应速度

### 2. 库存管理优化
- 库存扣减和归还通过消息队列异步处理
- 实现了库存预警机制，当库存低于阈值时自动发送预警消息
- 订单取消时自动归还库存，保证数据一致性

### 3. 日志收集与分析
- 通过AOP切面收集用户操作日志
- 收集系统错误日志
- 通过Kafka将日志异步写入文件系统，不影响主业务流程

### 4. 峰值流量削峰填谷
- 订单创建和处理通过消息队列缓冲
- 库存操作通过消息队列匀速处理
- 日志处理不影响主业务流程
- 提高系统在高并发场景下的稳定性

## Kafka主题设计

### 订单相关主题
- `topic-order-create`: 订单创建
- `topic-order-pay`: 订单支付
- `topic-order-delivery`: 订单发货
- `topic-order-complete`: 订单完成
- `topic-order-cancel`: 订单取消

### 库存相关主题
- `topic-stock-deduct`: 库存扣减
- `topic-stock-return`: 库存归还
- `topic-stock-alert`: 库存预警

### 日志相关主题
- `topic-log-user-action`: 用户行为日志
- `topic-log-system`: 系统日志
- `topic-log-error`: 错误日志

## 技术栈
- Spring Boot 3.1.0
- Spring Security
- MyBatis Plus
- Redis
- Kafka
- MySQL

## 运行环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Kafka 3.0+

## 启动步骤
1. 确保MySQL、Redis和Kafka服务已启动
2. 修改`application.yml`中的数据库、Redis和Kafka配置
3. 执行`mvn spring-boot:run`启动应用

## 项目结构
```
src/main/java/com/example/admin/
├── aspect/           # 切面
├── config/           # 配置类
├── constant/         # 常量类
├── controller/       # 控制器
├── dto/              # 数据传输对象
│   └── message/      # 消息模型
├── entity/           # 实体类
├── exception/        # 异常处理
├── mapper/           # 数据访问层
├── security/         # 安全配置
├── service/          # 服务层
│   └── impl/         # 服务实现
└── util/             # 工具类
```