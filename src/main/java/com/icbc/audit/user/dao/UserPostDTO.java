package com.icbc.audit.user.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户新增、更新传输对象")
public class UserPostDTO implements Serializable {
    @Schema(description = "用户ID，修改时必填")
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

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;
}
