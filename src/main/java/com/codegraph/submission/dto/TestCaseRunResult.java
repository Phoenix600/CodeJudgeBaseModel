package com.codegraph.submission.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestCaseRunResult {
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private boolean passed;
    private long executionTimeMs;
    private long memoryKb;
    private String error;
}
