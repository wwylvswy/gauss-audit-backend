package com.icbc.audit.input.service;

/**
 * 输入处理服务接口 - 定义输入处理的业务契约
 * 核心功能：
 * 1. 声明手动SQL处理方法
 * 2. 声明文件上传处理方法
 * 3. 声明数据库连接处理方法
 * 4. 规范输入验证逻辑
 * 5. 定义结果封装标准
 */


import com.icbc.audit.input.DTO.DbConnectionDTO;
import com.icbc.audit.input.DTO.FileProcessResult;
import com.icbc.audit.input.DTO.SchemaDTO;
import com.icbc.audit.input.entity.SqlInputEntity;
import org.springframework.web.multipart.MultipartFile;

public interface InputProcessingService {
    // 处理手动输入SQL
    SqlInputEntity processManualInput(String sql);

    // 处理文件上传
    FileProcessResult processFileUpload(MultipartFile file);

    // 处理数据库直连
    SchemaDTO processDbConnection(DbConnectionDTO connDTO);
}
