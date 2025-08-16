package com.icbc.audit.assit.util;

import com.icbc.audit.assit.exception.BusinessException;
import com.icbc.audit.assit.exception.ErrorCode;
import com.icbc.audit.assit.vo.ColumnDTO;
import com.icbc.audit.assit.vo.SchemaDTO;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.stereotype.Component;

@Component
public class GaussSqlParser {
    public SchemaDTO parse(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (!(statement instanceof CreateTable)) {
                throw new BusinessException(ErrorCode.SQL_SYNTAX_ERROR);
            }

            CreateTable createTable = (CreateTable) statement;
            SchemaDTO schema = new SchemaDTO();
            schema.setTableName(createTable.getTable().getName());

            createTable.getColumnDefinitions().forEach(colDef -> {
                schema.getColumns().add(new ColumnDTO(
                        colDef.getColumnName(),
                        colDef.getColDataType().toString()
                ));
            });
            return schema;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SQL_SYNTAX_ERROR, e);
        }
    }
}