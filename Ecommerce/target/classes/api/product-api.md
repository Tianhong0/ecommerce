# 商品管理模块API文档

## 1. 分类管理接口

### 1.1 创建分类
- 请求方式：POST
- 接口路径：`/category`
- 请求参数：
```json
{
    "name": "分类名称",        // 必填，字符串
    "description": "分类描述", // 选填，字符串
    "sort": 1,              // 必填，整数，排序值
    "status": 1             // 必填，整数，0-禁用 1-启用
}
```
- 响应示例：
```json
{
    "success": true,
    "message": "创建成功",
    "data": {
        "id": 1,
        "name": "分类名称",
        "description": "分类描述",
        "sort": 1,
        "status": 1,
        "createTime": "2024-01-01 12:00:00",
        "updateTime": "2024-01-01 12:00:00"
    }
}
```

### 1.2 更新分类
- 请求方式：PUT
- 接口路径：`/category/{id}`
- 路径参数：
  - `id`: 分类ID
- 请求参数：同创建分类
- 响应示例：同创建分类

### 1.3 删除分类
- 请求方式：DELETE
- 接口路径：`/category/{id}`
- 路径参数：
  - `id`: 分类ID
- 响应示例：
```json
{
    "success": true,
    "message": "删除成功",
    "data": null
}
```

### 1.4 获取分类详情
- 请求方式：GET
- 接口路径：`/category/{id}`
- 路径参数：
  - `id`: 分类ID
- 响应示例：同创建分类

### 1.5 获取分类列表
- 请求方式：GET
- 接口路径：`/category`
- 请求参数：
  - `page`: 页码，默认1
  - `size`: 每页条数，默认10
  - `name`: 分类名称，选填，用于搜索
- 响应示例：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "records": [
            {
                "id": 1,
                "name": "分类名称",
                "description": "分类描述",
                "sort": 1,
                "status": 1,
                "createTime": "2024-01-01 12:00:00",
                "updateTime": "2024-01-01 12:00:00"
            }
        ],
        "total": 100,
        "size": 10,
        "current": 1
    }
}
```

### 1.6 更新分类状态
- 请求方式：PUT
- 接口路径：`/category/{id}/status`
- 路径参数：
  - `id`: 分类ID
- 请求参数：
  - `status`: 状态值，0-禁用 1-启用
- 响应示例：
```json
{
    "success": true,
    "message": "状态更新成功",
    "data": null
}
```

## 2. 商品管理接口

### 2.1 创建商品
- 请求方式：POST
- 接口路径：`/product`
- 请求参数：
```json
{
    "categoryId": 1,           // 必填，分类ID
    "name": "商品名称",         // 必填，字符串
    "description": "商品描述",  // 选填，字符串
    "mainImage": "主图URL",    // 必填，字符串
    "price": 99.99,           // 必填，大于0的数值
    "stock": 100,             // 必填，大于0的整数
    "status": 1,              // 必填，整数，0-下架 1-上架
    "skuList": [              // 选填，SKU列表
        {
            "skuCode": "SKU001",           // 必填，SKU编码
            "attributes": "{\"颜色\":\"红色\",\"尺寸\":\"XL\"}", // 必填，规格属性JSON字符串
            "price": 99.99,                // 必填，大于0的数值
            "stock": 100,                  // 必填，大于0的整数
            "status": 1                    // 必填，整数，0-禁用 1-启用
        }
    ]
}
```
- 响应示例：
```json
{
    "success": true,
    "message": "创建成功",
    "data": {
        "id": 1,
        "categoryId": 1,
        "name": "商品名称",
        "description": "商品描述",
        "mainImage": "主图URL",
        "price": 99.99,
        "stock": 100,
        "status": 1,
        "createTime": "2024-01-01 12:00:00",
        "updateTime": "2024-01-01 12:00:00"
    }
}
```

### 2.2 更新商品
- 请求方式：PUT
- 接口路径：`/product/{id}`
- 路径参数：
  - `id`: 商品ID
- 请求参数：同创建商品
- 响应示例：同创建商品

### 2.3 删除商品
- 请求方式：DELETE
- 接口路径：`/product/{id}`
- 路径参数：
  - `id`: 商品ID
- 响应示例：
```json
{
    "success": true,
    "message": "删除成功",
    "data": null
}
```

### 2.4 获取商品详情
- 请求方式：GET
- 接口路径：`/product/{id}`
- 路径参数：
  - `id`: 商品ID
- 响应示例：同创建商品

### 2.5 获取商品列表
- 请求方式：GET
- 接口路径：`/product`
- 请求参数：
  - `page`: 页码，默认1
  - `size`: 每页条数，默认10
  - `name`: 商品名称，选填，用于搜索
  - `categoryId`: 分类ID，选填，用于筛选
- 响应示例：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "records": [
            {
                "id": 1,
                "categoryId": 1,
                "name": "商品名称",
                "description": "商品描述",
                "mainImage": "主图URL",
                "price": 99.99,
                "stock": 100,
                "status": 1,
                "createTime": "2024-01-01 12:00:00",
                "updateTime": "2024-01-01 12:00:00"
            }
        ],
        "total": 100,
        "size": 10,
        "current": 1
    }
}
```

### 2.6 更新商品状态
- 请求方式：PUT
- 接口路径：`/product/{id}/status`
- 路径参数：
  - `id`: 商品ID
- 请求参数：
  - `status`: 状态值，0-下架 1-上架
- 响应示例：
```json
{
    "success": true,
    "message": "状态更新成功",
    "data": null
}
```

### 2.7 更新商品库存
- 请求方式：PUT
- 接口路径：`/product/{id}/stock`
- 路径参数：
  - `id`: 商品ID
- 请求参数：
  - `stock`: 库存数量，大于0的整数
- 响应示例：
```json
{
    "success": true,
    "message": "库存更新成功",
    "data": null
}
```

## 注意事项

1. 所有接口都需要认证才能访问（除了登录注册接口）
2. 所有接口的响应格式统一为：
```json
{
    "success": true/false,    // 是否成功
    "message": "提示信息",     // 提示信息
    "data": {}               // 返回数据，可能为null
}
```

3. 分页接口的返回数据格式：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "records": [],       // 数据列表
        "total": 100,       // 总记录数
        "size": 10,         // 每页条数
        "current": 1        // 当前页码
    }
}
```

4. 错误响应示例：
```json
{
    "success": false,
    "message": "错误信息",
    "data": null
}
``` 