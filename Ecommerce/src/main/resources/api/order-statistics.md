# 订单统计模块API文档

## 获取订单统计数据
- 请求路径：`GET /orders/statistics`
- 请求权限：`order:query`
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "totalSales": "总销售额（已完成订单）",
        "totalOrderCount": "总订单数",
        "totalProductCount": "总商品数量",
        "totalRefundAmount": "总退款金额",
        "totalRefundCount": "总退款订单数",
        "orderStatusCount": {
            "0": "待付款订单数",
            "1": "待发货订单数",
            "2": "待收货订单数",
            "3": "已完成订单数",
            "4": "已取消订单数",
            "5": "已退款订单数"
        },
        "payTypeCount": {
            "1": "支付宝支付订单数",
            "2": "微信支付订单数",
            "3": "银联支付订单数"
        },
        "payTypeAmount": {
            "1": "支付宝支付总金额",
            "2": "微信支付总金额",
            "3": "银联支付总金额"
        }
    }
}
```

### 字段说明

#### 订单状态（orderStatusCount）
- 0: 待付款
- 1: 待发货
- 2: 待收货
- 3: 已完成
- 4: 已取消
- 5: 已退款

#### 支付方式（payTypeCount/payTypeAmount）
- 1: 支付宝
- 2: 微信
- 3: 银联

### 统计说明
1. 总销售额（totalSales）
   - 只统计状态为"已完成"的订单
   - 使用订单的实付金额（payAmount）计算

2. 总订单数（totalOrderCount）
   - 统计所有状态的订单数量

3. 总商品数量（totalProductCount）
   - 统计所有订单项的商品数量总和
   - 通过订单项表（order_item）的quantity字段累加

4. 退款统计
   - 总退款金额（totalRefundAmount）：只统计状态为"已完成"的退款
   - 总退款订单数（totalRefundCount）：只统计状态为"已完成"的退款

5. 订单状态分布（orderStatusCount）
   - 统计各状态订单的数量分布

6. 支付方式分布
   - 支付方式订单数（payTypeCount）：统计各支付方式的订单数量
   - 支付方式金额（payTypeAmount）：统计各支付方式的支付总金额 