package com.example.admin.controller;

import com.example.admin.dto.SystemInfoDTO;
import com.example.admin.dto.SystemResourceDTO;
import com.example.admin.exception.GlobalExceptionHandler.ApiResponse;
import com.example.admin.service.SystemMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemMonitorController {

    private final SystemMonitorService systemMonitorService;

    @GetMapping("/info")
    public ResponseEntity<ApiResponse> getSystemInfo() {
        SystemInfoDTO result = systemMonitorService.getSystemInfo();
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }

    @GetMapping("/cpu")
    public ResponseEntity<ApiResponse> getCpuUsage() {
        SystemResourceDTO result = systemMonitorService.getCpuUsage();
        return ResponseEntity.ok(new ApiResponse(true, "查询成功", result));
    }


}
