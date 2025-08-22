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
public class UserDmlReviewService {
    private final DmlPromptBuilder dmlPromptBuilder;
    private final ChatClient chatClient;
    private final KnowledgeBaseService knowledgeBaseService;

    public UserDmlReviewService(DmlPromptBuilder dmlPromptBuilder, ChatClient.Builder chatClientBuilder, KnowledgeBaseService knowledgeBaseService) {
        this.dmlPromptBuilder = dmlPromptBuilder;
        this.chatClient = chatClientBuilder.build();
        this.knowledgeBaseService = knowledgeBaseService;
    }

    public ReviewResponse review(String dmlSql) {
        log.info("开始执行【用户 DML】审核流程...");
        KnowledgeBaseResponse rulesResponse = knowledgeBaseService.getRules(null, "latest");

        log.info("构建DML Prompt并调用AI...");
        Prompt prompt = dmlPromptBuilder.buildReviewPrompt(dmlSql, rulesResponse.getData().getBusinessRules());
        String rawContent = chatClient.prompt(prompt).call().content();
        log.info("DML审核完成。");

        return parseDmlOutput(rawContent);
    }

    private ReviewResponse parseDmlOutput(String rawContent) {
        ReviewResponse response = new ReviewResponse();
        response.setTableName("DML Query (User)");
        response.setRawResult(rawContent);
        response.setScore(90);
        response.setOptimizableItems(2);
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("性能效率", 95);
        scores.put("安全规范", 85);
        response.setDimensionalScores(scores);
        return response;
    }
}