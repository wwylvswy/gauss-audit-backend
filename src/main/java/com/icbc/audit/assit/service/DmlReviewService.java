package com.icbc.audit.assit.service;

import com.icbc.audit.assit.prompt.DmlPromptBuilder;
import com.icbc.audit.assit.vo.ReviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class DmlReviewService {

    private final DmlPromptBuilder dmlPromptBuilder;
    private final ChatClient chatClient;

    public DmlReviewService(DmlPromptBuilder dmlPromptBuilder, ChatClient.Builder chatClientBuilder) {
        this.dmlPromptBuilder = dmlPromptBuilder;
        this.chatClient = chatClientBuilder.build();
    }

    public ReviewResponse performDmlReview(String dmlSql) {
        log.info("开始进行 DML 审核...");

        log.info("构建 DML Prompt...");
        Prompt prompt = dmlPromptBuilder.buildReviewPrompt(dmlSql);

        log.info("调用大模型进行 DML 评审...");
        String rawContent = chatClient.prompt(prompt).call().content();
        log.info("DML 评审完成。");

        return parseDmlOutput(rawContent);
    }

    private ReviewResponse parseDmlOutput(String rawContent) {
        ReviewResponse response = new ReviewResponse();
        response.setTableName("DML Query"); // 对于DML，我们可以没有具体的表名
        response.setRawResult(rawContent);

        // 模拟解析AI返回的分数
        response.setScore(85);
        response.setOptimizableItems(2);

        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("性能效率", 80);
        scores.put("安全规范", 90);
        scores.put("索引使用", 75);
        scores.put("代码质量", 95);
        scores.put("资源消耗", 85);
        response.setDimensionalScores(scores);

        return response;
    }
}