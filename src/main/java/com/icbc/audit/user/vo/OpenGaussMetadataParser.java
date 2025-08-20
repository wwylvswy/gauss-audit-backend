package com.icbc.audit.user.vo;

import lombok.Data;

//package com.icbc.audit.user.vo;
//
//import org.apache.calcite.adapter.jdbc.JdbcSchema;
//import org.apache.calcite.rel.RelNode;
//import org.apache.calcite.rel.externalize.RelWriterImpl;
//import org.apache.calcite.schema.SchemaPlus;
//import org.apache.calcite.schema.Table;
//import org.apache.calcite.sql.SqlDialect;
//import org.apache.calcite.sql.SqlExplainLevel;
//import org.apache.calcite.sql.dialect.PostgresqlSqlDialect;
//import org.apache.calcite.tools.Frameworks;
//import org.apache.calcite.tools.RelBuilder;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//
@Data
public class OpenGaussMetadataParser {
    private String tableName;
}
//
//    // OpenGauss 连接配置
//    private static final String DB_URL = "jdbc:postgresql://175.178.89.189:5432/postgres";
//    private static final String USER = "gaussdb";
//    private static final String PASSWORD = "Pwd@1234";
//
//    public static void main(String[] args) throws Exception {
//        // 1. 连接 OpenGauss 获取元数据
//        try (Connection ogConn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
//            // 2. 创建 Calcite 适配器
//            JdbcSchema schema = JdbcSchema.create(
//                    ogConn.unwrap(Connection.class),
//                    "opengauss_schema",
//                    null,
//                    null
//            );
//
//            // 3. 构建 Calcite 模型
//            SchemaPlus rootSchema = Frameworks.createRootSchema(true);
//            rootSchema.add("og", schema);  // 添加 OpenGauss 模式
//
//            // 4. 遍历所有表并生成 DDL
//            for (String tableName : schema.getTableNames()) {
//                // 获取表元数据
//                Table calciteTable = schema.getTable(tableName);
//
//                // 使用 RelBuilder 构建逻辑计划
//                RelBuilder builder = RelBuilder.create(Frameworks.newConfigBuilder()
//                        .defaultSchema(rootSchema.getSubSchema("og"))
//                        .build());
//                builder.scan(tableName);  // 扫描表结构
//                RelNode relNode = builder.build();  // 生成关系代数表达式
//
//                // 5. 生成 DDL 语句
//                SqlDialect dialect = PostgresqlSqlDialect.DEFAULT;
//                String ddl = relNode.explain(
//                                RelWriterImpl.class,
//                                SqlExplainLevel.ALL_ATTRIBUTES
//                        ).replace("LogicalTableScan", "CREATE TABLE " + tableName + " (\n")
//                        .replace("table=og", "")  // 清理冗余信息
//                        .replace("]", ");");       // 补充分号
//
//                System.out.println("-- DDL for: " + tableName);
//                System.out.println(ddl);
//            }
//        }
//    }
//}
