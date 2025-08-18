package com.icbc.audit.ogdl.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "连接测试结果")
@Data
public class ConnectionTestResultDTO {
    @Schema(description = "连接是否有效")
    private Boolean valid;
    @Schema(description = "数据库版本信息")
    private String version;
}
