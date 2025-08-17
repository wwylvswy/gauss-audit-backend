package com.icbc.audit.user.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RouteVO {
    private Long parentId;
    private String path;
    private String component;
    private String name;
    private Map<String, Object> meta;
    private List<RouteVO> children;
}
