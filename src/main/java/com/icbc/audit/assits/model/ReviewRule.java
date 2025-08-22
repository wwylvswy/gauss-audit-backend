package com.icbc.audit.assits.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 对应文档 5.3.1 实体 "评审规则(Rule)"
 */
@Entity
@Table(name = "review_rule")
@Data
public class ReviewRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ruleId;

    @Column(nullable = false)
    private String ruleType; // e.g., "命名规范", "数据类型"

    @Column(nullable = false, length = 1024)
    private String ruleDescription;

    private boolean enabled = true; // For enabling/disabling rules
}