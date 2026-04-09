package com.codegraph.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object for quick code run against sample test cases")
public class RunRequest {
    @Schema(description = "ID of the problem", example = "1")
    private Long problemId;

    @Schema(description = "Programming language", example = "JAVA")
    private String language;

    @Schema(description = "The source code to run", example = "class Solution { ... }")
    private String sourceCode;
}
