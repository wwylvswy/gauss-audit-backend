package com.icbc.audit.config;

import com.icbc.audit.assits.dto.GetRulesRequestDTO;
import com.icbc.audit.assits.vo.KnowledgeBaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class WebClientTest {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://192.168.135.150:8080")
            .build();
    public String getRulesRaw(List<String> categories, String version) {
        GetRulesRequestDTO requestDTO = new GetRulesRequestDTO(categories, version);

        return webClient.post()
                .uri("/v2/openApi/getRules/DDL")
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 获取原始字符串
    }

    public void getRulesWithHeaders(List<String> categories, String version) {
        GetRulesRequestDTO requestDTO = new GetRulesRequestDTO(categories, version);

        webClient.post()
                .uri("/v2/openApi/getRules/DDL")
                .bodyValue(requestDTO)
                .exchangeToMono(response -> {
                    // 打印状态码和 headers
                    System.out.println("Status Code: " + response.statusCode());
                    System.out.println("Content-Type: " + response.headers().header("Content-Type"));

                    // 读取 body
                    return response.bodyToMono(String.class).map(body -> {
                        System.out.println("Response Body: " + body);
                        return body;
                    });
                })
                .block();
    }
    public KnowledgeBaseResponse getRules(List<String> categories, String version) {
        GetRulesRequestDTO requestDTO = new GetRulesRequestDTO(categories, version);

        return webClient.post()
                .uri("/v2/openApi/getRules/DDL")
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
