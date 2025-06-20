# 商品分类 API 文档

## 1. 获取分类列表
- **接口地址**: `/category/list`
- **请求方法**: GET
- **请求参数**:
  - `pageNum` (可选, 默认1): 页码
  - `pageSize` (可选, 默认10): 每页数量
  - `name` (可选): 分类名称模糊查询
- **响应示例**:
```json
{
  "records": [
    {
      "id": 1,
      "name": "电子数码",
      "parentId": 0,
      "level": 1,
      "status": 1,
      "sort": 1,
      "createTime": "2023-12-01T10:00:00"
    }
  ],
  "total": 10,
  "size": 10,
  "current": 1
}
```

## 2. 创建分类
- **接口地址**: `/category/create`
- **请求方法**: POST
- **请求参数**:
```json
{
  "name": "手机",
  "parentId": 1,
  "level": 2,
  "status": 1,
  "sort": 1
}
```
- **响应**: 创建成功的分类对象

## 3. 更新分类
- **接口地址**: `/category/update`
- **请求方法**: PUT
- **请求参数**:
```json
{
  "id": 2,
  "name": "智能手机",
  "status": 1,
  "sort": 2
}
```
- **响应**: 更新后的分类对象

## 4. 删除分类
- **接口地址**: `/category/delete/{id}`
- **请求方法**: DELETE
- **响应**: 成功返回 true，失败返回 false

## 5. 获取分类详情
- **接口地址**: `/category/{id}`
- **请求方法**: GET
- **响应示例**:
```json
{
  "id": 1,
  "name": "电子数码",
  "parentId": 0,
  "level": 1,
  "status": 1,
  "sort": 1,
  "createTime": "2023-12-01T10:00:00",
  "updateTime": "2023-12-01T10:00:00"
}
```

## 状态码
- `status`: 0-禁用，1-启用

## 错误处理
- 400: 参数错误
- 404: 资源不存在
- 500: 服务器内部错误 