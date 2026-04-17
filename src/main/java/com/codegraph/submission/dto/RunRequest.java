package com.codegraph.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object for quick code run against sample test cases")
public class RunRequest {
    @Schema(description = "ID of the problem", example = "69d9425669e4a1529b3f6af9")
    private String problemId;

    @Schema(description = "Programming language", example = "JAVA")
    private String language;

    @Schema(description = "The source code to run", example = "class Solution { ... }")
    private String sourceCode;

    @Schema(description = "Optional driver code to wrap the solution")
    private String driverCode;

    @Schema(description = "The list of test cases to run against")
    private java.util.List<TestCaseDto> testCases;
}
