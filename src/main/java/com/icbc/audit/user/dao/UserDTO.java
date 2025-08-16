package com.icbc.audit.user.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户传输对象")
public class UserDTO implements Serializable {

    @Schema(description = "用户ID，仅修改/删除时必填")
    private Long id;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "是否启用")
    private Boolean enabled;

    // 查询条件专用字段
    @Schema(description = "分页页码，默认1")
    private Integer pageNo = 1;

    @Schema(description = "分页大小，默认10")
    private Integer pageSize = 10;
}