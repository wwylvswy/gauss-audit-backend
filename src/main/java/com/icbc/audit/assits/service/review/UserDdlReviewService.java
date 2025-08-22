package com.icbc.audit.assits.service.review;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbc.audit.assits.vo.KnowledgeBaseResponse;
import com.icbc.audit.assits.prompt.DdlPromptBuilder;
import com.icbc.audit.assits.prompt.RoutingPromptBuilder;
import com.icbc.audit.assits.service.KnowledgeBaseService;
import com.icbc.audit.assits.util.GaussSqlParser;
import com.icbc.audit.assits.vo.ReviewResponse;
import com.icbc.audit.assits.vo.SchemaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserDdlReviewService {
    private final GaussSqlParser sqlParser;
    private final KnowledgeBaseService knowledgeBaseService;
    private final RoutingPromptBuilder routingPromptBuilder;
    private final DdlPromptBuilder finalPromptBuilder;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public UserDdlReviewService(GaussSqlParser sqlParser, KnowledgeBaseService knowledgeBaseService, RoutingPromptBuilder routingPromptBuilder, DdlPromptBuilder finalPromptBuilder, ChatClient.Builder chatClientBuilder) {
        this.sqlParser = sqlParser;
        this.knowledgeBaseService = knowledgeBaseService;
        this.routingPromptBuilder = routingPromptBuilder;
        this.finalPromptBuilder = finalPromptBuilder;
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public ReviewResponse review(String sqlText) {
        log.info("开始执行【用户 DDL】审核流程...");
        SchemaDTO schema = sqlParser.parse(sqlText);

        log.info("阶段 1/2: 智能路由以确定相关业务规则...");
        List<String> businessCategories = knowledgeBaseService.getBusinessRuleCategories();
        List<String> relevantCategories = getRelevantBusinessCategories(schema, businessCategories);
        log.info("智能路由完成，匹配到的业务规则分类: {}", relevantCategories);

        KnowledgeBaseResponse rules = knowledgeBaseService.getRules(relevantCategories, "latest");

        log.info("阶段 2/2: 执行最终审核...");
        Prompt finalPrompt = finalPromptBuilder.buildReviewPrompt(schema, rules.getData().getGeneralRules(), rules.getData().getBusinessRules());
        String rawContent = chatClient.prompt(finalPrompt).call().content();
        log.info("最终审核完成。");

        return parseModelOutput(rawContent, schema.getTableName());
    }

    private List<String> getRelevantBusinessCategories(SchemaDTO schema, List<String> categories) {
        if (categories.isEmpty()) return Collections.emptyList();
        try {
            Prompt routingPrompt = routingPromptBuilder.buildRoutingPrompt(schema, categories);
            String responseJson = chatClient.prompt(routingPrompt).call().content();
            List<String> result = objectMapper.readValue(responseJson, new TypeReference<>() {});
            result.retainAll(categories);
            return result;
        } catch (Exception e) {
            log.error("用户DDL审核 - 智能路由阶段失败，将仅使用通用规则审核。", e);
            return Collections.emptyList();
        }
    }

    private ReviewResponse parseModelOutput(String rawContent, String tableName) {
        ReviewResponse response = new ReviewResponse();
        response.setTableName(tableName);
        response.setRawResult(rawContent);
        // 此处为模拟解析AI返回的分数，未来可以替换为更复杂的解析逻辑
        response.setScore(95);
        response.setOptimizableItems(1);
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("命名规范", 98);
        scores.put("类型选择", 92);
        scores.put("索引健康度", 95);
        response.setDimensionalScores(scores);
        return response;
    }
}