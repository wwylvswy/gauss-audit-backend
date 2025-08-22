package com.icbc.audit.assits.prompt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbc.audit.assits.vo.SchemaDTO;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DdlPromptBuilder {

    // 【核心修改】模板经过全面重构，增加了对JSON输出的明确指令
//    private final String templateString = """
//
//            你现在是一位资深的、严谨的 GaussDB 数据库专家。
//            你的任务是严格、专业、深入地评审用户提供的 GaussDB 表结构设计。
//
//            # 评审总则:
//            请严格遵守以下【通用规范】和【相关业务规则】对表结构进行评审。
//            ---
//            # 通用规范:
//            {generalRules}
//            ---
//            # 相关业务规则:
//            {businessRules}
//            ---
//            # 待评审的表结构定义 (JSON格式):
//            ```json
//            {schemaJson}
//            ```
//            ---
//            # 评审输出要求:
//            请严格按照以下多部分格式进行输出。Markdown部分用于生成报告，JSON部分用于程序解析。
//
//            ## 1. 优点总结
//            - 简要列出该表设计的1-2个主要优点。如果无明显优点，请指出。
//
//            ## 2. 问题清单
//            - 识别所有违反规则或存在潜在性能风险的设计点。
//            - 对每个问题点，请说明其【问题类型】(例如：命名规范、类型选择、索引缺失)，并【详细解释】原因。
//            - 【重要】为每个问题点明确指定【严重等级】，只能是 "严重" 或 "警告" 中的一个。
//
//            ## 3. 优化建议
//            - 针对【问题清单】中的每一个问题，提供具体的、可直接执行的优化建议。
//
//            ## 4. 综合评分
//            - 请给出一个1-100分的综合评分，只返回数字。
//
//            ## 5. 不同设计类型问题严重程度数量统计
//            - 统计【问题清单】中不同等级问题的数量。
//            - 必须严格按照下面的JSON格式输出，不要添加任何额外说明。
//            ```json
//            {
//              "errorSeverity": {
//                "字段设计": {
//                    "严重": 1,
//                    "警告": 3
//                },
//                "命名设计": {
//                    "严重": 1,
//                    "警告": 3
//                },
//                "约束设计": {
//                    "严重": 1,
//                    "警告": 3
//                },
//                "分区设计": {
//                    "严重": 1,
//                    "警告": 3
//                },
//                "索引设计": {
//                    "严重": 1,
//                    "警告": 3
//                }
//              }
//            }
//            ```
//
//            ## 6. 结构维度评分
//            - 根据表结构设计，从以下五个维度进行评分，每个维度满分100。
//            - 必须严格按照下面的JSON格式输出，不要添加任何额外说明，没有涉及的维度类型设置0分。
//            ```json
//            {
//              "dimensionalScores": {
//                "字段设计": 80,
//                "命名设计": 79,
//                "约束设计": 88,
//                "分区设计": 91,
//                "索引设计": 79
//              }
//            }
//            ```
//            """;

    private final String templateString = """
            
            你现在是一位资深的、严谨的 GaussDB 数据库专家。
            你的任务是严格、专业、深入地评审用户提供的 GaussDB 表结构设计。

            # 评审总则:
            请严格遵守以下【通用规范】和【相关业务规则】对表结构进行评审。
            ---
            # 通用规范:
            {generalRules}
            ---
            # 相关业务规则:
            {businessRules}
            ---
            # 待评审的表结构定义 (JSON格式):
            ```json
            {schemaJson}
            ```
            ---
            # 评审输出要求:
            请严格按照以下多部分格式进行输出。Markdown部分用于生成报告，JSON部分用于程序解析。

            ## 1. 优点总结
            - 简要列出该表设计的1-2个主要优点。如果无明显优点，请指出。

            ## 2. 问题清单
            - 识别所有违反规则或存在潜在性能风险的设计点。
            - 对每个问题点，请说明其【问题类型】(例如：命名规范、类型选择、索引缺失)，并【详细解释】原因。
            - 【重要】为每个问题点明确指定【严重等级】，只能是 "严重" 或 "警告" 中的一个。

            ## 3. 优化建议
            - 针对【问题清单】中的每一个问题，提供具体的、可直接执行的优化建议。

            ## 4. 综合评分
            - 请给出一个1-100分的综合评分，只返回数字。

            ## 5. 错误严重程度
            - 统计【问题清单】中不同严重等级问题的数量。
            - 必须严格按照下面的JSON格式输出，不要添加任何额外说明。
            ```json
            {
              "errorSeverity": {
                "严重": 1,
                "警告": 2
              }
            }
            ```

            ## 6. 结构维度评分
            - 根据表结构设计，从以下五个维度进行评分，每个维度满分100。
            - 必须严格按照下面的JSON格式输出，不要添加任何额外说明。
            ```json
            {
              "dimensionalScores": {
                "命名规范": 95,
                "类型选择": 88,
                "索引健康度": 98,
                "约束设计": 85,
                "冗余设计": 90
              }
            }
            ```
            """;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Prompt buildReviewPrompt(SchemaDTO schema, String generalRules, String businessRules) {
        try {
            String schemaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);

            PromptTemplate promptTemplate = new PromptTemplate(templateString);
            Map<String, Object> model = Map.of(
                    "generalRules", generalRules,
                    "businessRules", businessRules,
                    "schemaJson", schemaJson
            );
            return promptTemplate.create(model);
        } catch (Exception e) {
            throw new RuntimeException("构建最终审核Prompt时转换JSON失败", e);
        }
    }
}