package com.icbc.audit.assit.vo;

import lombok.Data;

/**
 * 对应 /api/model/review 接口的请求体
 */
@Data
public class ReviewRequest {
    private String sqlText;
    // 未来可以扩展，如指定要使用的模型类型、特定的规则集ID等
    // private String modelType;
}