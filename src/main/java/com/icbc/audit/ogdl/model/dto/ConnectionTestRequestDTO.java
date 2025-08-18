package com.icbc.audit.ogdl.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "数据库连接测试请求参数")
@Data
public class ConnectionTestRequestDTO {
    @Schema(description = "数据库主机IP", example = "192.168.1.100")
    @NotBlank(message = "主机IP不能为空")
    private String host;

    @Schema(description = "数据库端口", example = "5432")
    @NotNull(message = "端口不能为空")
    private Integer port;

    @Schema(description = "数据库名称", example = "postgres")
    @NotBlank(message = "数据库名称不能为空")
    private String databaseName;

    @Schema(description = "用户名", example = "gaussdb")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
