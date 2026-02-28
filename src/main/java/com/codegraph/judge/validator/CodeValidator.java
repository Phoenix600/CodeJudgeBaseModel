package com.codegraph.judge.validator;

import org.springframework.stereotype.Component;

@Component
public class CodeValidator {

    public void validate(String code) {
        if (code.contains("System.exit") || code.contains("Runtime")) {
            throw new RuntimeException("Forbidden API");
        }
    }
}