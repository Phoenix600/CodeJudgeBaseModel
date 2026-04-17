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
    private String title;

    @Schema(description = "Markdown description of the problem", example = "Given an array...")
    private String description;

    @Schema(description = "Difficulty level", example = "EASY")
    private String difficulty;

    @Schema(description = "List of tags", example = "[\"array\", \"hash-table\"]")
    private java.util.List<String> tags;

    @Schema(description = "Java driver code template containing {{SOLUTION}} placeholder", 
            example = "import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main { ... }")
    private String driverCode;

    @Schema(description = "The initial template shown to the user", 
            example = "class Solution { ... }")
    private String solutionTemplate;

    @Schema(description = "Execution time limit in milliseconds", example = "2000")
    private Integer timeLimitMs;

    @Schema(description = "Execution memory limit in Megabytes", example = "256")
    private Integer memoryLimitMb;

    @Schema(description = "Markdown footer for constraints and follow-up", example = "Constraints: 1 <= n <= 10^5")
    private String footer;

    @Schema(description = "ID of the chapter this problem belongs to", example = "69d9425669e4a1529b3f6af9")
    private String chapterId;

    private String editorialVideoUrl;
    private String editorialPdfUrl;
    private String quizQuestion;
    private String quizOptions;
    private String quizCorrectAnswer;

    @Schema(description = "Base64 or URL of the image for the problem description")
    private String image;

    @Schema(description = "Scale percentage for the problem image", example = "40")
    private Integer imageScale = 40;
}