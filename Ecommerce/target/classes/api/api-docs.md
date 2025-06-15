# 用户权限管理API文档

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
  