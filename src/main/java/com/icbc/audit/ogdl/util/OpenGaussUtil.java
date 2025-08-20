package com.icbc.opengauss.util;
import com.icbc.audit.ogdl.model.entity.OgDataSourceEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OpenGaussUtil {

    /**
     * 根据数据源实体获取数据库连接 (默认连接到指定的databaseName)
     *
     * @param dataSource   数据源实体
     * @param databaseName 数据库名
     * @return Connection对象
     * @throws SQLException SQL异常
     */
    public static Connection getConnection(OgDataSourceEntity dataSource, String databaseName) throws SQLException {
        String url = String.format("jdbc:postgresql://%s:%d/%s", dataSource.getHost(), dataSource.getPort(), databaseName);
        return DriverManager.getConnection(url, dataSource.getUsername(), dataSource.getPassword());
    }

    /**
     * 根据数据源实体获取数据库连接 (连接到默认数据库，如postgres)
     *
     * @param dataSource 数据源实体
     * @return Connection对象
     * @throws SQLException SQL异常
     */
    public static Connection getConnection(OgDataSourceEntity dataSource) throws SQLException {
        String defaultDb = dataSource.getDatabaseName() != null && !dataSource.getDatabaseName().isEmpty() ?
                dataSource.getDatabaseName() : "postgres";
        return getConnection(dataSource, defaultDb);
    }

    /**
     * 从连接中获取数据库列表
     *
     * @param conn 数据库连接
     * @return 数据库名称列表
     * @throws SQLException SQL异常
     */
    public static List<String> getDatabases(Connection conn) throws SQLException {
        List<String> databases = new ArrayList<>();
        String sql = "SELECT datname FROM pg_database WHERE datistemplate = false AND datname NOT IN ('omm');";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                databases.add(rs.getString("datname"));
            }
        }
        databases.add(0, "postgres");
        return databases;
    }

    /**
     * 从连接中获取指定数据库下的表列表
     * @param conn 数据库连接
     * @param databaseName 数据库名
     * @return 表名称列表
     * @throws SQLException SQL异常
     */
    public static List<String> getTables(Connection conn, String databaseName) throws SQLException {
        List<String> tables = new ArrayList<>();
        // 查询当前数据库下的所有用户表
        String sql = """
            SELECT table_name\s
            FROM information_schema.tables\s
            WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
              AND table_type = 'BASE TABLE'
              AND table_name NOT IN ('gs_errors', 'gs_source', 'proc_coverage', 'snapshot')
            ORDER BY table_name;
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
        }
        return tables;
    }

    /**
     * 内部类，用于封装列的详细信息
     */
    public static class ColumnInfo {
        public String name;
        public String type;
        public String defaultValue;
        public boolean notNull;
        public String comment;

        public ColumnInfo(String name, String type, String defaultValue, boolean notNull, String comment) {
            this.name = name;
            this.type = type;
            this.defaultValue = defaultValue;
            this.notNull = notNull;
            this.comment = comment;
        }
    }

    /**
     * 内部类，用于封装主键信息
     */
    public static class PrimaryKeyInfo {
        public String constraintName;
        public List<String> columnNames;

        public PrimaryKeyInfo(String constraintName, List<String> columnNames) {
            this.constraintName = constraintName;
            this.columnNames = columnNames;
        }
    }

    /**
     * 内部类，用于封装外键信息
     */
    public static class ForeignKeyInfo {
        public String constraintName;
        public String columnName;
        public String foreignTableName;
        public String foreignColumnName;

        public ForeignKeyInfo(String constraintName, String columnName, String foreignTableName, String foreignColumnName) {
            this.constraintName = constraintName;
            this.columnName = columnName;
            this.foreignTableName = foreignTableName;
            this.foreignColumnName = foreignColumnName;
        }
    }

    /**
     * 内部类，用于封装分区信息
     */
    public static class PartitionInfo {
        public String strategy; // 分区策略，例如 'r' (range), 'l' (list), 'h' (hash)
        public String hashBucket; // Hash 分区的桶数 (如果是hash分区)
        public List<String> partitionKeys; // 分区键列表

        public PartitionInfo() {
            this.partitionKeys = new ArrayList<>();
        }

        // 简单转换策略代码为可读字符串 (可选)
        public String getStrategyDescription() {
            switch (strategy) {
                case "r": return "RANGE";
                case "l": return "LIST";
                case "h": return "HASH";
                default: return "UNKNOWN (" + strategy + ")";
            }
        }
    }

    /**
     * 获取指定表的详细建表语句 (DDL)，包含列、主键、外键、注释等信息。
     * 注意：此方法重建的DDL可能不包含所有分区细节或存储参数。
     *
     * @param conn         数据库连接
     * @param databaseName 数据库名
     * @param tableName    表名
     * @return 完整的建表语句
     * @throws SQLException SQL异常
     */
    public static String getTableDDL(Connection conn, String databaseName, String tableName) throws SQLException {
        StringBuilder ddl = new StringBuilder();

        // --- 1. 检查表是否为分区表父表 ---
        log.info("检查表是否为分区表父表");
        boolean isPartitionedParent = false;
        String partitionCheckSql = """
            SELECT c.relkind
            FROM pg_class c
            JOIN pg_namespace n ON c.relnamespace = n.oid
            WHERE c.relname = ?;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(partitionCheckSql)) {
            pstmt.setString(1, tableName);
            // pstmt.setString(2, schemaName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String relKind = rs.getString("relkind");
                if ("p".equals(relKind)) {
                    isPartitionedParent = true;
                }
            }
            rs.close();
        }

        // --- 2. 获取列信息 ---
        log.info("获取列信息");
        Map<String, ColumnInfo> columns = new HashMap<>();
        String columnSql = """
                SELECT
                    a.attname AS column_name,
                    pg_catalog.format_type(a.atttypid, a.atttypmod) AS data_type,
                    CASE WHEN a.atthasdef THEN pg_get_expr(d.adbin, d.adrelid) ELSE '' END AS default_value,
                    CASE WHEN a.attnotnull THEN 'NOT NULL' ELSE '' END AS not_null,
                    col_description(a.attrelid, a.attnum) AS column_comment
                FROM pg_catalog.pg_attribute a
                         LEFT JOIN pg_catalog.pg_attrdef d ON (a.attrelid = d.adrelid AND a.attnum = d.adnum)
                         JOIN pg_catalog.pg_class c ON (a.attrelid = c.oid)
                         JOIN pg_catalog.pg_namespace n ON (c.relnamespace = n.oid)
                WHERE c.relname = ? AND a.attnum > 0 AND NOT a.attisdropped
                ORDER BY a.attnum;
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(columnSql)) {
            pstmt.setString(1, tableName);
            // pstmt.setString(2, schemaName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String colName = rs.getString("column_name");
                String dataType = rs.getString("data_type");
                String defaultValue = rs.getString("default_value");
                boolean notNull = "NOT NULL".equals(rs.getString("not_null"));
                String comment = rs.getString("column_comment");
                columns.put(colName, new ColumnInfo(colName, dataType, defaultValue, notNull, comment));
            }
            rs.close();
        }

        // --- 3. 获取主键信息 ---
        log.info("获取主键信息");
        PrimaryKeyInfo primaryKeyInfo = null;
        String pkSql = """
                SELECT conname, pg_get_constraintdef(c.oid) AS condef
                FROM pg_catalog.pg_constraint c
                         JOIN pg_catalog.pg_class t ON (c.conrelid = t.oid)
                         JOIN pg_catalog.pg_namespace n ON (t.relnamespace = n.oid)
                WHERE t.relname = ? AND c.contype = 'p';
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(pkSql)) {
            pstmt.setString(1, tableName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String conName = rs.getString("conname");
                String conDef = rs.getString("condef");
                // condef 格式类似 "PRIMARY KEY (col1, col2)"
                String colsPart = conDef.replaceAll("PRIMARY KEY \\((.*)\\)", "$1");
                List<String> pkCols = new ArrayList<>();
                for (String col : colsPart.split(",")) {
                    pkCols.add(col.trim());
                }
                primaryKeyInfo = new PrimaryKeyInfo(conName, pkCols);
            }
            rs.close();
        }

        // --- 4. 获取外键信息 ---
        log.info("获取外键信息");
        List<ForeignKeyInfo> foreignKeys = new ArrayList<>();
        String fkSql = """
                SELECT conname,
                       a1.attname AS column_name,
                       confrelid::regclass AS foreign_table_name,
                       a2.attname AS foreign_column_name
                FROM pg_constraint c
                         JOIN pg_class t1 ON (c.conrelid = t1.oid)
                         JOIN pg_namespace n1 ON (t1.relnamespace = n1.oid)
                         JOIN pg_attribute a1 ON (a1.attrelid = c.conrelid AND a1.attnum = c.conkey[1])
                         JOIN pg_attribute a2 ON (a2.attrelid = c.confrelid AND a2.attnum = c.confkey[1])
                WHERE t1.relname = ? AND c.contype = 'f';
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(fkSql)) {
            pstmt.setString(1, tableName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String conName = rs.getString("conname");
                String columnName = rs.getString("column_name");
                String foreignTableNameRaw = rs.getString("foreign_table_name");
                // confrelid::regclass 可能返回带引号的名称，需要处理
                String foreignTableName = foreignTableNameRaw != null ? foreignTableNameRaw.replace("\"", "") : "";
                String foreignColumnName = rs.getString("foreign_column_name");
                foreignKeys.add(new ForeignKeyInfo(conName, columnName, foreignTableName, foreignColumnName));
            }
            rs.close();
        }

        // --- 5. 获取分区信息 (如果适用) ---
        log.info("获取分区信息");
        PartitionInfo partitionInfo = new PartitionInfo(); // 初始化，即使不是分区表
        if (isPartitionedParent) {
            // 5a. 获取分区策略和 Hash 桶数 (严格使用用户提供的SQL)
            String partInfoSql = """
                SELECT
                    p.relname AS partition_name,
                    p.partstrategy AS strategy,
                    p.boundaries AS hash_bucket
                FROM
                    pg_partition p
                JOIN
                    pg_class c ON p.parentid = c.oid
                WHERE
                    c.relname = ?
                    AND p.parttype = 'p';
                """;
            try (PreparedStatement pstmt = conn.prepareStatement(partInfoSql)) {
                pstmt.setString(1, tableName); // 绑定父表名
                ResultSet rs = pstmt.executeQuery();
                // 注意：一个父表可能有多个物理分区，这里我们只关心策略和桶数（假设它们对所有分区是一致的）
                // 或者只取第一个结果。如果策略不同，逻辑会更复杂。
                if (rs.next()) {
                    // partition_name 在这里主要是为了匹配，我们主要关心 strategy 和 hash_bucket
                    partitionInfo.strategy = rs.getString("strategy");
                    partitionInfo.hashBucket = rs.getString("hash_bucket"); // 可能为 null
                    // 如果需要处理多个分区的情况，可以在这里循环 rs.next()
                }
                rs.close();
            } catch (SQLException e) {
                // 忽略错误，可能表没有分区或查询失败
                System.err.println("Warning: Could not fetch basic partition info using provided SQL for " + tableName + ": " + e.getMessage());
            }

            // 5b. 获取分区键 (严格使用用户提供的SQL)
            String partKeySql = """
                SELECT
                    a.attname AS partition_key
                FROM
                    pg_attribute a
                JOIN
                    pg_partition p ON a.attrelid = p.parentid
                WHERE
                    p.relname = ?
                    AND a.attnum = ANY(p.partkey);
                """;
            try (PreparedStatement pstmt = conn.prepareStatement(partKeySql)) {
                pstmt.setString(1, tableName); // 绑定父表名 (在pg_class中的relname，用于关联pg_partition的parentid)
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String key = rs.getString("partition_key");
                    if (key != null) {
                        partitionInfo.partitionKeys.add(key);
                    }
                }
                rs.close();
            } catch (SQLException e) {
                // 忽略错误
                System.err.println("Warning: Could not fetch partition keys using provided SQL for " + tableName + ": " + e.getMessage());
            }
        }

        // --- 6. 构建 DDL ---
        log.info("构建 DDL 语句");
        if (isPartitionedParent) {
            ddl.append("-- 注意：这是一个分区表的父表\n");
        }
        ddl.append("CREATE TABLE ").append(tableName).append(" (\n");

        List<String> columnDefs = new ArrayList<>();
        for (ColumnInfo colInfo : columns.values()) {
            StringBuilder colDef = new StringBuilder();
            colDef.append("    ").append(colInfo.name).append(" ").append(colInfo.type);
            if (colInfo.defaultValue != null && !colInfo.defaultValue.isEmpty()) {
                // 简单处理默认值，对于复杂的如函数调用可能需要特殊处理
                colDef.append(" DEFAULT ").append(colInfo.defaultValue);
            }
            if (colInfo.notNull) {
                colDef.append(" NOT NULL");
            }
            columnDefs.add(colDef.toString());
        }
        ddl.append(String.join(",\n", columnDefs));

        if (primaryKeyInfo != null) {
            ddl.append(",\n    CONSTRAINT ").append(primaryKeyInfo.constraintName)
                    .append(" PRIMARY KEY (").append(String.join(", ", primaryKeyInfo.columnNames)).append(")");
        }

        for (ForeignKeyInfo fkInfo : foreignKeys) {
            ddl.append(",\n    CONSTRAINT ").append(fkInfo.constraintName)
                    .append(" FOREIGN KEY (").append(fkInfo.columnName).append(")")
                    .append(" REFERENCES ").append(fkInfo.foreignTableName)
                    .append(" (").append(fkInfo.foreignColumnName).append(")");
        }

        ddl.append("\n)");

        // 如果是分区表父表，添加 PARTITION BY 子句
        if (isPartitionedParent && !partitionInfo.partitionKeys.isEmpty()) {
            ddl.append(" PARTITION BY ").append(partitionInfo.getStrategyDescription())
                    .append(" (").append(String.join(", ", partitionInfo.partitionKeys)).append(")");
            // 如果是 Hash 分区且有桶数信息，可以添加注释
            if ("h".equals(partitionInfo.strategy) && partitionInfo.hashBucket != null && !partitionInfo.hashBucket.isEmpty()) {
                ddl.append(" -- HASH BUCKETS: ").append(partitionInfo.hashBucket);
            }
        } else if (isPartitionedParent) {
            // 分区表但未能获取到键信息
            ddl.append(" PARTITION BY /* STRATEGY AND KEY UNKNOWN */ (...)");
        }

        ddl.append(";");

        // --- 6. 添加列注释 ---
        log.info("添加列注释");
        for (ColumnInfo colInfo : columns.values()) {
            if (colInfo.comment != null && !colInfo.comment.isEmpty()) {
                ddl.append("\nCOMMENT ON COLUMN ").append(tableName).append(".").append(colInfo.name)
                        .append(" IS '").append(colInfo.comment.replace("'", "''")).append("';"); // 转义单引号
            }
        }

        // --- 7. 添加表注释 (如果需要) ---
        log.info("添加表注释");
        String tableCommentSql = """
                SELECT obj_description(c.oid) AS table_comment
                FROM pg_class c JOIN pg_namespace n ON (c.relnamespace = n.oid)
                WHERE c.relname = ?;
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(tableCommentSql)) {
            pstmt.setString(1, tableName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String tableComment = rs.getString("table_comment");
                if (tableComment != null && !tableComment.isEmpty()) {
                    ddl.append("\nCOMMENT ON TABLE ").append(tableName)
                            .append(" IS '").append(tableComment.replace("'", "''")).append("';");
                }
            }
            rs.close();
        }

        return ddl.toString();
    }
}



