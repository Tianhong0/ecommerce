package com.example.admin.controller;


import com.example.admin.exception.GlobalExceptionHandler;
import com.example.admin.util.AliOssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")

    public ResponseEntity<GlobalExceptionHandler.ApiResponse> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 判断文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new GlobalExceptionHandler.ApiResponse(false, "文件不能为空"));
            }

            // 判断文件的类型是否为支持的
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null ||
                (!originalFilename.endsWith(".mp4") &&
                 !originalFilename.endsWith(".jpg") &&
                 !originalFilename.endsWith(".png"))) {
                return ResponseEntity.badRequest()
                    .body(new GlobalExceptionHandler.ApiResponse(false, "文件格式不支持，仅支持mp4、jpg、png格式"));
            }

            String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String url = AliOssUtils.uploadFile(filename, file.getInputStream());

            return ResponseEntity.ok(new GlobalExceptionHandler.ApiResponse(true, "上传成功", url));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new GlobalExceptionHandler.ApiResponse(false, "文件上传失败：" + e.getMessage()));
        }
    }
}
