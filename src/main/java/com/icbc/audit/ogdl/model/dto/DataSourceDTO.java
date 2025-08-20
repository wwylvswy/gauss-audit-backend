package com.icbc.audit.ogdl.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DataSourceDTO {
    private Long id;

    @NotBlank(message = "数据源名称不能为空")
    private String datasourceName;

    @NotBlank(message = "主机地址不能为空")
    private String host;

    @Min(value = 1, message = "端口号不能小于 1")
    @Max(value = 65535, message = "端口号不能大于 65535")
    private Integer port;

    @NotBlank(message = "数据库名称不能为空")
    private String databaseName;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "创建人不能为空")
    private String creator;

    private Boolean status = true;
}
