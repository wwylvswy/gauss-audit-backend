package com.icbc.audit.input.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * SQL输入实体类 - 负责存储用户输入的SQL信息
 * 核心功能：
 * 1. 标识SQL输入来源类型（手动输入/文件上传/数据库连接）
 * 2. 存储原始SQL内容和解析后的结构化JSON
 * 3. 记录输入来源详情（文件路径/数据库连接信息）
 * 4. 记录文件字符编码格式（针对文件上传）
 * 5. 自动记录创建时间戳
 */

/**
 * SQL输入实体类，用于存储用户输入的SQL信息
 */
@Data
public class SqlInputEntity {
    // 主键ID
    private Long id;

    // 输入类型枚举 (MANUAL:手动输入, FILE:文件上传, DB_CONNECTION:数据库连接)
    private InputType inputType;

    // 原始SQL内容或文件内容
    private String originalContent;

    // 解析后的结构化JSON Schema
    private String parsedSchema;

    // 来源详情（文件路径或DB连接信息）
    private String sourceDetails;

    // 文件编码格式（如UTF-8, GBK等）
    private String charSet;

    // 输入创建时间
    private LocalDateTime createTime = LocalDateTime.now();

    // 构造函数
    public SqlInputEntity() {}

    public SqlInputEntity(InputType inputType, String originalContent) {
        this.inputType = inputType;
        this.originalContent = originalContent;
    }

    // 枚举定义
    public enum InputType {
        MANUAL,       // 手动输入
        FILE,         // 文件上传
        DB_CONNECTION // 数据库连接
    }

    @Override
    public String toString() {
        return "SqlInputEntity{" +
                "id=" + id +
                ", inputType=" + inputType +
                ", originalContent='" + originalContent + '\'' +
                ", sourceDetails='" + sourceDetails + '\'' +
                ", charSet='" + charSet + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}