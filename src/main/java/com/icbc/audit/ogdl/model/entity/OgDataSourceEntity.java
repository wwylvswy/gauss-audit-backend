package com.icbc.audit.ogdl.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("og_datasource")
public class OgDataSourceEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("datasource_name")
    private String datasourceName;
    private String host;
    private Integer port;

    @TableField("database_name")
    private String databaseName;
    private String username;
    private String password;
    private String creator;
    private Boolean status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;
}
