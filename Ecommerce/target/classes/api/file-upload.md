# 文件上传模块API文档

## 上传文件
- 请求路径：`POST /upload`
- 请求权限：`file:upload`
- 请求方式：`multipart/form-data`
- 请求参数：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| file | File | 是 | 要上传的文件，支持jpg、png、mp4格式 |

- 响应结果：
```json
{
    "success": true,
    "message": "上传成功",
    "data": "https://tianhong-lingxi.oss-cn-beijing.aliyuncs.com/xxx.jpg"
}
```

### 错误响应
1. 文件为空
```json
{
    "success": false,
    "message": "文件不能为空"
}
```

2. 文件格式不支持
```json
{
    "success": false,
    "message": "文件格式不支持，仅支持mp4、jpg、png格式"
}
```

3. 文件上传失败
```json
{
    "success": false,
    "message": "文件上传失败：[具体错误信息]"
}
```

### 说明
1. 文件限制：
   - 支持的文件格式：jpg、png、mp4
   - 单个文件大小限制：10MB
   - 请求总大小限制：10MB

2. 返回数据说明：
   - success: 是否成功
   - message: 提示信息
   - data: 上传成功后的文件访问URL

3. 注意事项：
   - 文件参数名必须为 "file"
   - 请求必须使用 multipart/form-data 格式
   - 上传的文件会被重命名为UUID格式
   - 文件会被上传到阿里云OSS存储
   - 返回的URL可以直接访问上传的文件

4. 调用示例：
```javascript
// 使用 FormData
const formData = new FormData();
formData.append('file', 文件对象);

// 发送请求
fetch('/upload', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => {
  if (data.success) {
    console.log('文件URL：', data.data);
  }
});
```

5. 安全说明：
   - 接口需要认证才能访问
   - 文件上传前会进行格式验证
   - 文件名会被重命名，避免文件名冲突
   - 建议在上传前在前端进行文件类型和大小的预检查 