# 系统监控 API 文档

## 1. 获取系统资源信息
- **接口地址**: `/system/resources`
- **请求方法**: GET
- **响应示例**:
```json
{
  "cpuUsage": 45.5,
  "memoryUsage": 65.3,
  "diskUsage": 75.2,
  "networkIO": {
    "sent": 1024000,
    "received": 2048000
  },
  "timestamp": "2023-12-01T10:00:00"
}
```

## 2. 获取系统日志
- **接口地址**: `/system/logs`
- **请求方法**: GET
- **请求参数**:
  - `pageNum` (可选, 默认1): 页码
  - `pageSize` (可选, 默认10): 每页数量
  - `level` (可选): 日志级别 (INFO, WARN, ERROR)
  - `startTime` (可选): 开始时间
  - `endTime` (可选): 结束时间
- **响应示例**:
```json
{
  "records": [
    {
      "id": 1,
      "level": "ERROR",
      "message": "数据库连接失败",
      "timestamp": "2023-12-01T09:45:00",
      "traceId": "trace-123456"
    }
  ],
  "total": 10,
  "size": 10,
  "current": 1
}
```

## 3. 获取性能指标
- **接口地址**: `/system/metrics`
- **请求方法**: GET
- **响应示例**:
```json
{
  "requestCount": 10000,
  "averageResponseTime": 150,
  "errorRate": 0.5,
  "activeConnections": 50,
  "timestamp": "2023-12-01T10:00:00"
}
```

## 4. 获取应用健康状态
- **接口地址**: `/system/health`
- **请求方法**: GET
- **响应示例**:
```json
{
  "status": "UP",
  "components": {
    "database": "UP",
    "redis": "UP",
    "kafka": "UP"
  },
  "details": {
    "database": {
      "status": "UP",
      "connections": 10
    },
    "redis": {
      "status": "UP",
      "connectedClients": 5
    }
  }
}
```

## 5. 获取系统配置
- **接口地址**: `/system/config`
- **请求方法**: GET
- **响应示例**:
```json
{
  "applicationName": "Ecommerce Admin System",
  "version": "1.0.3",
  "environment": "production",
  "javaVersion": "17",
  "springBootVersion": "2.7.5"
}
```

## 错误处理
- 400: 参数错误
- 403: 权限不足
- 500: 服务器内部错误 