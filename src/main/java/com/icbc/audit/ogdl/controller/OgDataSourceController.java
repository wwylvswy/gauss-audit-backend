package com.icbc.audit.ogdl.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icbc.audit.ogdl.model.dto.ConnectionTestRequestDTO;
import com.icbc.audit.ogdl.model.dto.ConnectionTestResultDTO;
import com.icbc.audit.ogdl.model.dto.DataSourceDTO;
import com.icbc.audit.ogdl.model.entity.OgDataSourceEntity;
import com.icbc.audit.ogdl.service.OgDataSourceService;
import com.icbc.audit.ogdl.util.OpenGaussUtil;
import com.icbc.audit.web.ApiResponse;
import com.icbc.audit.web.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Tag(name = "OpenGauss数据源管理和直连数据源")
@Slf4j
@RestController
@RequestMapping("/api/db/")
@RequiredArgsConstructor
public class OgDataSourceController {
    @Autowired
    private OgDataSourceService datasourceService;

    @Operation(
            summary = "分页查询数据源列表",
            description = "分页查询数据源列表，返回分页结果"
    )
    @GetMapping("/page")
    public ApiResponse<IPage<OgDataSourceEntity>> getDatasourcePage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<OgDataSourceEntity> datasources = datasourceService.pageDatasources(page, pageSize);
        return ApiResponse.ok(datasources);
    }

    @Operation(
            summary = "根据ID获取数据源信息",
            description = "根据数据源ID获取详细信息"
    )
    @GetMapping("/getDatasourceInfo/{id}")
    public ApiResponse<OgDataSourceEntity> getDatasourceInfo(@PathVariable Long id) {
        OgDataSourceEntity datasource = datasourceService.getDatasourceById(id);
        if (datasource == null) {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "数据源不存在");
        }
        return ApiResponse.ok(datasource);
    }

    @Operation(
            summary = "新增数据源",
            description = "新增一个数据源"
    )
    @PostMapping("/addDatasource")
    public ApiResponse<Void> addDatasource(@Validated @RequestBody DataSourceDTO dataSourceDTO) {
        boolean saved = datasourceService.addDatasource(dataSourceDTO);
        if (saved) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "新增数据源失败");
        }
    }

    @Operation(
            summary = "删除数据源",
            description = "根据数据源ID删除数据源"
    )
    @DeleteMapping("/deleteDatasource/{id}")
    public ApiResponse<Void> deleteDatasource(@PathVariable Long id) {
        boolean deleted = datasourceService.deleteDatasource(id);
        if (deleted) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "删除数据源失败");
        }
    }

    @Operation(
            summary = "批量删除数据源",
            description = "根据数据源ID列表批量删除数据源"
    )
    @DeleteMapping("/batchDeleteDatasource")
    public ApiResponse<Void> batchDeleteDatasource(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "请选择要删除的数据项");
        }
        boolean deleted = datasourceService.batchDeleteDatasources(ids);
        if (deleted) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "批量删除数据源失败");
        }
    }

    @Operation(
            summary = "更新数据源",
            description = "根据数据源ID更新数据源信息"
    )
    @PutMapping("/updateDatasource/{id}")
    public ApiResponse<Void> updateDatasource(@PathVariable Long id, @Validated @RequestBody DataSourceDTO dataSourceDTO) {
        // 确保 ID 一致
        dataSourceDTO.setId(id);
        boolean updated = datasourceService.updateDatasource(dataSourceDTO);
        if (updated) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "更新数据源失败");
        }
    }

    @Operation(summary = "测试数据库连接")
    @PostMapping("/test")
    public ApiResponse<ConnectionTestResultDTO> testConnection(@RequestBody @Valid ConnectionTestRequestDTO request) {
        try {
            ConnectionTestResultDTO result = datasourceService.testConnection(request);
            return ApiResponse.ok(result);
        } catch (Exception e) {
            log.error("数据库连接测试失败", e);
            return ApiResponse.fail(ErrorCode.INVALID_PARAM, "连接测试失败: " + e.getMessage());
        }
    }
    
    @PatchMapping("/enable/{id}")
    @Operation(
            summary = "启用/禁用数据源",
            description = "根据数据源ID启用或禁用数据源"
    )
    public ApiResponse<Void> enableDatasource(@PathVariable Long id, @RequestParam Boolean status) {
        DataSourceDTO  dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(id);
        dataSourceDTO.setStatus(status);
        Boolean res = datasourceService.enableDatasource(dataSourceDTO);
        if (res) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "操作失败");
        }
    }
    
    @Operation(
            summary = "获取所有可用的数据源",
            description = "获取所有状态为启用且未删除的数据源列表"
    )
    @GetMapping("/getAllDatasource")
    public ApiResponse<List<OgDataSourceEntity>> getAllDatasource() {
        QueryWrapper<OgDataSourceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", true).eq("is_delete", 0);
        List<OgDataSourceEntity> dataSources = datasourceService.list(queryWrapper);
        return ApiResponse.ok(dataSources);
    }


    /**
     * 根据数据源ID获取其下的数据库列表
     * @param id 数据源ID
     * @return 数据库名称列表
     */
    @GetMapping("/datasource/{id}/databases")
    public ApiResponse<List<String>> getDatabases(@PathVariable Long id) {
        OgDataSourceEntity dataSource = datasourceService.getById(id);
        if (dataSource == null) {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "数据源不存在");
        }

        try (Connection conn = OpenGaussUtil.getConnection(dataSource)) {
            List<String> databases = OpenGaussUtil.getDatabases(conn);
            return ApiResponse.ok(databases);
        } catch (SQLException e) {
            // 记录日志
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "获取数据库列表失败" + e.getMessage());
        }
    }

    /**
     * 根据数据源ID和数据库名获取其下的表列表
     * @param id 数据源ID
     * @param databaseName 数据库名
     * @return 表名称列表
     */
    @GetMapping("/datasources/{id}/databases/{databaseName}/tables")
    public ApiResponse<List<String>> getTables(@PathVariable Long id, @PathVariable String databaseName) {
        OgDataSourceEntity dataSource = datasourceService.getById(id);
        if (dataSource == null) {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "数据源不存在");
        }
        // 这里可以验证databaseName是否属于该数据源，为简化省略
        try (Connection conn = OpenGaussUtil.getConnection(dataSource, databaseName)) {
            List<String> tables = OpenGaussUtil.getTables(conn, databaseName);
            return ApiResponse.ok(tables);
        } catch (SQLException e) {
            // 记录日志
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "获取表列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据数据源ID、数据库名和表名获取建表语句(DDL)
     * @param id 数据源ID
     * @param databaseName 数据库名
     * @param tableName 表名
     * @return 包含DDL的Map
     */
    @GetMapping("/datasources/{id}/databases/{databaseName}/tables/{tableName}/ddl")
    public ApiResponse<Map<String, String>> getTableDDL(@PathVariable Long id, @PathVariable String databaseName, @PathVariable String tableName) {
        OgDataSourceEntity dataSource = datasourceService.getById(id);
        if (dataSource == null) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "数据源不存在");
        }
        // 验证databaseName和tableName有效性可在此添加
        if (databaseName == null || databaseName.isEmpty() || tableName == null || tableName.isEmpty()) {
            return ApiResponse.fail(ErrorCode.INVALID_PARAM, "数据库名或表名不能为空");
        }

        try (Connection conn = OpenGaussUtil.getConnection(dataSource, databaseName)) {
            String ddl = OpenGaussUtil.getTableDDL(conn, databaseName, tableName);
            return ApiResponse.ok(Map.of("ddl", ddl));
        } catch (SQLException e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "获取建表语句失败: " + e.getMessage());
        }
    }
}
