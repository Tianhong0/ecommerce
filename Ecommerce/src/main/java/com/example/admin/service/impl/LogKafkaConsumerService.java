package com.example.admin.service.impl;

import com.example.admin.constant.KafkaConstant;
import com.example.admin.dto.message.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 日志Kafka消费者服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogKafkaConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String LOG_DIR = "logs";
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * 处理用户行为日志消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_LOG_USER_ACTION, groupId = KafkaConstant.GROUP_LOG)
    public void handleUserActionLogMessage(String message) {
        try {
            log.debug("接收到用户行为日志消息: {}", message);
            LogMessage logMessage = objectMapper.readValue(message, LogMessage.class);
            
            // 将日志写入文件
            writeLogToFile("user_action", logMessage);
            
        } catch (JsonProcessingException e) {
            log.error("处理用户行为日志消息失败", e);
        }
    }
    
    /**
     * 处理系统日志消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_LOG_SYSTEM, groupId = KafkaConstant.GROUP_LOG)
    public void handleSystemLogMessage(String message) {
        try {
            log.debug("接收到系统日志消息: {}", message);
            LogMessage logMessage = objectMapper.readValue(message, LogMessage.class);
            
            // 将日志写入文件
            writeLogToFile("system", logMessage);
            
        } catch (JsonProcessingException e) {
            log.error("处理系统日志消息失败", e);
        }
    }
    
    /**
     * 处理错误日志消息
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_LOG_ERROR, groupId = KafkaConstant.GROUP_LOG)
    public void handleErrorLogMessage(String message) {
        try {
            log.debug("接收到错误日志消息: {}", message);
            LogMessage logMessage = objectMapper.readValue(message, LogMessage.class);
            
            // 将日志写入文件
            writeLogToFile("error", logMessage);
            
        } catch (JsonProcessingException e) {
            log.error("处理错误日志消息失败", e);
        }
    }
    
    /**
     * 将日志写入文件
     */
    private void writeLogToFile(String logType, LogMessage logMessage) {
        try {
            // 确保日志目录存在
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            // 按日期和日志类型生成日志文件名
            String today = LocalDate.now().format(DATE_FORMATTER);
            File logFile = new File(logDir, logType + "_" + today + ".log");
            
            // 将日志写入文件
            try (FileWriter fileWriter = new FileWriter(logFile, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {
                
                // 格式化日志内容
                String logContent = objectMapper.writeValueAsString(logMessage);
                printWriter.println(logContent);
            }
        } catch (IOException e) {
            log.error("写入日志文件失败", e);
        }
    }
} 