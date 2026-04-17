package com.codegraph.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object for final code submission")
public class SubmitRequest {
    @Schema(description = "ID of the problem being solved", example = "69d9425669e4a1529b3f6af9")
    private String problemId;

    @Schema(description = "The source code to be judged", example = "class Solution { ... }")
    private String sourceCode;

    @Schema(description = "Optional driver code to wrap the solution")
    private String driverCode;

    @Schema(description = "Programming language", example = "JAVA")
    private String language = "JAVA";

    @Schema(description = "The list of all test cases to run against")
    private java.util.List<TestCaseDto> testCases;
}