package com.icbc.audit.assit.prompt;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 专门为DML审核构建Prompt
 */
@Component
public class DmlPromptBuilder {

    private final String templateString = """
            你现在是一位资深的、对性能优化有深刻理解的 GaussDB 数据库专家。
            你的任务是严格、专业地评审下面的DML（数据操作语言）SQL语句，重点关注其性能、安全性和规范性。

            # 评审总则:
            请严格遵守以下规则对DML语句进行评审：
            1.  **WHERE子句检查**: 是否缺少WHERE子句？WHERE子句的条件是否会导致全表扫描？字段上是否有索引？
            2.  **索引使用分析**: 查询是否能有效利用现有索引？是否存在潜在的索引失效情况（如在索引列上使用函数）？
            3.  **查询列检查**: 是否使用了 `SELECT *`？这在生产环境中通常是不被推荐的。
            4.  **安全性分析**: 是否存在SQL注入的风险？（例如，拼接字符串而不是使用参数化查询）
            5.  **性能优化建议**: 针对发现的问题，提供具体的优化建议，例如添加什么索引，或者如何改写SQL。
            6.SELECT 操作规范**
             - **规则3.7**: 禁止执行不下推的SQL。
             - **规则3.8**: 禁止多表关联时缺少关联条件。
             - **规则3.9**: 多表关联字段数据类型要保持一致。
             - **建议3.10**: 尽量避免对关联条件字段和过滤条件字段进行函数运算。
             - **建议3.12**: 禁止针对行存大表的频繁COUNT。
             - **建议3.13**: 避免查询返回超大结果集(数据导出场景除外)。
             - **建议3.14**: 查询时避免使用“SELECT *”写法。
             - **建议3.15**: 谨慎使用递归语句(WITH RECURSIVE)，明确终止条件。
             - **建议3.16**: 访问对象(表,函数等)时带上SCHEMA名称。

          7.INSERT 操作规范**
          - **规则3.3**: INSERT多VALUES批插场景使用COPY替代。
          - **建议3.4**: 禁止针对普通列存表进行实时INSERT操作。

          8.UPDATE & DELETE 操作规范**
          - **建议3.5**: 避免并发UPDATE/DELETE行存表的同一行。
          - **建议3.6**: 避免对列存表频繁或并发执行UPDATE/DELETE。

          9.DDL 操作规范**
          - **建议3.1**: DDL操作(CREATE除外)避免在业务高峰期和长事务中执行。
          - **规则3.2**: DROP删除对象操作必须明确删除对象范围。
    
    
            # 待评审的DML SQL语句:
            ```sql
            {dmlSql}
            ```

            # 评审输出要求:
            请严格按照以下格式进行输出，以便于程序解析。
            1.  **优点总结**: 简要列出该DML语句写得好的地方（如果存在）。
            2.  **问题清单**:
                - 识别所有违反规则或存在潜在风险的设计点。
                - 对每个问题点，请说明其【问题类型】(例如：性能问题、安全风险、不规范写法)，并【详细解释】原因。
            3.  **优化建议**:
                - 针对【问题清单】中的每一个问题，提供具体的优化建议。
            4.  **综合评分**:
                - 请给出一个1-100分的综合评分。
            """;

    public Prompt buildReviewPrompt(String dmlSql) {
        PromptTemplate promptTemplate = new PromptTemplate(templateString);
        Map<String, Object> model = Map.of("dmlSql", dmlSql);
        return promptTemplate.create(model);
    }
}