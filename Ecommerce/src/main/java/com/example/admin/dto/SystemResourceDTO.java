package com.example.admin.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SystemResourceDTO {
    // CPU信息
    private String cpuModel; // CPU型号
    private Integer cpuCores; // CPU核心数
    private Integer cpuThreads; // CPU线程数
    private BigDecimal cpuTemperature; // CPU温度
    private BigDecimal cpuUsage; // CPU使用率
    
    // 内存信息
    private BigDecimal memoryTotal; // 内存总量（MB）
    private BigDecimal memoryUsed; // 已用内存（MB）
    private BigDecimal memoryFree; // 空闲内存（MB）
    private BigDecimal memoryUsage; // 内存使用率
    
    // 磁盘信息
    private BigDecimal diskTotal; // 磁盘总量（GB）
    private BigDecimal diskUsed; // 已用磁盘（GB）
    private BigDecimal diskFree; // 空闲磁盘（GB）
    private BigDecimal diskUsage; // 磁盘使用率
} 