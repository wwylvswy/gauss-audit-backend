package com.icbc.audit.assit.vo;

public class ColumnDTO {
    private String name;
    private String dataType;

    public ColumnDTO(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}