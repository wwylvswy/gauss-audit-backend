package com.icbc.audit.assit.parser;
// ... (代码与之前版本完全相同)

import com.icbc.audit.assit.parser.vo.ParsedTable;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SqlParserService {

    public ParsedTable parseDdl(String ddlText) throws Exception {
        Statement statement = CCJSqlParserUtil.parse(ddlText);
        if (!(statement instanceof CreateTable)) {
            throw new IllegalArgumentException("提供的SQL不是一个有效的CREATE TABLE语句。");
        }
        CreateTable createTable = (CreateTable) statement;
        ParsedTable parsedTable = new ParsedTable();

        parsedTable.setTableName(createTable.getTable().getName().replace("`", ""));

        if (createTable.getColumnDefinitions() != null) {
            String fields = createTable.getColumnDefinitions()
                    .stream()
                    .map(ColumnDefinition::toString)
                    .collect(Collectors.joining(",\n  "));
            parsedTable.setFields(fields);
        }

        if (createTable.getIndexes() != null) {
            String indexes = createTable.getIndexes()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(";\n  "));
            parsedTable.setIndexes(indexes);
        }

        if (createTable.getTableOptionsStrings() != null) {
            String options = String.join(" ", createTable.getTableOptionsStrings());
            parsedTable.setOptions(options);
        }

        return parsedTable;
    }
}