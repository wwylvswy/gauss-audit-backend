package com.icbc.audit.assits.service;

import com.icbc.audit.assits.vo.KnowledgeBaseResponse;

import java.util.List;

/**
 * 知识库服务接口。
 * 这是审核模块与知识库模块之间的契约。
 */
public interface KnowledgeBaseService {
    List<String> getBusinessRuleCategories();
    KnowledgeBaseResponse getRules(List<String> categories, String version);
    KnowledgeBaseResponse getDmlRules(List<String> categories, String version);
}