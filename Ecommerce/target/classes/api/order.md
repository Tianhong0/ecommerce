# 订单管理API文档

## 创建订单
- 请求路径：`POST /orders`
- 请求权限：`order:create`
- 请求参数：
```json
{
    "userId": "用户ID",
    "totalAmount": "订单总金额",
    "payAmount": "实付金额",
    "freightAmount": "运费金额",
    "payType": "支付方式：1-支付宝 2-微信 3-银联",
    "receiverName": "收货人姓名",
    "receiverPhone": "收货人电话",
    "receiverAddress": "收货地址",
    "note": "订单备注",
    "orderItems": [
        {
            "productId": "商品ID",
            "skuId": "SKU ID",
            "productName": "商品名称",
            "skuName": "SKU名称",
            "productPic": "商品图片",
            "price": "商品单价",
            "quantity": "购买数量",
            "totalAmount": "总金额"
        }
    ]
}
```
- 响应结果：
```json
{
    "success": true,
    "message": "创建成功",
    "data": {
        "id": "订单ID",
        "orderNo": "订单编号",
        "userId": "用户ID",
        "totalAmount": "订单总金额",
        "payAmount": "实付金额",
        "freightAmount": "运费金额",
        "payType": "支付方式",
        "status": "订单状态：0-待付款 1-待发货 2-待收货 3-已完成 4-已取消 5-已退款",
        "receiverName": "收货人姓名",
        "receiverPhone": "收货人电话",
        "receiverAddress": "收货地址",
        "note": "订单备注",
        "payTime": "支付时间",
        "deliveryTime": "发货时间",
        "receiveTime": "收货时间",
        "createTime": "创建时间",
        "updateTime": "更新时间",
        "orderItems": [
            {
                "id": "订单项ID",
                "orderId": "订单ID",
                "productId": "商品ID",
                "skuId": "SKU ID",
                "productName": "商品名称",
                "skuName": "SKU名称",
                "productPic": "商品图片",
                "price": "商品单价",
                "quantity": "购买数量",
                "totalAmount": "总金额",
                "createTime": "创建时间",
                "updateTime": "更新时间"
            }
        ]
    }
}
```

## 分页查询订单
- 请求路径：`GET /orders/page`
- 请求权限：`order:query`
- 请求参数：
  - pageNum：页码，默认1
  - pageSize：每页数量，默认10
  - orderNo：订单编号（可选）
  - status：订单状态（可选）
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "records": [
            {
                "id": "订单ID",
                "orderNo": "订单编号",
                // ... 订单详细信息
            }
        ],
        "total": "总记录数",
        "size": "每页数量",
        "current": "当前页码"
    }
}
```

## 获取订单详情
- 请求路径：`GET /orders/{id}`
- 请求权限：`order:query`
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "id": "订单ID",
        "orderNo": "订单编号",
        // ... 订单详细信息
    }
}
```

## 更新订单状态
- 请求路径：`PUT /orders/{id}/status`
- 请求权限：`order:update`
- 请求参数：
  - status：订单状态
- 响应结果：
```json
{
    "success": true,
    "message": "状态更新成功"
}
```

## 删除订单
- 请求路径：`DELETE /orders/{id}`
- 请求权限：`order:delete`
- 响应结果：
```json
{
    "success": true,
    "message": "删除成功"
}
```

## 获取订单物流信息
- 请求路径：`GET /orders/{id}/delivery`
- 请求权限：`order:query`
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "id": "物流ID",
        "orderId": "订单ID",
        "deliveryCompany": "物流公司",
        "deliverySn": "物流单号",
        "receiverName": "收货人姓名",
        "receiverPhone": "收货人电话",
        "receiverAddress": "收货地址",
        "status": "物流状态：0-待发货 1-已发货 2-已签收",
        "deliveryInfo": "物流信息",
        "deliveryTime": "发货时间",
        "receiveTime": "签收时间",
        "createTime": "创建时间",
        "updateTime": "更新时间"
    }
}
```

## 更新订单物流信息
- 请求路径：`PUT /orders/{id}/delivery`
- 请求权限：`order:update`
- 请求参数：
```json
{
    "deliveryCompany": "物流公司",
    "deliverySn": "物流单号",
    "receiverName": "收货人姓名",
    "receiverPhone": "收货人电话",
    "receiverAddress": "收货地址",
    "deliveryInfo": "物流信息"
}
```
- 响应结果：
```json
{
    "success": true,
    "message": "物流信息更新成功"
}
```

## 更新物流状态
- 请求路径：`PUT /orders/{id}/delivery/status`
- 请求权限：`order:update`
- 请求参数：
  - status：物流状态
- 响应结果：
```json
{
    "success": true,
    "message": "物流状态更新成功"
}
```

## 获取订单退款信息
- 请求路径：`GET /orders/{id}/refund`
- 请求权限：`order:query`
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "id": "退款ID",
        "orderId": "订单ID",
        "refundNo": "退款单号",
        "refundAmount": "退款金额",
        "status": "退款状态：0-待处理 1-已同意 2-已拒绝 3-已完成",
        "reason": "退款原因",
        "description": "退款说明",
        "proofImages": "凭证图片",
        "rejectReason": "拒绝原因",
        "applyTime": "申请时间",
        "handleTime": "处理时间",
        "completeTime": "完成时间",
        "createTime": "创建时间",
        "updateTime": "更新时间"
    }
}
```

## 申请退款
- 请求路径：`POST /orders/{id}/refund`
- 请求权限：`order:refund`
- 请求参数：
```json
{
    "refundAmount": "退款金额",
    "reason": "退款原因",
    "description": "退款说明",
    "proofImages": "凭证图片"
}
```
- 响应结果：
```json
{
    "success": true,
    "message": "退款申请提交成功"
}
```

## 处理退款
- 请求路径：`PUT /orders/{id}/refund`
- 请求权限：`order:refund`
- 请求参数：
  - status：退款状态
  - rejectReason：拒绝原因（当status=2时必填）
- 响应结果：
```json
{
    "success": true,
    "message": "退款处理成功"
}
```

## 获取最近订单
- 请求路径：`GET /orders/recent`
- 请求权限：`order:query`
- 请求参数：
  - limit：获取数量，默认10
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": [
        {
            "id": "订单ID",
            "orderNo": "订单编号",
            // ... 订单详细信息
        }
    ]
}
```

## 获取订单数量
- 请求路径：`GET /orders/count`
- 请求权限：`order:query`
- 请求参数：
  - status：订单状态（可选）
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": "订单数量"
}
```

## 错误处理
- 400: 参数错误
- 404: 订单不存在
- 500: 服务器内部错误 