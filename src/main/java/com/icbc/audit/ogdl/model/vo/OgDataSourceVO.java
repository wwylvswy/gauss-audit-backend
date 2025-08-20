package com.icbc.audit.ogdl.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OgDataSourceVO {
    private Long id;
    private String datasourceName;
    private String host;
    private Integer port;
    private String databaseName;
    private String username;
    private String password;
    private Long creatorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
