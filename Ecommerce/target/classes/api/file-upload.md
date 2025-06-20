# 文件上传 API 文档

## 1. 单文件上传
- **接口地址**: `/file/upload`
- **请求方法**: POST
- **请求参数**:
  - `file`: 文件对象（multipart/form-data）
  - `type` (可选): 文件类型（product, user, order）
- **响应示例**:
```json
{
  "fileName": "image_20231201_123456.jpg",
  "fileUrl": "https://oss.example.com/uploads/image_20231201_123456.jpg",
  "fileSize": 1024,
  "fileType": "image/jpeg"
}
```

## 2. 多文件上传
- **接口地址**: `/file/upload/multiple`
- **请求方法**: POST
- **请求参数**:
  - `files`: 文件对象数组（multipart/form-data）
  - `type` (可选): 文件类型（product, user, order）
- **响应示例**:
```json
{
  "files": [
    {
      "fileName": "image1_20231201_123456.jpg",
      "fileUrl": "https://oss.example.com/uploads/image1_20231201_123456.jpg",
      "fileSize": 1024,
      "fileType": "image/jpeg"
    },
    {
      "fileName": "image2_20231201_123457.jpg",
      "fileUrl": "https://oss.example.com/uploads/image2_20231201_123457.jpg",
      "fileSize": 2048,
      "fileType": "image/jpeg"
    }
  ]
}
```

## 3. 文件预签名上传
- **接口地址**: `/file/upload/presigned`
- **请求方法**: GET
- **请求参数**:
  - `fileName`: 文件名
  - `fileType`: 文件类型
- **响应示例**:
```json
{
  "uploadUrl": "https://oss.example.com/presigned-upload",
  "fileKey": "uploads/image_20231201_123456.jpg",
  "expireTime": "2023-12-01T13:00:00"
}
```

## 4. 文件删除
- **接口地址**: `/file/delete`
- **请求方法**: DELETE
- **请求参数**:
```json
{
  "fileUrl": "https://oss.example.com/uploads/image_20231201_123456.jpg"
}
```
- **响应**: 成功返回 true，失败返回 false

## 文件类型限制
- 支持的文件类型：
  - 图片：jpg, jpeg, png, gif, webp
  - 文档：pdf, doc, docx, xls, xlsx
  - 最大文件大小：10MB

## 错误处理
- 400: 参数错误（文件类型、大小不符）
- 403: 权限不足
- 500: 服务器内部错误

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