package com.icbc.audit.ogdl.controller;


import com.icbc.audit.ogdl.model.dto.ConnectionTestRequestDTO;
import com.icbc.audit.ogdl.model.dto.ConnectionTestResultDTO;
import com.icbc.audit.ogdl.model.dto.TableMetadataDTO;
import com.icbc.audit.ogdl.service.ConnectionService;
import com.icbc.audit.web.ApiResponse;
import com.icbc.audit.web.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "OpenGauss直连数据库管理")
@Slf4j
@RestController
@RequestMapping("/api/connect/")
@RequiredArgsConstructor
public class ConnectionController {

    @Autowired
    private final ConnectionService connectService;

    @Operation(summary = "测试数据库连接")
    @PostMapping("/test")
    public ApiResponse<ConnectionTestResultDTO> testConnection(@RequestBody @Valid ConnectionTestRequestDTO request) {
        try {
            ConnectionTestResultDTO result = connectService.testConnection(request);
            return ApiResponse.ok(result);
        } catch (Exception e) {
            log.error("数据库连接测试失败", e);
            return ApiResponse.fail(ErrorCode.INVALID_PARAM, "连接测试失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取表元数据")
    @GetMapping("/metadata/{datasourceId}/{tableName}")
    public ApiResponse<List<TableMetadataDTO>> getTableMetadata(
            @PathVariable String datasourceId,
            @PathVariable String tableName) {
        try {
            // 在实际应用中，datasourceId 可用于从配置中心或数据库获取连接信息
            // 这里简化处理，假设datasourceId包含连接信息或指向一个预定义的连接
            List<TableMetadataDTO> metadata = connectService.getTableMetadata(datasourceId, tableName);
            return ApiResponse.ok(metadata);
        } catch (Exception e) {
            log.error("获取表元数据失败", e);
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "获取表元数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据元数据生成SQL")
    @GetMapping("/sql/generate/{datasourceId}/{tableName}")
    public ApiResponse<String> generateSql(
            @PathVariable String datasourceId,
            @PathVariable String tableName) {
        try {
            String sql = connectService.generateSql(datasourceId, tableName);
            return ApiResponse.ok(sql);
        } catch (Exception e) {
            log.error("生成SQL失败", e);
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "生成SQL失败: " + e.getMessage());
        }
    }
}

