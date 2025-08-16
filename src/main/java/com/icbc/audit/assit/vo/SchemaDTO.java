package com.icbc.audit.assit.vo;

import java.util.ArrayList;
import java.util.List;

public class SchemaDTO {
    private String tableName;
    private List<ColumnDTO> columns = new ArrayList<>();

    // Getters and Setters
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public List<ColumnDTO> getColumns() { return columns; }
    public void setColumns(List<ColumnDTO> columns) { this.columns = columns; }
}