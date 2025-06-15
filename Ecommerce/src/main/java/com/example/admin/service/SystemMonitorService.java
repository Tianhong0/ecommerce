package com.example.admin.service;

import com.example.admin.dto.SystemInfoDTO;
import com.example.admin.dto.SystemResourceDTO;

public interface SystemMonitorService {
    /**
     * 获取系统信息
     */
    SystemInfoDTO getSystemInfo();

    /**
     * 获取CPU使用率
     */
    SystemResourceDTO getCpuUsage();

    /**
     * 获取内存使用情况
     */
    SystemResourceDTO getMemoryUsage();
} 