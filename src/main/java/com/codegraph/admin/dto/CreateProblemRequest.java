package com.codegraph.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Request object for creating or updating a coding problem")
public class CreateProblemRequest {

    @Schema(description = "Title of the problem", example = "Two Sum")
    public String title;

    @Schema(description = "Markdown description of the problem", example = "Given an array...")
    public String description;

    @Schema(description = "Difficulty level", example = "EASY")
    public String difficulty;

    @Schema(description = "Comma-separated tags", example = "array,hash-table")
    public String tags;

    @Schema(description = "Java driver code template containing {{SOLUTION}} placeholder", 
            example = "import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main { ... }")
    public String driverCode;

    @Schema(description = "The initial template shown to the user", 
            example = "class Solution { ... }")
    public String solutionTemplate;

    @Schema(description = "Execution time limit in milliseconds", example = "2000")
    public Integer timeLimitMs;

    @Schema(description = "Execution memory limit in Megabytes", example = "256")
    public Integer memoryLimitMb;
}