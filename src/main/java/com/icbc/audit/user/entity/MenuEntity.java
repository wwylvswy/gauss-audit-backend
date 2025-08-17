package com.icbc.audit.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_menu")
public class MenuEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("parent_id")
    private Long parentId;

    @TableField("path")
    private String path;

    @TableField("component")
    private String component;

    @TableField("name")
    private String name;

    @TableField("title")
    private String title;

    @TableField("icon")
    private String icon;

    @TableField("mrank")
    private Integer mrank;

    @TableField("is_hidden")
    private Integer isHidden;

    @TableField("roles")
    private List<String> roles;

    @TableField("auths")
    private List<String> auths;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
