# Ecommerce 电商后台管理系统

## 项目简介

本项目是一个基于 Spring Boot 的企业级电商后台管理系统，提供了完整的电商业务管理解决方案。系统包括用户管理、商品管理、订单管理、权限控制和系统监控等核心功能模块。

## 技术栈

- **后端框架**
  - Spring Boot 2.7.5
  - Spring Security
  - MyBatis Plus
  - Kafka
  - Redis

- **数据库**
  - MySQL 8.0+
  - Flyway (数据库版本控制)

- **缓存与消息**
  - Redis
  - Apache Kafka

- **安全**
  - JWT 认证
  - Spring Security

- **工具**
  - Maven
  - Lombok
  - Swagger/OpenAPI

## 系统架构

```
├── src
│   ├── main
│   │   ├── java                # 核心业务代码
│   │   │   └── com
│   │   │       └── example
│   │   │           └── admin
│   │   │               ├── config         # 配置类
│   │   │               ├── controller     # 控制器
│   │   │               ├── service        # 服务层
│   │   │               ├── mapper         # 数据访问层
│   │   │               ├── entity         # 实体类
│   │   │               └── security       # 安全相关
│   │   └── resources
│   │       ├── api             # API文档
│   │       ├── db              # 数据库迁移脚本
│   │       └── application.yml # 应用配置
└── target                      # 编译输出目录
```

## 主要功能模块

1. **用户管理**
   - 用户注册、登录
   - 角色和权限控制
   - 用户信息管理

2. **商品管理**
   - 商品CRUD
   - 商品分类
   - SKU管理
   - 库存追踪

3. **订单管理**
   - 订单创建
   - 订单状态追踪
   - 订单退款处理

4. **系统监控**
   - 资源监控
   - 性能指标
   - 日志管理

## 依赖列表

### 核心依赖
```xml
<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- MyBatis Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.2</version>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.9.1</version>
    </dependency>

    <!-- Kafka -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>

    <!-- Redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <!-- Flyway -->
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+
- Kafka 2.8+

## 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/your-username/ecommerce-admin-system.git
cd ecommerce-admin-system
```

### 2. 配置数据库
1. 创建 MySQL 数据库
```sql
CREATE DATABASE ecommerce_admin;
```

2. 修改 `src/main/resources/application.yml` 中的数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_admin
    username: your_username
    password: your_password
```

### 3. 配置 Redis
修改 `application.yml` 中的 Redis 配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

### 4. 配置 Kafka
修改 `application.yml` 中的 Kafka 配置
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
```

### 5. 运行数据库迁移
```bash
mvn flyway:migrate
```

### 6. 启动应用
```bash
mvn spring-boot:run
```

## 测试

### 运行单元测试
```bash
mvn test
```

### 运行集成测试
```bash
mvn verify
```

## 部署

### 打包
```bash
mvn clean package
```

### 生产环境
建议使用 Docker 容器化部署，详细配置请参考 `Dockerfile`

## API 文档

详细的 API 文档位于 `src/main/resources/api/` 目录，包括各模块的接口定义、请求/响应示例。

## 安全

- 使用 JWT 进行身份认证
- 基于角色的权限控制
- 密码使用 BCrypt 加密
- 防止 SQL 注入和 XSS 攻击

## 性能优化

- Redis 缓存
- Kafka 异步消息处理
- MyBatis Plus 查询优化
- 连接池配置

## 监控与日志

- 使用 Spring Boot Actuator 进行系统监控
- 集成 Logback 进行日志管理
- 提供系统资源和性能指标监控接口

## 贡献指南

1. Fork 项目
2. 创建 feature 分支 (`git checkout -b feature/AmazingFeature`)
3. 提交代码 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

本项目基于 MIT 许可证开源。详见 `LICENSE` 文件。

