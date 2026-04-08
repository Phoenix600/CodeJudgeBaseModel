package com.codegraph.submission.dto;

import lombok.Data;

@Data
public class RunRequest {
    private Long problemId;
    private String language;
    private String sourceCode;
}
