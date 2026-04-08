package com.codegraph.submission.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RunResult {
    private boolean compiled;
    private String compileError;
    private List<TestCaseRunResult> results;
    private String status; // ACCEPTED, WRONG_ANSWER, etc.
}
