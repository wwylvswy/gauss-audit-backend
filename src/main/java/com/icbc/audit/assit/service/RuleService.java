package com.icbc.audit.assit.service;

import com.icbc.audit.assit.model.ReviewRule;
import com.icbc.audit.assit.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public String loadActiveRulesAsString() {
        Iterable<ReviewRule> rules = ruleRepository.findAll();
        return StreamSupport.stream(rules.spliterator(), false)
                .map(rule -> "- (" + rule.getRuleType() + ") " + rule.getRuleDescription())
                .collect(Collectors.joining("\n"));
    }
}