package com.icbc.audit.ogdl.service.impl;


import com.icbc.audit.ogdl.model.dto.ConnectionTestRequestDTO;
import com.icbc.audit.ogdl.model.dto.ConnectionTestResultDTO;
import com.icbc.audit.ogdl.model.dto.TableMetadataDTO;
import com.icbc.audit.ogdl.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    // 简单映射：数据库类型到OpenGauss/PostgreSQL类型（可根据需要扩展）
    private static final Map<String, String> TYPE_MAPPING = new ConcurrentHashMap<>();
    static {
        TYPE_MAPPING.put("character varying", "character varying");
        TYPE_MAPPING.put("varchar", "character varying");
        TYPE_MAPPING.put("text", "text");
        TYPE_MAPPING.put("integer", "integer");
        TYPE_MAPPING.put("int4", "integer");
        TYPE_MAPPING.put("bigint", "bigint");
        TYPE_MAPPING.put("int8", "bigint");
        TYPE_MAPPING.put("smallint", "smallint");
        TYPE_MAPPING.put("int2", "smallint");
        TYPE_MAPPING.put("numeric", "numeric");
        TYPE_MAPPING.put("decimal", "numeric");
        TYPE_MAPPING.put("real", "real");
        TYPE_MAPPING.put("float4", "real");
        TYPE_MAPPING.put("double precision", "double precision");
        TYPE_MAPPING.put("float8", "double precision");
        TYPE_MAPPING.put("boolean", "boolean");
        TYPE_MAPPING.put("bool", "boolean");
        TYPE_MAPPING.put("date", "date");
        TYPE_MAPPING.put("time without time zone", "time without time zone");
        TYPE_MAPPING.put("timestamp without time zone", "timestamp without time zone");
        TYPE_MAPPING.put("timestamptz", "timestamp with time zone");
        TYPE_MAPPING.put("timestamp with time zone", "timestamp with time zone");
        TYPE_MAPPING.put("uuid", "uuid");
    }

    /**

    // JDBC URL format for OpenGauss
    private static final String OG_JDBC_URL_TEMPLATE = "jdbc:opengauss://%s:%d/%s";

    @Override
    public ConnectionTestResultDTO testConnection(ConnectionTestRequestDTO request) throws SQLException {
        ConnectionTestResultDTO result = new ConnectionTestResultDTO();
        String url = String.format(OG_JDBC_URL_TEMPLATE, request.getHost(), request.getPort(), request.getDatabase());

        // Note: In production, avoid hardcoding driver class name. Use proper configuration.
        try {
            Class.forName("org.opengauss.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("找不到OpenGauss JDBC驱动", e);
        }

        // Establish connection with a 5-second timeout
        try (Connection conn = DriverManager.getConnection(url, request.getUsername(), request.getPassword())) {
            // 5秒超时
            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(5));
            boolean isValid = conn.isValid((int) TimeUnit.SECONDS.toMillis(5));
            result.setValid(isValid);

            if (isValid) {
                // Query database version
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT version()")) {
                    if (rs.next()) {
                        result.setVersion(rs.getString(1));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<TableMetadataDTO> getTableMetadata(String datasourceId, String tableName) throws SQLException {
        // Simplified: In real scenario, use datasourceId to get connection details
        // For demo, we'll assume a fixed connection or retrieve from a config map
        // Here we just throw an exception to indicate it's not fully implemented
        // but show the structure of how it would work.

        // Example connection details (should come from config based on datasourceId)
        ConnectionTestRequestDTO mockRequest = new ConnectionTestRequestDTO();
        // Replace with actual logic to get from datasourceId
        mockRequest.setHost("localhost");
        mockRequest.setPort(5432);
        mockRequest.setDatabase("postgres");
        mockRequest.setUsername("gaussdb");
        // NEVER hardcode in production!
        mockRequest.setPassword("password");

        String url = String.format(OG_JDBC_URL_TEMPLATE, mockRequest.getHost(), mockRequest.getPort(), mockRequest.getDatabase());

        try {
            Class.forName("org.opengauss.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("找不到OpenGauss JDBC驱动", e);
        }

        List<TableMetadataDTO> metadataList = new ArrayList<>();
        String query = """
            SELECT
                column_name,
                data_type,
                is_nullable,
                column_default,
                col_description((table_schema||'.'||table_name)::regclass, ordinal_position) as column_comment
            FROM
                information_schema.columns
            WHERE
                table_name = ?
            ORDER BY
                ordinal_position;
            """;

        try (Connection conn = DriverManager.getConnection(url, mockRequest.getUsername(), mockRequest.getPassword());
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(10));
            pstmt.setString(1, tableName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TableMetadataDTO meta = new TableMetadataDTO();
                    meta.setColumnName(rs.getString("column_name"));
                    meta.setDataType(rs.getString("data_type"));
                    meta.setIsNullable("YES".equalsIgnoreCase(rs.getString("is_nullable")));
                    meta.setDefaultValue(rs.getString("column_default"));
                    meta.setComment(rs.getString("column_comment"));
                    metadataList.add(meta);
                }
            }
        }
        return metadataList;
    }

    @Override
    public String generateSql(String datasourceId, String tableName) throws SQLException {
        List<TableMetadataDTO> metadataList = getTableMetadata(datasourceId, tableName);
        if (metadataList.isEmpty()) {
            throw new SQLException("无法获取表 " + tableName + " 的元数据");
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");

        for (int i = 0; i < metadataList.size(); i++) {
            TableMetadataDTO meta = metadataList.get(i);
            sqlBuilder.append("\"").append(meta.getColumnName()).append("\"");
            if (i < metadataList.size() - 1) {
                sqlBuilder.append(", ");
            }
        }

        sqlBuilder.append("\nFROM \"").append(tableName).append("\";\n");

        return sqlBuilder.toString();
    }
    */


    // JDBC URL format for PostgreSQL-compatible OpenGauss
    // 注意：这里使用的是 postgresql 前缀
    private static final String OG_PG_JDBC_URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s";

    @Override
    public ConnectionTestResultDTO testConnection(ConnectionTestRequestDTO request) throws SQLException {
        ConnectionTestResultDTO result = new ConnectionTestResultDTO();
        // 使用 PostgreSQL 兼容的 URL 格式
        String url = String.format(OG_PG_JDBC_URL_TEMPLATE, request.getHost(), request.getPort(), request.getDatabaseName());

        // 加载 PostgreSQL JDBC 驱动
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("找不到PostgreSQL JDBC驱动", e);
        }

        // 建立连接并设置5秒超时
        try (Connection conn = DriverManager.getConnection(url, request.getUsername(), request.getPassword())) {
            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(5)); // 5秒超时
            boolean isValid = conn.isValid((int) TimeUnit.SECONDS.toMillis(5));
            result.setValid(isValid);

            if (isValid) {
                // 查询数据库版本
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT version()")) {
                    if (rs.next()) {
                        result.setVersion(rs.getString(1));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<TableMetadataDTO> getTableMetadata(String datasourceId, String tableName) throws SQLException {
        // 简化处理：实际应用中应根据 datasourceId 获取连接详情
        // 示例连接详情 (应根据 datasourceId 从配置获取)
        ConnectionTestRequestDTO mockRequest = new ConnectionTestRequestDTO();
        // 替换为根据 datasourceId 获取的实际逻辑
        mockRequest.setHost("175.178.89.189");
        mockRequest.setPort(5432);
        mockRequest.setDatabaseName("postgres");
        mockRequest.setUsername("gaussdb");
        // 生产环境切勿硬编码!
        mockRequest.setPassword("Pwd@1234");

        // 使用 PostgreSQL 兼容的 URL 格式
        String url = String.format(OG_PG_JDBC_URL_TEMPLATE, mockRequest.getHost(), mockRequest.getPort(), mockRequest.getDatabaseName());

        // 加载 PostgreSQL JDBC 驱动
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("找不到PostgreSQL JDBC驱动", e);
        }

        List<TableMetadataDTO> metadataList = new ArrayList<>();
        // 使用 information_schema 查询元数据，这在PG兼容模式下是有效的
//        String query = """
//            SELECT
//                column_name,
//                data_type,
//                is_nullable,
//                column_default,
//                col_description((table_schema||'.'||table_name)::regclass, ordinal_position) as column_comment
//            FROM
//                information_schema.columns
//            WHERE
//                table_name = ?
//            ORDER BY
//                ordinal_position;
//            """;

//        try (Connection conn = DriverManager.getConnection(url, mockRequest.getUsername(), mockRequest.getPassword());
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//
//            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(10));
//            pstmt.setString(1, tableName);
//
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    TableMetadataDTO meta = new TableMetadataDTO();
//                    meta.setColumnName(rs.getString("column_name"));
//                    meta.setDataType(rs.getString("data_type"));
//                    meta.setIsNullable("YES".equalsIgnoreCase(rs.getString("is_nullable")));
//                    meta.setDefaultValue(rs.getString("column_default"));
//                    meta.setComment(rs.getString("column_comment"));
//                    System.out.println(meta.toString());
//                    metadataList.add(meta);
//                }
//            }
//        }
//        return metadataList;

        // 1. 获取基础列信息
        String baseQuery = """
            SELECT
                c.column_name,
                c.data_type,
                c.is_nullable,
                c.column_default,
                col_description((c.table_schema||'.'||c.table_name)::regclass, c.ordinal_position) as column_comment
            FROM
                information_schema.columns c
            WHERE
                c.table_name = ?
                AND c.table_schema = 'public' -- 根据实际schema调整
            ORDER BY
                c.ordinal_position;
            """;

        Map<String, TableMetadataDTO> columnMap = new LinkedHashMap<>(); // 保持顺序

        try (Connection conn = DriverManager.getConnection(url, mockRequest.getUsername(), mockRequest.getPassword());
             PreparedStatement pstmt = conn.prepareStatement(baseQuery)) {

            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(10));
            pstmt.setString(1, tableName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TableMetadataDTO meta = new TableMetadataDTO();
                    String colName = rs.getString("column_name");
                    meta.setColumnName(colName);
                    meta.setDataType(rs.getString("data_type"));
                    meta.setIsNullable("YES".equalsIgnoreCase(rs.getString("is_nullable")));
                    meta.setDefaultValue(rs.getString("column_default"));
                    meta.setComment(rs.getString("column_comment"));
                    // 初始化新增字段
                    meta.setIsPrimaryKey(false);
                    meta.setPrimaryKeyConstraintName(null);
                    meta.setIsForeignKey(false);
                    meta.setForeignKeyConstraintName(null);
                    meta.setForeignKeyReferenceTable(null);
                    meta.setForeignKeyReferenceColumn(null);
                    meta.setIsPartitionKey(false);

                    metadataList.add(meta);
                    columnMap.put(colName, meta);
                }
            }
        }

        if (metadataList.isEmpty()) {
            log.info("表 {} 在 schema 'public' 中未找到列或不存在。", tableName);
            return metadataList; // 表不存在或无列
        }

        // 2. 获取主键信息
        String pkQuery = """
            SELECT
                kcu.column_name,
                tc.constraint_name
            FROM
                information_schema.table_constraints tc
            JOIN
                information_schema.key_column_usage kcu
                ON tc.constraint_name = kcu.constraint_name
                AND tc.table_schema = kcu.table_schema
                AND tc.table_name = kcu.table_name
            WHERE
                tc.constraint_type = 'PRIMARY KEY'
                AND tc.table_name = ?
                AND tc.table_schema = 'public'; -- 根据实际schema调整
            """;

        try (Connection conn = DriverManager.getConnection(url, mockRequest.getUsername(), mockRequest.getPassword());
             PreparedStatement pstmt = conn.prepareStatement(pkQuery)) {

            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(10));
            pstmt.setString(1, tableName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String colName = rs.getString("column_name");
                    String constraintName = rs.getString("constraint_name");
                    TableMetadataDTO meta = columnMap.get(colName);
                    if (meta != null) {
                        meta.setIsPrimaryKey(true);
                        meta.setPrimaryKeyConstraintName(constraintName);
                    }
                }
            }
        } catch (SQLException e) {
            log.warn("查询表 {} 的主键信息时出错: {}", tableName, e.getMessage());
        }

        // 3. 获取外键信息 (修正：确保获取引用列)
        String fkQuery = """
            SELECT DISTINCT -- 使用DISTINCT避免重复
                kcu.column_name,
                tc.constraint_name,
                ccu.table_name AS foreign_table_name,
                ccu.column_name AS foreign_column_name
            FROM
                information_schema.table_constraints tc
            JOIN
                information_schema.key_column_usage kcu
                ON tc.constraint_name = kcu.constraint_name
                AND tc.table_schema = kcu.table_schema
            JOIN
                information_schema.constraint_column_usage ccu
                ON ccu.constraint_name = tc.constraint_name
                AND ccu.table_schema = tc.table_schema
            WHERE
                tc.constraint_type = 'FOREIGN KEY'
                AND tc.table_name = ?
                AND tc.table_schema = 'public'; -- 根据实际schema调整
            """;

        try (Connection conn = DriverManager.getConnection(url, mockRequest.getUsername(), mockRequest.getPassword());
             PreparedStatement pstmt = conn.prepareStatement(fkQuery)) {

            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(10));
            pstmt.setString(1, tableName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String colName = rs.getString("column_name");
                    String constraintName = rs.getString("constraint_name");
                    String refTable = rs.getString("foreign_table_name");
                    String refColumn = rs.getString("foreign_column_name");
                    TableMetadataDTO meta = columnMap.get(colName);
                    if (meta != null) {
                        meta.setIsForeignKey(true);
                        meta.setForeignKeyConstraintName(constraintName);
                        meta.setForeignKeyReferenceTable(refTable);
                        meta.setForeignKeyReferenceColumn(refColumn);
                    }
                }
            }
        } catch (SQLException e) {
            log.warn("查询表 {} 的外键信息时出错: {}", tableName, e.getMessage());
        }

        // 4. 获取分区键信息 (修正：使用适用于OpenGauss的查询)
        // 查询 pg_partitioned_table 获取分区表的 partrelid
        // 然后查询 pg_attribute 获取分区键列名
        String partitionKeyQuery = """
            SELECT a.attname AS partition_key
            FROM pg_class c
            JOIN pg_namespace n ON c.relnamespace = n.oid
            JOIN pg_partitioned_table p ON c.oid = p.partrelid
            JOIN pg_attribute a ON a.attrelid = c.oid AND a.attnum = ANY(p.partattrs)
            WHERE c.relname = ?
              AND n.nspname = 'public'; -- 根据实际schema调整
            """;

        try (Connection conn = DriverManager.getConnection(url, mockRequest.getUsername(), mockRequest.getPassword());
             PreparedStatement pstmt = conn.prepareStatement(partitionKeyQuery)) {

            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(10));
            pstmt.setString(1, tableName);

            try (ResultSet rs = pstmt.executeQuery()) {
                // 标记所有找到的列为分区键
                while (rs.next()) {
                    String partitionKey = rs.getString("partition_key");
                    TableMetadataDTO meta = columnMap.get(partitionKey);
                    if (meta != null) {
                        log.debug("发现分区键: {}", partitionKey);
                        meta.setIsPartitionKey(true);
                    } else {
                        log.warn("在元数据中未找到分区键列: {}", partitionKey);
                    }
                }
                log.info("成功查询表 {} 的分区键信息。", tableName);
            }
        } catch (SQLException e) {
            // 分区信息查询可能因权限或表非分区表而失败，记录日志但不中断主流程
            log.warn("查询表 {} 的分区键信息时出错或表非分区表: {}", tableName, e.getMessage());
            // 不抛出异常，继续执行
        }

        return metadataList;
    }

    @Override
    public String generateSql(String datasourceId, String tableName) throws SQLException {
        List<TableMetadataDTO> metadataList = getTableMetadata(datasourceId, tableName);
        if (metadataList.isEmpty()) {
            throw new SQLException("无法获取表 " + tableName + " 的元数据");
        }
        StringBuilder sqlBuilder = new StringBuilder();
//        sqlBuilder.append("CREATE TABLE \"").append(tableName).append("\" (\n");

//        for (int i = 0; i < metadataList.size(); i++) {
//            TableMetadataDTO meta = metadataList.get(i);
//            sqlBuilder.append("    \"").append(meta.getColumnName()).append("\" ");
//
//            // 映射数据类型
//            String dbType = meta.getDataType() != null ? meta.getDataType().toLowerCase() : "text";
//            // 如果找不到映射，则使用原始类型
//            String mappedType = TYPE_MAPPING.getOrDefault(dbType, dbType);
//            sqlBuilder.append(mappedType);
//
//            // 处理非空约束
//            if (meta.getIsNullable() != null && !meta.getIsNullable()) {
//                sqlBuilder.append(" NOT NULL");
//            }
//
//            // 处理默认值 (简单处理，实际可能需要更复杂的转义)
//            if (meta.getDefaultValue() != null && !meta.getDefaultValue().trim().isEmpty()) {
//                // PostgreSQL/OpenGauss 对默认值格式要求较严格，这里简化处理
//                // 真实场景可能需要根据数据类型调整默认值格式
//                String defaultValue = meta.getDefaultValue().trim();
//                // 简单判断是否是函数调用或特殊关键字，否则加引号
//                if (defaultValue.startsWith("nextval(") ||
//                        // 序列
//                        defaultValue.equalsIgnoreCase("NULL") ||
//                        // 数字
//                        defaultValue.matches("^\\d+(\\.\\d+)?$") ||
//                        defaultValue.toLowerCase().startsWith("current_")) {
//                    // current_timestamp 等
//                    sqlBuilder.append(" DEFAULT ").append(defaultValue);
//                } else {
//                    // 字符串等其他类型默认值加引号
//                    // 转义单引号
//                    sqlBuilder.append(" DEFAULT '").append(defaultValue.replace("'", "''")).append("'");
//                }
//            }
//
//            // 添加注释 (如果需要，可以作为单独的COMMENT ON语句)
//            // if (meta.getComment() != null && !meta.getComment().trim().isEmpty()) {
//            //     // 注释通常作为独立语句添加
//            // }
//
//            if (i < metadataList.size() - 1) {
//                sqlBuilder.append(",");
//            }
//            sqlBuilder.append("\n");
//        }
//
//        sqlBuilder.append(");\n");

        // 添加注释 (作为独立语句)
        // 注意：information_schema.columns 的 column_comment 在 OpenGauss PG兼容模式下可能不直接可用或需要特殊查询
        // 这里假设 TableMetadataDTO.comment 是有效的
        // 如果需要，可以在这里添加 COMMENT ON COLUMN 语句
        /*
        for (TableMetadataDTO meta : metadataList) {
             if (meta.getComment() != null && !meta.getComment().trim().isEmpty()) {
                 sqlBuilder.append("COMMENT ON COLUMN \"").append(tableName).append("\".\"")
                           .append(meta.getColumnName()).append("\" IS '")
                           .append(meta.getComment().replace("'", "''")).append("';\n");
             }
        }
        */

        sqlBuilder.append("CREATE TABLE \"public\".\"").append(tableName).append("\" (\n"); // 添加schema

        List<String> primaryKeyColumns = new ArrayList<>();
        List<String> foreignKeyConstraints = new ArrayList<>();
        List<String> partitionKeyColumns = new ArrayList<>(); // 用于分区子句

        for (int i = 0; i < metadataList.size(); i++) {
            TableMetadataDTO meta = metadataList.get(i);
            sqlBuilder.append("    \"").append(meta.getColumnName()).append("\" ");

            String dbType = meta.getDataType() != null ? meta.getDataType().toLowerCase() : "text";
            String mappedType = TYPE_MAPPING.getOrDefault(dbType, dbType);
            sqlBuilder.append(mappedType);

            if (meta.getIsNullable() != null && !meta.getIsNullable()) {
                sqlBuilder.append(" NOT NULL");
            }

            if (meta.getDefaultValue() != null && !meta.getDefaultValue().trim().isEmpty()) {
                String defaultValue = meta.getDefaultValue().trim();
                // 简单处理常见默认值，实际可能需要更复杂的解析
                if (defaultValue.startsWith("nextval(") ||
                        defaultValue.equalsIgnoreCase("NULL") ||
                        defaultValue.matches("^\\d+(\\.\\d+)?$") ||
                        defaultValue.toLowerCase().startsWith("current_")) {
                    sqlBuilder.append(" DEFAULT ").append(defaultValue);
                } else {
                    // 假设是字符串或表达式，用单引号包裹（注意转义）
                    sqlBuilder.append(" DEFAULT '").append(defaultValue.replace("'", "''")).append("'");
                }
            }

            if (meta.getIsPrimaryKey()) {
                primaryKeyColumns.add("\"" + meta.getColumnName() + "\"");
                // 主键约束通常在列定义后统一添加，这里不立即添加
            }

            if (meta.getIsForeignKey()) {
                // 外键约束通常在所有列定义后添加
                // 注意：这里假引用的表和列存在且类型匹配
                foreignKeyConstraints.add(
                        "    CONSTRAINT \"" + meta.getForeignKeyConstraintName() + "\" FOREIGN KEY (\"" + meta.getColumnName() + "\") " +
                                "REFERENCES \"public\".\""+ meta.getForeignKeyReferenceTable() + "\" (\"" + meta.getForeignKeyReferenceColumn() + "\")"
                );
            }

            if (meta.getIsPartitionKey()) {
                partitionKeyColumns.add("\"" + meta.getColumnName() + "\"");
            }

            if (i < metadataList.size() - 1) {
                sqlBuilder.append(",");
            }
            sqlBuilder.append("\n");
        }

        // 添加主键约束
        if (!primaryKeyColumns.isEmpty()) {
            sqlBuilder.append("    , CONSTRAINT \"pk_").append(tableName).append("\" PRIMARY KEY (")
                    .append(String.join(", ", primaryKeyColumns))
                    .append(")\n");
        }

        // 添加外键约束
        if (!foreignKeyConstraints.isEmpty()) {
            for (String fk : foreignKeyConstraints) {
                sqlBuilder.append("    , ").append(fk).append("\n");
            }
        }

        // 注意：这里仅为示例，实际分区策略（RANGE, LIST, HASH）和边界值需要从元数据中获取
        // OpenGauss的分区创建语法比较复杂，通常不在CREATE TABLE语句末尾直接定义所有分区。
        // 这里仅示意性地添加 PARTITION BY 子句。
        if (!partitionKeyColumns.isEmpty()) {
            // 示例：假设是 LIST 分区，实际需要更多信息
            sqlBuilder.append(") PARTITION BY LIST (") // 或 RANGE, HASH
                    .append(String.join(", ", partitionKeyColumns))
                    .append(");\n");
            // 后续需要单独执行 ALTER TABLE ... ADD PARTITION ... 语句
            sqlBuilder.append("-- 注意：需要手动添加具体的分区定义 (ADD PARTITION) 语句\n");
        } else {
            sqlBuilder.append(");\n");
        }


        // 添加注释 (作为独立语句)
        for (TableMetadataDTO meta : metadataList) {
            if (meta.getComment() != null && !meta.getComment().trim().isEmpty()) {
                sqlBuilder.append("COMMENT ON COLUMN \"public\".\"").append(tableName).append("\".\"")
                        .append(meta.getColumnName()).append("\" IS '")
                        .append(meta.getComment().replace("'", "''")).append("';\n");
            }
        }

        return sqlBuilder.toString();
    }
}

// customer_accounts