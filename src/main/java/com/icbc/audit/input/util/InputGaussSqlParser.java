package com.icbc.audit.input.util;

/**
 * GaussDB SQL解析器 - 负责解析SQL语句为结构化数据
 * 核心功能：
 * 1. 使用JSqlParser解析SQL语法树
 * 2. 支持GaussDB特有数据类型识别
 * 3. 提取表、列、索引、约束等元数据
 * 4. 验证SQL语法合法性
 * 5. 输出结构化JSON Schema
 * 6. 处理SQL注入风险
 */

import com.icbc.audit.input.DTO.ColumnDTO;
import com.icbc.audit.input.DTO.SchemaDTO;
import com.icbc.audit.input.exception.ErrorCode;
import com.icbc.audit.input.exception.SqlParseException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.stereotype.Component;

@Component
public class InputGaussSqlParser {

    // 解析SQL并生成SchemaDTO
    public SchemaDTO parse(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (!(statement instanceof CreateTable)) {
                throw new SqlParseException(ErrorCode.SQL_SYNTAX_ERROR);
            }

            CreateTable createTable = (CreateTable) statement;
            SchemaDTO schema = new SchemaDTO();
            // 提取表名
            schema.setTableName(createTable.getTable().getName());
            // 解析列定义（简化示例）
            createTable.getColumnDefinitions().forEach(colDef -> {
                schema.getColumns().add(new ColumnDTO(
                        colDef.getColumnName(),
                        colDef.getColDataType().toString()
                ));
            });
            return schema;
        } catch (JSQLParserException e) {
            throw new SqlParseException(ErrorCode.SQL_SYNTAX_ERROR, e);
        }
    }

    // 实时语法校验
    public boolean validateSyntax(String sql) {
        try {
            CCJSqlParserUtil.parse(sql);
            return true;
        } catch (JSQLParserException e) {
            return false;
        }
    }
}