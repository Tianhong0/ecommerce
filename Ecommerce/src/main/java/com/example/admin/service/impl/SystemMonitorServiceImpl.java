package com.example.admin.service.impl;

import com.example.admin.dto.SystemInfoDTO;
import com.example.admin.dto.SystemResourceDTO;
import com.example.admin.service.SystemMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SystemMonitorServiceImpl implements SystemMonitorService {

    @Override
    public SystemInfoDTO getSystemInfo() {
        SystemInfoDTO info = new SystemInfoDTO();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        
        info.setOsName(osBean.getName());
        info.setOsVersion(osBean.getVersion());
        info.setOsArch(osBean.getArch());
        info.setJavaVersion(System.getProperty("java.version"));
        info.setJavaVendor(System.getProperty("java.vendor"));
        info.setJavaHome(System.getProperty("java.home"));
        info.setUserDir(System.getProperty("user.dir"));
        info.setUserHome(System.getProperty("user.home"));
        info.setUserName(System.getProperty("user.name"));
        
        return info;
    }

    @Override
    public SystemResourceDTO getCpuUsage() {
        SystemResourceDTO resource = new SystemResourceDTO();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            
            // 获取CPU使用率
            double cpuLoad = sunOsBean.getSystemCpuLoad();
            if (cpuLoad >= 0) {
                resource.setCpuUsage(BigDecimal.valueOf(cpuLoad * 100)
                        .setScale(2, RoundingMode.HALF_UP));
            }
            
            // 获取CPU核心数和线程数
            resource.setCpuCores(sunOsBean.getAvailableProcessors());
            resource.setCpuThreads(sunOsBean.getAvailableProcessors() * 2); // 假设是超线程CPU
            
            // 获取CPU型号
            resource.setCpuModel(getCpuModel());
            
            // 获取CPU温度
            resource.setCpuTemperature(getCpuTemperature());
            
            // 获取系统内存信息
            long totalPhysicalMemory = sunOsBean.getTotalPhysicalMemorySize();
            long freePhysicalMemory = sunOsBean.getFreePhysicalMemorySize();
            long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
            
            // 转换为MB
            resource.setMemoryTotal(BigDecimal.valueOf(totalPhysicalMemory / (1024.0 * 1024.0))
                    .setScale(2, RoundingMode.HALF_UP));
            resource.setMemoryUsed(BigDecimal.valueOf(usedPhysicalMemory / (1024.0 * 1024.0))
                    .setScale(2, RoundingMode.HALF_UP));
            resource.setMemoryFree(BigDecimal.valueOf(freePhysicalMemory / (1024.0 * 1024.0))
                    .setScale(2, RoundingMode.HALF_UP));
            
            // 计算内存使用率
            double memoryUsage = (double) usedPhysicalMemory / totalPhysicalMemory * 100;
            resource.setMemoryUsage(BigDecimal.valueOf(memoryUsage)
                    .setScale(2, RoundingMode.HALF_UP));
            
            // 获取磁盘信息
            File[] roots = File.listRoots();
            if (roots != null && roots.length > 0) {
                File root = roots[0]; // 获取根目录
                long totalSpace = root.getTotalSpace();
                long freeSpace = root.getFreeSpace();
                long usedSpace = totalSpace - freeSpace;
                
                // 转换为GB
                resource.setDiskTotal(BigDecimal.valueOf(totalSpace / (1024.0 * 1024.0 * 1024.0))
                        .setScale(2, RoundingMode.HALF_UP));
                resource.setDiskUsed(BigDecimal.valueOf(usedSpace / (1024.0 * 1024.0 * 1024.0))
                        .setScale(2, RoundingMode.HALF_UP));
                resource.setDiskFree(BigDecimal.valueOf(freeSpace / (1024.0 * 1024.0 * 1024.0))
                        .setScale(2, RoundingMode.HALF_UP));
                
                // 计算磁盘使用率
                double diskUsage = (double) usedSpace / totalSpace * 100;
                resource.setDiskUsage(BigDecimal.valueOf(diskUsage)
                        .setScale(2, RoundingMode.HALF_UP));
            }
        }
        
        return resource;
    }

    @Override
    public SystemResourceDTO getMemoryUsage() {
        return getCpuUsage(); // 复用getCpuUsage方法的实现
    }

    /**
     * 获取CPU型号
     */
    private String getCpuModel() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command = "";
            
            if (os.contains("win")) {
                // 修改Windows下的命令，使用更可靠的方式获取CPU型号
                command = "wmic cpu get name /value";
            } else if (os.contains("linux")) {
                command = "cat /proc/cpuinfo | grep 'model name' | uniq";
            } else if (os.contains("mac")) {
                command = "sysctl -n machdep.cpu.brand_string";
            }
            
            if (!command.isEmpty()) {
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (os.contains("win")) {
                        // Windows下处理Name=开头的行
                        if (line.startsWith("Name=")) {
                            return line.substring(5).trim();
                        }
                    } else {
                        // 其他系统直接返回第一行非空结果
                        if (!line.trim().isEmpty()) {
                            return line.replaceAll("(model name|Name|:)\\s*", "").trim();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取CPU型号失败", e);
        }
        return "Unknown";
    }

    /**
     * 获取CPU温度
     */
    private BigDecimal getCpuTemperature() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command = "";
            
            if (os.contains("win")) {
                command = "wmic /namespace:\\\\root\\wmi PATH MSAcpi_ThermalZoneTemperature get CurrentTemperature";
            } else if (os.contains("linux")) {
                command = "cat /sys/class/thermal/thermal_zone0/temp";
            } else if (os.contains("mac")) {
                command = "osascript -e 'tell application \"System Events\" to get value of attribute \"temperature\" of thermal zone 0'";
            }
            
            if (!command.isEmpty()) {
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();
                if (line != null) {
                    // 清理输出结果并转换为摄氏度
                    String temp = line.replaceAll("[^0-9.]", "").trim();
                    if (!temp.isEmpty()) {
                        double temperature;
                        if (os.contains("win")) {
                            // Windows返回的是开尔文温度，需要转换为摄氏度
                            temperature = (Double.parseDouble(temp) / 10.0) - 273.15;
                        } else {
                            // Linux返回的是毫摄氏度，需要转换为摄氏度
                            temperature = Double.parseDouble(temp) / 1000.0;
                        }
                        return BigDecimal.valueOf(temperature).setScale(2, RoundingMode.HALF_UP);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取CPU温度失败", e);
        }
        return null;
    }
} 