package com.icbc.audit.assits.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetRulesRequestDTO {
    private List<String> categories;
    private String version;

    // 构造函数（可选）
    public GetRulesRequestDTO() {}

    public GetRulesRequestDTO(List<String> categories, String version) {
        this.categories = categories;
        this.version = version;
    }
}
