# 商品管理API文档

## 创建商品
- 请求路径：`POST /product`
- 请求权限：需要认证
- 请求参数：
```json
{
    "categoryId": "分类ID",
    "name": "商品名称",
    "description": "商品描述",
    "mainImage": "商品主图",
    "price": "商品价格",
    "stock": "商品库存",
    "status": "商品状态：0-下架 1-上架",
    "skuList": [
        {
            "skuCode": "SKU编码",
            "name": "SKU名称",
            "attributes": "规格属性（JSON格式）",
            "price": "SKU价格",
            "stock": "SKU库存",
            "status": "SKU状态：0-禁用 1-启用"
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
        "id": "商品ID",
        "categoryId": "分类ID",
        "name": "商品名称",
        "description": "商品描述",
        "mainImage": "商品主图",
        "price": "商品价格",
        "stock": "商品库存",
        "status": "商品状态",
        "createTime": "创建时间",
        "updateTime": "更新时间",
        "skuList": [
            {
                "id": "SKU ID",
                "productId": "商品ID",
                "skuCode": "SKU编码",
                "name": "SKU名称",
                "attributes": "规格属性",
                "price": "SKU价格",
                "stock": "SKU库存",
                "status": "SKU状态",
                "createTime": "创建时间",
                "updateTime": "更新时间"
            }
        ]
    }
}
```

## 更新商品
- 请求路径：`PUT /product/{id}`
- 请求权限：需要认证
- 请求参数：
```json
{
    "categoryId": "分类ID",
    "name": "商品名称",
    "description": "商品描述",
    "mainImage": "商品主图",
    "price": "商品价格",
    "stock": "商品库存",
    "status": "商品状态：0-下架 1-上架",
    "skuList": [
        {
            "skuCode": "SKU编码",
            "name": "SKU名称",
            "attributes": "规格属性（JSON格式）",
            "price": "SKU价格",
            "stock": "SKU库存",
            "status": "SKU状态：0-禁用 1-启用"
        }
    ]
}
```
- 响应结果：
```json
{
    "success": true,
    "message": "更新成功",
    "data": {
        "id": "商品ID",
        // ... 同创建商品的返回数据结构
    }
}
```

## 删除商品
- 请求路径：`DELETE /product/{id}`
- 请求权限：需要认证
- 响应结果：
```json
{
    "success": true,
    "message": "删除成功"
}
```

## 获取商品详情
- 请求路径：`GET /product/{id}`
- 请求权限：需要认证
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "id": "商品ID",
        // ... 同创建商品的返回数据结构
    }
}
```

## 分页查询商品列表
- 请求路径：`GET /product`
- 请求权限：需要认证
- 请求参数：
  - page：页码，默认1
  - size：每页数量，默认10
  - name：商品名称（可选）
  - categoryId：分类ID（可选）
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "records": [
            {
                "id": "商品ID",
                // ... 商品详细信息
            }
        ],
        "total": "总记录数",
        "size": "每页数量",
        "current": "当前页码"
    }
}
```

## 更新商品状态
- 请求路径：`PUT /product/{id}/status`
- 请求权限：需要认证
- 请求参数：
  - status：商品状态（0-下架 1-上架）
- 响应结果：
```json
{
    "success": true,
    "message": "状态更新成功"
}
```

## 更新商品库存
- 请求路径：`PUT /product/{id}/stock`
- 请求权限：需要认证
- 请求参数：
  - stock：商品库存数量
- 响应结果：
```json
{
    "success": true,
    "message": "库存更新成功"
}
``` 