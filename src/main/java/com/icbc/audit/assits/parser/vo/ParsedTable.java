package com.icbc.audit.assits.parser.vo;

import lombok.Data;
// ... (代码与之前版本完全相同)
@Data
public class ParsedTable {
    private String tableName;
    private String fields;
    private String indexes;
    private String options;
}