# 电商后台管理系统

## 项目介绍
这是一个基于Spring Boot 3.x开发的电商后台管理系统，提供了完整的商品管理、订单管理、用户管理等功能。系统采用前后端分离架构，后端提供RESTful API接口。

## 技术栈
- 核心框架：Spring Boot 3.1.0
- 安全框架：Spring Security + JWT
- 持久层框架：MyBatis Plus 3.5.3
- 数据库：MySQL
- 缓存：Redis
- 消息队列：Kafka
- 对象存储：阿里云OSS
- 其他：Lombok、AOP等

## 主要功能
### 1. 用户管理
- 用户注册、登录
- 基于JWT的认证授权
- 用户权限管理
- 用户信息管理

### 2. 商品管理
- 商品分类管理
- 商品信息管理
- 商品SKU管理
- 商品库存管理
- 商品图片上传（阿里云OSS）

### 3. 订单管理
- 订单创建和管理
- 订单状态流转
- 订单支付管理
- 订单退款管理
- 订单物流管理

### 4. 系统功能
- 系统监控（CPU、内存等）
- 操作日志记录
- 异常处理
- 数据统计

## 项目结构
```
src/main/java/com/example/admin/
├── aspect/        # AOP切面，用于日志记录等
├── config/        # 配置类
├── constant/      # 常量定义
├── controller/    # 控制器
├── dto/          # 数据传输对象
├── entity/       # 实体类
├── exception/    # 异常处理
├── mapper/       # MyBatis映射接口
├── security/     # 安全相关
├── service/      # 业务逻辑
└── util/         # 工具类
```

## 核心特性
1. 安全性
   - 基于Spring Security的认证授权
   - JWT token认证
   - 细粒度的权限控制

2. 可扩展性
   - 模块化设计
   - 统一的异常处理
   - 规范的API接口

3. 性能优化
   - Redis缓存
   - 异步消息处理
   - 数据库优化

4. 可维护性
   - 统一的日志记录
   - 规范的代码结构
   - 完善的注释文档

## 快速开始
1. 环境要求
   - JDK 17+
   - MySQL 8.0+
   - Redis 6.0+
   - Kafka 2.8+
   - Maven 3.6+

2. 配置修改
   - 修改application.yml中的数据库配置
   - 修改Redis配置
   - 修改Kafka配置
   - 配置阿里云OSS

3. 运行项目
   ```bash
   mvn spring-boot:run
   ```

## API文档
详细的API文档请参考 `src/main/resources/api/` 目录下的文档：
- product.md：商品管理API
- order.md：订单管理API
- user.md：用户管理API

## 异常处理
系统采用统一的异常处理机制：
- BusinessException：业务异常
- GlobalExceptionHandler：全局异常处理器
- 统一的响应格式：
  ```json
  {
    "success": true/false,
    "message": "提示信息",
    "data": {}
  }
  ```

## 日志系统
- 操作日志：记录用户的关键操作
- 错误日志：记录系统异常
- 使用Kafka进行日志收集

## 贡献指南
1. Fork 本仓库
2. 创建新的功能分支
3. 提交代码
4. 创建 Pull Request

## 许可证
MIT License
