package com.icbc.audit.input.DTO;

import lombok.Data;

/**
 * 列信息数据传输对象 - 负责封装表列的定义信息
 * 核心功能：
 * 1. 存储列名和数据类型
 * 2. 标记是否为空和主键状态
 * 3. 标识GaussDB特殊数据类型（向量等）
 * 4. 存储列约束信息（唯一性、外键等）
 * 5. 提供格式化输出方法
 */

@Data
public class ColumnDTO {
    private String name;
    private String dataType;
    private boolean isNullable;
    private boolean isPrimaryKey;
    // 是否为GaussDB特殊类型（如向量）
    private boolean specialType;
    // 约束信息
    private String constraints;

    // 构造器
    public ColumnDTO(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }
}