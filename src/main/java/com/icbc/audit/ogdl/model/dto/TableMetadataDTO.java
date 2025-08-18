package com.icbc.audit.ogdl.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "表元数据信息")
@Data
public class TableMetadataDTO {
    @Schema(description = "列名")
    private String columnName;
    @Schema(description = "数据类型")
    private String dataType;
    @Schema(description = "是否允许为空")
    private Boolean isNullable;
    @Schema(description = "默认值")
    private String defaultValue;
    @Schema(description = "注释")
    private String comment;

    // --- 新增字段 ---
    @Schema(description = "是否为主键")
    private Boolean isPrimaryKey = false;
    @Schema(description = "主键约束名称")
    private String primaryKeyConstraintName;
    @Schema(description = "是否为外键")
    private Boolean isForeignKey = false;
    @Schema(description = "外键约束名称")
    private String foreignKeyConstraintName;
    @Schema(description = "外键引用的表")
    private String foreignKeyReferenceTable;
    @Schema(description = "外键引用的列")
    private String foreignKeyReferenceColumn;
    @Schema(description = "是否为分区键")
    private Boolean isPartitionKey = false;
}