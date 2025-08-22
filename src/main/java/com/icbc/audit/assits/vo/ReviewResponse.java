package com.icbc.audit.assits.vo;

import lombok.Data;

import java.util.Map;

/**
 * 对应 /api/model/review 接口返回的 data 部分
 */
@Data
public class ReviewResponse {
    private String tableName;
    private int score;
    private int optimizableItems;
    private String rawResult; // 大模型返回的原始文本结果
    // 未来可以扩展为结构化数据
    // private List<String> advantages;
    // private List<Issue> issues;
    // 【关键修改】添加一个 Map 用于存放雷达图的维度分数
    private Map<String, Integer> dimensionalScores;
}