package com.icbc.audit.assits.prompt;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class DmlPromptBuilder {

    // 【核心修改】模板经过全面重构，增加了对JSON输出的明确指令
    private final String templateString = """
            你现在是一位资深的、对性能优化有深刻理解的 GaussDB 数据库专家。
            你的任务是严格、专业地评审下面的DML语句。

            # 评审总则:
            请严格遵守以下【基础规则】进行评审。
            {rules}

            # 待评审的DML SQL语句:
            ```sql
            {dmlSql}
            ```
            ---
            # 评审输出要求:
            请严格按照以下多部分格式进行输出。Markdown部分用于生成报告，JSON部分用于程序解析。

            ## 1. 优点总结
            - 简要列出该DML语句写得好的地方（如果存在）。

            ## 2. 问题清单
            - 识别所有违反规则或存在潜在风险的设计点。
            - 对每个问题点，请说明其【问题类型】(例如：性能问题、安全风险)，并【详细解释】原因。
            - 【重要】为每个问题点明确指定【严重等级】，只能是 "严重" 或 "警告" 中的一个。

            ## 3. 优化建议
            - 针对【问题清单】中的每一个问题，提供具体的优化建议。

            ## 4. 综合评分
            - 请给出一个1-100分的综合评分，只返回数字。

            ## 5. 错误严重程度
            - 统计【问题清单】中不同严重等级问题的数量。
            - 必须严格按照下面的JSON格式输出，不要添加任何额外说明。
            ```json
            {
              "errorSeverity": {
                "严重": 0,
                "警告": 1
              }
            }
            ```

            ## 6. 性能维度评分
            - 根据DML语句，从以下五个维度进行评分，每个维度满分100。
            - 必须严格按照下面的JSON格式输出，不要添加任何额外说明。
            ```json
            {
              "dimensionalScores": {
                "性能效率": 80,
                "安全规范": 90,
                "索引使用": 75,
                "代码质量": 95,
                "资源消耗": 85
              }
            }
            ```
            """;

    public Prompt buildReviewPrompt(String dmlSql, String rules) {
        Map<String, Object> model = Map.of(
                "dmlSql", dmlSql,
                "rules", rules.isEmpty() ? "无特定规则" : rules
        );
        return new PromptTemplate(templateString).create(model);
    }
}