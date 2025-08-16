package com.icbc.audit.input.service.impl;

/**
 * 输入处理服务实现 - 具体执行业务逻辑处理
 * 核心功能：
 * 1. 实现三种输入方式的处理流程
 * 2. 协调工具类（文件处理器/SQL解析器/数据库连接器）
 * 3. 封装输入实体和解析结果
 * 4. 处理输入异常并转换为业务异常
 * 5. 记录处理日志
 */

import com.icbc.audit.input.DTO.DbConnectionDTO;
import com.icbc.audit.input.DTO.FileProcessResult;
import com.icbc.audit.input.DTO.SchemaDTO;
import com.icbc.audit.input.entity.SqlInputEntity;
import com.icbc.audit.input.exception.ErrorCode;
import com.icbc.audit.input.exception.SqlParseException;
import com.icbc.audit.input.service.InputProcessingService;
import com.icbc.audit.input.util.FileProcessor;
import com.icbc.audit.input.util.GaussDbConnector;
import com.icbc.audit.input.util.GaussSqlParser;
import com.icbc.audit.input.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InputProcessingServiceImpl implements InputProcessingService {

    @Autowired
    private GaussSqlParser sqlParser;

    @Autowired
    private FileProcessor fileProcessor;

    private final GaussDbConnector dbConnector = new GaussDbConnector();

    @Override
    public SqlInputEntity processManualInput(String sql) {
        // 实时语法校验
        if (!sqlParser.validateSyntax(sql)) {
            throw new SqlParseException(ErrorCode.SQL_SYNTAX_ERROR);
        }
        // 解析SQL生成Schema
        SchemaDTO schema = sqlParser.parse(sql);
        // 构建返回实体
        SqlInputEntity entity = new SqlInputEntity(
                SqlInputEntity.InputType.MANUAL,
                sql
        );
        entity.setParsedSchema(JsonUtil.toJson(schema));
        return entity;
    }

    @Override
    public FileProcessResult processFileUpload(MultipartFile file) {
        return fileProcessor.process(file);
    }

    @Override
    public SchemaDTO processDbConnection(DbConnectionDTO connDTO) {
        return dbConnector.extractSchema(connDTO);
    }
}