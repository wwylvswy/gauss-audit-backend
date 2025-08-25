package com.icbc.audit.assits.service.review;

import com.icbc.audit.assits.dto.GetRulesRequestDTO;
import com.icbc.audit.assits.service.KnowledgeBaseService;
import com.icbc.audit.assits.vo.KnowledgeBaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    // 从配置文件读取远程后端服务地址
//    @Value("${knowledge.base.remote-url}")
//    private String knowledgeBaseUrl;
    private String knowledgeBaseUrl = "http://192.168.135.150:8080";

//    @Value("${knowledge.get-ddl-rules}")
//    private String knowledgeGetDdl;
    private String knowledgeGetDdl = "/v2/openApi/getRules/DDL";

    private String knowledgeGetDml = "/v2/openApi/getRules/DML";

    private final WebClient webClient = WebClient.builder()
            .baseUrl(knowledgeBaseUrl)
            .build();


    @Override
    public List<String> getBusinessRuleCategories() {
        // TODO 实际应从知识库管理远程接口获取
        List<String> ans = new ArrayList<>();
        ans.add("合规管理");
        ans.add("基础规则");
        ans.add("风险管理");
        ans.add("产品定价");
        ans.add("客户服务");
        return ans;
    }

    @Override
    public KnowledgeBaseResponse getRules(List<String> categories, String version) {
        GetRulesRequestDTO requestDTO = new GetRulesRequestDTO(categories, version);

        return webClient.post()
                .uri(knowledgeGetDdl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchangeToMono(response -> {
                    // 统一处理成功和失败情况
                    if (response.statusCode().isError()) {
                        return response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "HTTP " + response.statusCode() + ", body: " + body)));
                    }
                    return response.bodyToMono(KnowledgeBaseResponse.class);
                })
                .block();
    }

    public KnowledgeBaseResponse getDmlRules(List<String> categories, String version) {
        GetRulesRequestDTO requestDTO = new GetRulesRequestDTO(categories, version);

        return webClient.post()
                .uri(knowledgeGetDml)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchangeToMono(response -> {
                    // 统一处理成功和失败情况
                    if (response.statusCode().isError()) {
                        return response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "HTTP " + response.statusCode() + ", body: " + body)));
                    }
                    return response.bodyToMono(KnowledgeBaseResponse.class);
                })
                .block();
    }
}
