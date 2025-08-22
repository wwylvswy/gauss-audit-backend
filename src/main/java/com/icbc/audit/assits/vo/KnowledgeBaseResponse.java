package com.icbc.audit.assits.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private boolean success;
    private int total;
    private DataContent data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataContent {
        private String businessRules;
        private String generalRules;
    }
}

