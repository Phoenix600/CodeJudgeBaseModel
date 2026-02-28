package com.codegraph.submission.dto;

import lombok.Data;

@Data
public class SubmitRequest {
    private Long problemId;
    private String sourceCode;
    private String language = "JAVA";
}