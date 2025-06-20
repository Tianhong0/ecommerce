# Ecommerce 电商后台管理系统 API 文档

## 系统概述
本文档描述了电商后台管理系统的所有API接口，包括用户管理、商品管理、订单管理、系统监控等模块。

## 接口规范
- 基础URL: `/api/v1`
- 请求方法: RESTful
- 认证方式: JWT Token
- 响应格式: JSON
- 错误处理: 统一错误响应

## 认证与授权
- 所有接口（除登录/注册）需要携带JWT Token
- Token在请求头 `Authorization` 中，格式为 `Bearer {token}`
- 权限控制基于角色和细粒度权限

## 模块列表

### 1. 用户管理 (`/user`)
- 用户注册
- 用户登录
- 用户信息查询与更新
- 用户角色管理

### 2. 商品管理 (`/product`)
- 商品列表查询
- 商品创建与更新
- 商品SKU管理
- 商品分类管理

### 3. 订单管理 (`/order`)
- 订单创建
- 订单查询
- 订单状态管理
- 订单退款处理

### 4. 权限管理 (`/permission`)
- 角色定义
- 权限分配
- 权限检查

### 5. 系统监控 (`/system`)
- 系统资源监控
- 性能指标
- 日志查询

### 6. 文件上传 (`/file`)
- 单文件上传
- 多文件上传
- 文件预签名
- 文件删除

### 7. 订单统计 (`/order/statistics`)
- 销售总览
- 商品销售排行
- 订单状态分布
- 用户消费分析
- 退款统计

## 公共响应结构
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

## 错误码
- 200: 成功
- 400: 参数错误
- 401: 未授权
- 403: 权限不足
- 404: 资源不存在
- 500: 服务器内部错误

## 版本信息
- 当前版本: v1.0.3
- 最后更新: 2023-12-01

## 附加说明
- 所有时间戳使用 ISO 8601 格式
- 金额使用 BigDecimal 精确到分
- 分页参数：`pageNum`（页码）, `pageSize`（每页数量）

## 用户管理

### 创建用户
- 请求路径：`POST /api/users`
- 请求参数：
  ```json
  {
    "username": "string",     // 用户名
    "password": "string",     // 密码
    "nickname": "string",     // 昵称
    "email": "string",        // 邮箱
    "phone": "string"         // 手机号
  }
  ```
- 响应结果：
  ```json
  {
    "success": true,
    "message": "创建用户成功"
  }
  ```

### 更新用户
- 请求路径：`PUT /api/users/{id}`
- 请求参数：
  ```json
  {
    "username": "string",     // 用户名
    "password": "string",     // 密码（可选）
    "nickname": "string",     // 昵称
    "email": "string",        // 邮箱
    "phone": "string"         // 手机号
  }
  ```
- 响应结果：
  ```json
  {
    "success": true,
    "message": "更新用户成功"
  }
  ```

### 删除用户
- 请求路径：`DELETE /api/users/{id}`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "删除用户成功"
  }
  ```

### 获取用户详情
- 请求路径：`GET /api/users/{id}`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取用户成功",
    "data": {
      "id": "long",
      "username": "string",
      "nickname": "string",
      "email": "string",
      "phone": "string",
      "status": "integer",
      "createTime": "datetime",
      "updateTime": "datetime"
    }
  }
  ```

### 获取用户列表
- 请求路径：`GET /api/users`
- 请求参数：
  - pageNum：页码（默认1）
  - pageSize：每页大小（默认10）
  - username：用户名（可选，模糊查询）
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取用户列表成功",
    "data": {
      "records": [
        {
          "id": "long",
          "username": "string",
          "nickname": "string",
          "email": "string",
          "phone": "string",
          "status": "integer",
          "createTime": "datetime",
          "updateTime": "datetime"
        }
      ],
      "total": "long",
      "size": "integer",
      "current": "integer"
    }
  }
  ```

### 更新用户状态
- 请求路径：`PUT /api/users/{id}/status`
- 请求参数：
  - status：状态（0-禁用，1-启用）
- 响应结果：
  ```json
  {
    "success": true,
    "message": "更新用户状态成功"
  }
  ```

### 分配用户角色
- 请求路径：`POST /api/users/{id}/roles`
- 请求参数：
  ```json
  [
    "long"  // 角色ID列表
  ]
  ```
- 响应结果：
  ```json
  {
    "success": true,
    "message": "分配角色成功"
  }
  ```

### 获取用户角色
- 请求路径：`GET /api/users/{id}/roles`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取用户角色成功",
    "data": [
      "long"  // 角色ID列表
    ]
  }
  ```

## 角色管理

### 创建角色
- 请求路径：`POST /api/roles`
- 请求参数：
  ```json
  {
    "name": "string",         // 角色名称
    "code": "string",         // 角色编码
    "description": "string"   // 角色描述
  }
  ```
- 响应结果：
  ```json
  {
    "success": true,
    "message": "创建角色成功"
  }
  ```

