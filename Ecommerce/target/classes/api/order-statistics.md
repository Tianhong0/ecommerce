# 订单统计 API 文档

## 1. 销售总览
- **接口地址**: `/order/statistics/overview`
- **请求方法**: GET
- **请求参数**:
  - `startDate` (可选): 开始日期 (YYYY-MM-DD)
  - `endDate` (可选): 结束日期 (YYYY-MM-DD)
- **响应示例**:
```json
{
  "totalSales": 1000000.00,
  "totalOrders": 5000,
  "averageOrderValue": 200.00,
  "salesTrend": [
    {"date": "2023-11-01", "sales": 50000.00},
    {"date": "2023-11-02", "sales": 55000.00}
  ]
}
```

## 2. 商品销售排行
- **接口地址**: `/order/statistics/product-ranking`
- **请求方法**: GET
- **请求参数**:
  - `topN` (可选, 默认10): 排名前N的商品
  - `startDate` (可选): 开始日期
  - `endDate` (可选): 结束日期
- **响应示例**:
```json
{
  "ranking": [
    {
      "productId": 1,
      "productName": "智能手机",
      "totalSales": 500000.00,
      "salesQuantity": 1000,
      "rank": 1
    },
    {
      "productId": 2,
      "productName": "笔记本电脑",
      "totalSales": 400000.00,
      "salesQuantity": 800,
      "rank": 2
    }
  ]
}
```

## 3. 订单状态分布
- **接口地址**: `/order/statistics/status-distribution`
- **请求方法**: GET
- **请求参数**:
  - `startDate` (可选): 开始日期
  - `endDate` (可选): 结束日期
- **响应示例**:
```json
{
  "statusDistribution": [
    {"status": 0, "name": "待付款", "count": 500, "percentage": 10},
    {"status": 1, "name": "待发货", "count": 1000, "percentage": 20},
    {"status": 2, "name": "待收货", "count": 2000, "percentage": 40},
    {"status": 3, "name": "已完成", "count": 1000, "percentage": 20},
    {"status": 4, "name": "已取消", "count": 500, "percentage": 10}
  ]
}
```

## 4. 用户消费分析
- **接口地址**: `/order/statistics/user-consumption`
- **请求方法**: GET
- **请求参数**:
  - `startDate` (可选): 开始日期
  - `endDate` (可选): 结束日期
- **响应示例**:
```json
{
  "userConsumptionTiers": [
    {
      "tier": "高消费用户",
      "totalAmount": 500000.00,
      "userCount": 50,
      "averageConsumption": 10000.00
    },
    {
      "tier": "中等消费用户",
      "totalAmount": 300000.00,
      "userCount": 200,
      "averageConsumption": 1500.00
    },
    {
      "tier": "低消费用户",
      "totalAmount": 100000.00,
      "userCount": 500,
      "averageConsumption": 200.00
    }
  ]
}
```

## 5. 退款统计
- **接口地址**: `/order/statistics/refund`
- **请求方法**: GET
- **请求参数**:
  - `startDate` (可选): 开始日期
  - `endDate` (可选): 结束日期
- **响应示例**:
```json
{
  "totalRefundAmount": 50000.00,
  "refundCount": 100,
  "refundRate": 2.0,
  "refundReasons": [
    {"reason": "商品质量问题", "count": 40, "percentage": 40},
    {"reason": "与描述不符", "count": 30, "percentage": 30},
    {"reason": "物流问题", "count": 20, "percentage": 20},
    {"reason": "其他", "count": 10, "percentage": 10}
  ]
}
```

## 错误处理
- 400: 参数错误
- 403: 权限不足
- 500: 服务器内部错误 