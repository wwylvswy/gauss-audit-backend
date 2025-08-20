package com.icbc.audit.assit.prompt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbc.audit.assit.vo.SchemaDTO;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PromptBuilder {

    private final String templateString = """
            你现在是一位资深的、严谨的 GaussDB 数据库专家。
            你的任务是严格、专业、深入地评审用户提供的 GaussDB 表结构设计。

            # 评审总则:
            请严格遵守以下规则对表结构进行评审，并结合你自己的数据库最佳实践知识进行补充。
            {rules}

            # 待评审的表结构定义 (JSON格式):
            ```json
            {schemaJson}
            ```

            # 评审输出要求:
            请严格按照以下格式进行输出，以便于程序解析, 必须使用markdown格式返回数据。
            1.  **优点总结**: 简要列出该表设计的1-2个主要优点。如果无明显优点，请指出。
            2.  **问题清单**:
                - 识别所有违反规则或存在潜在性能风险、维护性问题的设计点。
                - 对每个问题点，请说明其【问题类型】(例如：命名规范、类型选择、索引缺失、冗余设计)，并【详细解释】为什么这是一个问题。
            3.  **优化建议**:
                - 针对【问题清单】中的每一个问题，提供具体的、可直接执行的优化建议。
            4.  **综合评分**:
                - 请给出一个1-100分的综合评分。
            """;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Prompt buildReviewPrompt(SchemaDTO schema, String rules) {
        try {
            String schemaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);

            PromptTemplate promptTemplate = new PromptTemplate(templateString);
            Map<String, Object> model = Map.of(
                    "rules", rules.isEmpty() ? "无特定规则，请使用通用数据库最佳实践进行评审。" : rules,
                    "schemaJson", schemaJson
            );
            return promptTemplate.create(model);
        } catch (Exception e) {
            throw new RuntimeException("将表结构对象转换为JSON时失败", e);
        }
    }
}