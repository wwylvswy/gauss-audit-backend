package com.icbc.audit.assits.prompt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbc.audit.assits.vo.SchemaDTO;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RoutingPromptBuilder {
    private final String templateString = """
            你是一个SQL分类专家。你的任务是根据SQL语句的结构化信息，从给定的分类列表中选择最相关的分类。
            你必须严格按照JSON数组的格式返回结果，例如 ["分类A", "分类B"]。如果都不相关，请返回空数组 []。
            不要添加任何额外的解释、注释或开场白。
            ---
            # 待分析的SQL结构 (JSON):
            ```json
            {schemaJson}
            ```
            # 可选的业务规则分类列表:
            {businessCategories}
            # 输出 (必须是JSON数组格式):
            """;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Prompt buildRoutingPrompt(SchemaDTO schema, List<String> categories) {
        try {
            String schemaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
            String categoriesString = categories.toString();
            Map<String, Object> model = Map.of(
                    "schemaJson", schemaJson,
                    "businessCategories", categoriesString
            );
            return new PromptTemplate(templateString).create(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("构建路由Prompt时转换JSON失败", e);
        }
    }
}