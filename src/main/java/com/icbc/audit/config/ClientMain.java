package com.icbc.audit.config;

import com.icbc.audit.assits.vo.KnowledgeBaseResponse;

import java.util.ArrayList;
import java.util.List;

public class ClientMain {
    public static void main(String[] args) {
        WebClientTest client = new WebClientTest();
        List<String> categories = new ArrayList<>();
        categories.add("合规管理");
        String version = "用户DDL";

        String rawResponse = client.getRulesRaw(categories, version);
        client.getRulesWithHeaders(categories, version);
        System.out.println("Raw Response: " + rawResponse);

        KnowledgeBaseResponse response = client.getRules(categories, version);
        System.out.println("Parsed Response: " + response.toString());
    }
}