### 更新角色
- 请求路径：`PUT /api/roles/{id}`
- 请求参数：
  ```json
  {
    "name": "string",         // 角色名称
    "code": "string",         // 角色编码
    "description": "string"   // 角色描述
  }
  ```
- 响应结果：
  ```json
  {
    "success": true,
    "message": "更新角色成功"
  }
  ```

### 删除角色
- 请求路径：`DELETE /api/roles/{id}`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "删除角色成功"
  }
  ```

### 获取角色详情
- 请求路径：`GET /api/roles/{id}`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取角色成功",
    "data": {
      "id": "long",
      "name": "string",
      "code": "string",
      "description": "string",
      "status": "integer",
      "createTime": "datetime",
      "updateTime": "datetime"
    }
  }
  ```

### 获取角色列表
- 请求路径：`GET /api/roles`
- 请求参数：
  - pageNum：页码（默认1）
  - pageSize：每页大小（默认10）
  - name：角色名称（可选，模糊查询）
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取角色列表成功",
    "data": {
      "records": [
        {
          "id": "long",
          "name": "string",
          "code": "string",
          "description": "string",
          "status": "integer",
          "createTime": "datetime",
          "updateTime": "datetime"
        }
      ],
      "total": "long",
      "size": "integer",
      "current": "integer"
    }
  }
  ```

### 更新角色状态
- 请求路径：`PUT /api/roles/{id}/status`
- 请求参数：
  - status：状态（0-禁用，1-启用）
- 响应结果：
  ```json
  {
    "success": true,
    "message": "更新角色状态成功"
  }
  ```

### 分配角色权限
- 请求路径：`POST /api/roles/{id}/permissions`
- 请求参数：
  ```json
  [
    "long"  // 权限ID列表
  ]
  ```
- 响应结果：
  ```json
  {
    "success": true,
    "message": "分配权限成功"
  }
  ```

### 获取角色权限
- 请求路径：`GET /api/roles/{id}/permissions`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取角色权限成功",
    "data": [
      "long"  // 权限ID列表
    ]
  }
  ```

## 权限管理

### 创建权限
- 请求路径：`POST /api/permissions`
- 请求参数：
  ```json
  {
    "name": "string",         // 权限名称
    "code": "string",         // 权限编码
    "type": "integer",        // 类型（1-菜单，2-按钮）
    "parentId": "long",       // 父级ID
    "path": "string",         // 路径
    "icon": "string",         // 图标
    "sort": "integer"         // 排序
  }
  ```
- 响应结果：
  ```json
  {
    "success": true,
    "message": "创建权限成功"
  }
  ```

### 更新权限
- 请求路径：`PUT /api/permissions/{id}`
- 请求参数：
  ```json
  {
    "name": "string",         // 权限名称
    "code": "string",         // 权限编码
    "type": "integer",        // 类型（1-菜单，2-按钮）
    "parentId": "long",       // 父级ID
    "path": "string",         // 路径
    "icon": "string",         // 图标
    "sort": "integer"         // 排序
  }
  ```
- 响应结果：
  ```json
  {
    "success": true,
    "message": "更新权限成功"
  }
  ```

### 删除权限
- 请求路径：`DELETE /api/permissions/{id}`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "删除权限成功"
  }
  ```

### 获取权限详情
- 请求路径：`GET /api/permissions/{id}`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取权限成功",
    "data": {
      "id": "long",
      "name": "string",
      "code": "string",
      "type": "integer",
      "parentId": "long",
      "path": "string",
      "icon": "string",
      "sort": "integer",
      "status": "integer",
      "createTime": "datetime",
      "updateTime": "datetime"
    }
  }
  ```

### 获取权限列表
- 请求路径：`GET /api/permissions`
- 请求参数：
  - pageNum：页码（默认1）
  - pageSize：每页大小（默认10）
  - name：权限名称（可选，模糊查询）
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取权限列表成功",
    "data": {
      "records": [
        {
          "id": "long",
          "name": "string",
          "code": "string",
          "type": "integer",
          "parentId": "long",
          "path": "string",
          "icon": "string",
          "sort": "integer",
          "status": "integer",
          "createTime": "datetime",
          "updateTime": "datetime"
        }
      ],
      "total": "long",
      "size": "integer",
      "current": "integer"
    }
  }
  ```

### 获取权限树
- 请求路径：`GET /api/permissions/tree`
- 响应结果：
  ```json
  {
    "success": true,
    "message": "获取权限树成功",
    "data": [
      {
        "id": "long",
        "name": "string",
        "code": "string",
        "type": "integer",
        "parentId": "long",
        "path": "string",
        "icon": "string",
        "sort": "integer",
        "status": "integer",
        "createTime": "datetime",
        "updateTime": "datetime",
        "children": []
      }
    ]
  }
  ```

### 更新权限状态
- 请求路径：`PUT /api/permissions/{id}/status`
- 请求参数：
  - status：状态（0-禁用，1-启用）
- 响应结果：
  ```json
  {
    "success": true,
    "message": "更新权限状态成功"
  }
  