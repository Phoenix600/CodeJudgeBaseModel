package com.codegraph.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request object for final code submission")
public class SubmitRequest {
    @Schema(description = "ID of the problem being solved", example = "1")
    private Long problemId;

    @Schema(description = "The source code to be judged", example = "class Solution { ... }")
    private String sourceCode;

    @Schema(description = "Programming language", example = "JAVA")
    private String language = "JAVA";
}