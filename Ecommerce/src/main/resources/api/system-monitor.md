# 系统监控模块API文档

## 获取系统信息
- 请求路径：`GET /system/info`
- 请求权限：`system:monitor`
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "osName": "操作系统名称",
        "osVersion": "操作系统版本",
        "osArch": "操作系统架构",
        "javaVersion": "Java版本",
        "javaVendor": "Java供应商",
        "javaHome": "Java安装路径",
        "userDir": "用户目录",
        "userHome": "用户主目录",
        "userName": "用户名"
    }
}
```

## 获取CPU使用率
- 请求路径：`GET /system/cpu`
- 请求权限：`system:monitor`
- 响应结果：
```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "cpuModel": "CPU型号",
        "cpuCores": "CPU核心数",
        "cpuThreads": "CPU线程数",
        "cpuTemperature": "CPU温度（摄氏度）",
        "cpuUsage": "CPU使用率（百分比）",
        "memoryTotal": "内存总量（MB）",
        "memoryUsed": "已用内存（MB）",
        "memoryFree": "空闲内存（MB）",
        "memoryUsage": "内存使用率（百分比）",
        "diskTotal": "磁盘总量（GB）",
        "diskUsed": "已用磁盘（GB）",
        "diskFree": "空闲磁盘（GB）",
        "diskUsage": "磁盘使用率（百分比）"
    }
}
```

### 说明
1. 系统信息接口返回基本的系统环境信息，包括操作系统和Java运行环境的相关信息。

2. CPU使用率接口返回：
   - CPU基本信息：型号、核心数、线程数
   - CPU使用情况：使用率、温度
   - 内存使用情况：总量、已用、空闲、使用率
   - 磁盘使用情况：总量、已用、空闲、使用率

3. 内存使用情况接口返回与CPU使用率接口相同的信息。

4. 所有数值说明：
   - 所有数值保留2位小数
   - 内存单位：MB
   - 磁盘单位：GB
   - 使用率单位：百分比
   - CPU温度单位：摄氏度

5. 注意事项：
   - CPU温度获取可能因操作系统和硬件支持情况而不同
   - 某些系统可能无法获取CPU温度，此时返回null
   - CPU线程数基于核心数估算，可能与实际值有差异 