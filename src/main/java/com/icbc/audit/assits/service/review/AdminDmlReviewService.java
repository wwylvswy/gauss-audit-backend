package com.icbc.audit.assits.service.review;

import com.icbc.audit.assits.vo.KnowledgeBaseResponse;
import com.icbc.audit.assits.prompt.DmlPromptBuilder;
import com.icbc.audit.assits.service.KnowledgeBaseService;
import com.icbc.audit.assits.vo.ReviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class AdminDmlReviewService {
    private final DmlPromptBuilder dmlPromptBuilder;
    private final ChatClient chatClient;
    private final KnowledgeBaseService knowledgeBaseService;

    public AdminDmlReviewService(DmlPromptBuilder dmlPromptBuilder, ChatClient.Builder chatClientBuilder, KnowledgeBaseService knowledgeBaseService) {
        this.dmlPromptBuilder = dmlPromptBuilder;
        this.chatClient = chatClientBuilder.build();
        this.knowledgeBaseService = knowledgeBaseService;
    }

    public ReviewResponse review(String dmlSql, String version) {
        String finalVersion = (version == null || version.isEmpty()) ? "dev" : version;
        log.info("开始执行【管理员 DML】审核流程, 版本: {}...", finalVersion);
        KnowledgeBaseResponse rulesResponse = knowledgeBaseService.getDmlRules(null, finalVersion);

        log.info("构建DML Prompt并调用AI...");
        Prompt prompt = dmlPromptBuilder.buildReviewPrompt(dmlSql, rulesResponse.getData().getBusinessRules());
        String rawContent = chatClient.prompt(prompt).call().content();
        log.info("DML审核完成。");

        return parseDmlOutput(rawContent);
    }

    private ReviewResponse parseDmlOutput(String rawContent) {
        ReviewResponse response = new ReviewResponse();
        response.setTableName("DML Query (Admin)");
        response.setRawResult(rawContent);
        response.setScore(80);
        response.setOptimizableItems(4);
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("性能效率", 80);
        scores.put("安全规范", 80);
        response.setDimensionalScores(scores);
        return response;
    }
}