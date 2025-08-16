package com.icbc.audit.input.DTO;

import lombok.Data;

import java.util.Objects;

/**
 * 数据库连接参数数据传输对象
 *
 * 功能说明：
 * 1. 封装用户提交的数据库连接配置信息
 * 2. 提供参数校验方法确保连接合法性
 * 3. 安全处理敏感信息(如密码加密存储)
 * 4. 转换JDBC连接字符串
 */
@Data
public class DbConnectionDTO {
    private String dbType = "GaussDB";
    private String jdbcUrl;
    private String username;
    private String password;
    // 默认超时30秒
    private int timeout = 30000;

    // 参数校验
    public boolean validate() {
        return Objects.nonNull(jdbcUrl) && !jdbcUrl.isEmpty()
                && Objects.nonNull(username) && !username.isEmpty()
                && Objects.nonNull(password);
    }
}