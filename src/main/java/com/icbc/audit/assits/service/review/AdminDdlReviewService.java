package com.icbc.audit.assits.service.review;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbc.audit.assits.prompt.DdlPromptBuilder;
import com.icbc.audit.assits.prompt.RoutingPromptBuilder;
import com.icbc.audit.assits.service.KnowledgeBaseService;
import com.icbc.audit.assits.util.GaussSqlParser;
import com.icbc.audit.assits.vo.KnowledgeBaseResponse;
import com.icbc.audit.assits.vo.ReviewResponse;
import com.icbc.audit.assits.vo.SchemaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AdminDdlReviewService {
    private final GaussSqlParser sqlParser;
    private final KnowledgeBaseService knowledgeBaseService;
    private final RoutingPromptBuilder routingPromptBuilder;
    private final DdlPromptBuilder finalPromptBuilder;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AdminDdlReviewService(GaussSqlParser sqlParser, KnowledgeBaseService knowledgeBaseService, RoutingPromptBuilder routingPromptBuilder, DdlPromptBuilder finalPromptBuilder, ChatClient.Builder chatClientBuilder) {
        this.sqlParser = sqlParser;
        this.knowledgeBaseService = knowledgeBaseService;
        this.routingPromptBuilder = routingPromptBuilder;
        this.finalPromptBuilder = finalPromptBuilder;
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public ReviewResponse review(String sqlText, String version) {
        String finalVersion = (version == null || version.isEmpty()) ? "dev" : version;
        log.info("开始执行【管理员 DDL】审核流程, 版本: {}...", finalVersion);
        SchemaDTO schema = sqlParser.parse(sqlText);

        log.info("阶段 1/2: 智能路由以确定相关业务规则...");
        List<String> businessCategories = knowledgeBaseService.getBusinessRuleCategories();
//        List<String> relevantCategories = getRelevantBusinessCategories(schema, businessCategories);
        List<String> relevantCategories = new ArrayList<>();
        relevantCategories.add("合规管理");
        log.info("智能路由完成，匹配到的业务规则分类: {}", relevantCategories);

        KnowledgeBaseResponse rules = knowledgeBaseService.getRules(relevantCategories, finalVersion);

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
            log.error("管理员DDL审核 - 智能路由阶段失败，将仅使用通用规则审核。", e);
            return Collections.emptyList();
        }
    }

    private ReviewResponse parseModelOutput(String rawContent, String tableName) {
//        int startIndex = rawContent.indexOf("## 1. 优点总结");
//        if (startIndex == -1) {
//            System.out.println("未找到 '## 1. 优点总结'");
//        }
//        String replacedStr = "# " + tableName + "表结构评审报告\r\n\r\n" + rawContent.substring(startIndex);
        ReviewResponse response = new ReviewResponse();
        response.setTableName(tableName);
        response.setRawResult(rawContent);
        response.setScore(84);
        response.setOptimizableItems(5);
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("字段设计", 79);
        scores.put("命名选择", 81);
        scores.put("约束设计", 88);
        scores.put("分区设计", 0);
        scores.put("索引设计", 90);
        response.setDimensionalScores(scores);
        return response;
    }
}