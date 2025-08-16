package com.icbc.audit.input.controller;

/**
 * 输入处理控制器 - 负责接收HTTP请求并分发处理
 * 核心功能：
 * 1. 提供RESTful接口处理三种输入方式（手动/文件/数据库）
 * 2. 验证输入参数合法性
 * 3. 调用输入处理服务并返回标准化响应
 * 4. 处理跨域请求
 * 5. 统一异常处理和错误响应封装
 */


import com.icbc.audit.input.DTO.DbConnectionDTO;
import com.icbc.audit.input.DTO.FileProcessResult;
import com.icbc.audit.input.DTO.SchemaDTO;
import com.icbc.audit.input.entity.SqlInputEntity;
import com.icbc.audit.input.exception.BusinessException;
import com.icbc.audit.input.service.InputProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/input")
public class InputController {

    @Autowired
    private InputProcessingService inputProcessingService;

    /**
     * 处理手动输入SQL
     */
    @PostMapping("/manual")
    public ResponseEntity<?> handleManualInput(@RequestBody Map<String, String> request) {
        try {
            String sql = request.get("sql");
            if (sql == null || sql.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("SQL内容不能为空");
            }

            SqlInputEntity result = inputProcessingService.processManualInput(sql);
            return ResponseEntity.ok(result);
        } catch (BusinessException e) {
            return ResponseEntity
                    .badRequest()
                    .body(createErrorResponse(e.getErrorCode().getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("SYSTEM_ERROR", "系统处理失败: " + e.getMessage()));
        }
    }

    /**
     * 处理文件上传
     */
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("请选择要上传的文件");
            }

            FileProcessResult result = inputProcessingService.processFileUpload(file);
            return ResponseEntity.ok(result);
        } catch (BusinessException e) {
            return ResponseEntity
                    .badRequest()
                    .body(createErrorResponse(e.getErrorCode().getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("SYSTEM_ERROR", "文件处理失败: " + e.getMessage()));
        }
    }

    /**
     * 处理数据库直连
     */
    @PostMapping("/db-connect")
    public ResponseEntity<?> handleDbConnection(@RequestBody DbConnectionDTO connectionDTO) {
        try {
            if (!connectionDTO.validate()) {
                return ResponseEntity.badRequest().body("数据库连接参数不完整");
            }

            SchemaDTO schema = inputProcessingService.processDbConnection(connectionDTO);
            return ResponseEntity.ok(schema);
        } catch (BusinessException e) {
            return ResponseEntity
                    .badRequest()
                    .body(createErrorResponse(e.getErrorCode().getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("SYSTEM_ERROR", "数据库连接失败: " + e.getMessage()));
        }
    }

    /**
     * 创建统一的错误响应格式
     */
    private Map<String, String> createErrorResponse(String code, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        return error;
    }
}
