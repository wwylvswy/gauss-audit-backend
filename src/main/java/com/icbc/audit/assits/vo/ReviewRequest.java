package com.icbc.audit.assits.vo;

import lombok.Data;

/**
 * 对应 /api/model/review 接口的请求体
 */
@Data
public class ReviewRequest {
    private String sqlText;
    // 可选的版本号，主要供管理员使用
    private String version;
}