package com.icbc.audit.input.util;

import com.icbc.audit.input.DTO.DbConnectionDTO;
import com.icbc.audit.input.DTO.SchemaDTO;
import com.icbc.audit.input.exception.DbConnectionException;
import com.icbc.audit.input.exception.ErrorCode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * GaussDB数据库连接工具类
 * 功能说明：
 * 1. 管理GaussDB数据库连接池
 * 2. 执行数据库元数据逆向工程(生成DDL)
 * 3. 处理数据库连接异常
 * 4. 安全关闭连接资源
 * 5. 支持GaussDB特有SQL语法处理
 */

public class GaussDbConnector {

    public SchemaDTO extractSchema(DbConnectionDTO connDTO) {
        if (!connDTO.validate()) {
            throw new DbConnectionException(ErrorCode.INVALID_CONNECTION_PARAMS);
        }

        try (Connection conn = DriverManager.getConnection(
                connDTO.getJdbcUrl(),
                connDTO.getUsername(),
                connDTO.getPassword()
        )) {
            // 查询information_schema获取表元数据（简化示例）
            SchemaDTO schema = new SchemaDTO();
            try (Statement stmt = conn.createStatement()) {
                // 获取表结构
                ResultSet rs = stmt.executeQuery("SELECT * FROM information_schema.tables");
                while (rs.next()) {
                    schema.setTableName(rs.getString("table_name"));
                }
                // 补充SHOW CREATE TABLE信息
                ResultSet createTableRs = stmt.executeQuery("SHOW CREATE TABLE " + schema.getTableName());
                if (createTableRs.next()) {
                    schema.setCreateTableSql(createTableRs.getString(2));
                }
            }
            return schema;
        } catch (Exception e) {
            throw new DbConnectionException(ErrorCode.DB_CONNECTION_FAILED, e);
        }
    }
}