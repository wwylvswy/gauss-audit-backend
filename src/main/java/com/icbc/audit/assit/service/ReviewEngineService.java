package com.icbc.audit.assit.service;


import com.icbc.audit.assit.prompt.PromptBuilder;
import com.icbc.audit.assit.util.GaussSqlParser;
import com.icbc.audit.assit.vo.ReviewResponse;
import com.icbc.audit.assit.vo.SchemaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class ReviewEngineService {

    private final GaussSqlParser sqlParser; // 【重要】替换为新的解析器
    private final RuleService ruleService;
    private final PromptBuilder promptBuilder;
    private final ChatClient chatClient;

    public ReviewEngineService(GaussSqlParser sqlParser, // 【重要】更新构造函数
                               RuleService ruleService,
                               PromptBuilder promptBuilder,
                               ChatClient.Builder chatClientBuilder,
                               @Value("${spring.ai.openai.api-key}") String apiKey) {
        this.sqlParser = sqlParser;
        this.ruleService = ruleService;
        this.promptBuilder = promptBuilder;
        this.chatClient = chatClientBuilder.build();
        // ... API Key检查逻辑 ...
    }

    public ReviewResponse performReview(String sqlText) {
        log.info("DDL 服务层: 接收到评审任务...");
        try {
            log.info("DDL 服务层: 步骤 1/4 - 开始解析SQL为结构化对象...");
            // 【重要】调用新解析器
            SchemaDTO schema = sqlParser.parse(sqlText);
            log.info("DDL 服务层: SQL解析成功, 表名: {}", schema.getTableName());

            log.info("DDL 服务层: 步骤 2/4 - 加载评审规则...");
            String relevantRules = ruleService.loadActiveRulesAsString();

            log.info("DDL 服务层: 步骤 3/4 - 构建Prompt...");
            Prompt prompt = promptBuilder.buildReviewPrompt(schema, relevantRules); // 【重要】传递新对象
            log.debug("DDL 服务层: 即将发送给 AI 的完整 Prompt: \n{}", prompt.getContents());

            log.info("DDL 服务层: 步骤 4/4 - 开始调用大模型 API...");
            String rawContent = chatClient.prompt(prompt).call().content();

            if (rawContent == null || rawContent.isBlank()) {
                throw new RuntimeException("AI 服务返回了空的响应内容。");
            }
            log.info("DDL 服务层: 大模型调用成功。");

            return parseModelOutput(rawContent, schema.getTableName());

        } catch (Exception e) {
            log.error("在 DDL 评审过程中发生严重错误！", e);
            throw new RuntimeException("调用AI服务失败: " + e.getMessage(), e);
        }
    }

    private ReviewResponse parseModelOutput(String rawContent, String tableName) {
        ReviewResponse response = new ReviewResponse();
        response.setTableName(tableName);
        response.setRawResult(rawContent);
        // ... 模拟分数解析逻辑保持不变 ...
        response.setScore(92);
        response.setOptimizableItems(4);
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("命名规范", 95);
        scores.put("类型选择", 88);
        scores.put("索引健康度", 98);
        scores.put("约束设计", 85);
        scores.put("冗余设计", 90);
        response.setDimensionalScores(scores);
        return response;
    }
}