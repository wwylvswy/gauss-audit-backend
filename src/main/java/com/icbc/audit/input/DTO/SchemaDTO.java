package com.icbc.audit.input.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL解析后的结构化数据对象
 *
 * 功能说明：
 * 1. 封装SQL解析器生成的数据库结构元数据
 * 2. 包含表、列、索引等核心元素定义
 * 3. 支持GaussDB特殊数据类型标记
 * 4. 提供JSON序列化能力
 */
@Data
public class SchemaDTO {
    private String tableName;
    private String createTableSql;
    private List<ColumnDTO> columns = new ArrayList<>();
    private List<String> indexes = new ArrayList<>();
    private List<String> constraints = new ArrayList<>();
    private String tableComment;

    // 构造器
    public SchemaDTO() {}
}
