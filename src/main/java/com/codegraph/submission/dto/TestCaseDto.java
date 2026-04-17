package com.codegraph.submission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for a test case used during code execution")
public class TestCaseDto {

    @Schema(description = "The unique identifier for the test case")
    private String id;

    @Schema(description = "The input to provide to the stdin", example = "5\n1 2 3 4 5")
    private String input;

    @Schema(description = "The expected output from stdout", example = "[5, 4, 3, 2, 1]")
    private String expectedOutput;

    @Schema(description = "Whether this is a visible sample test case for the learner", example = "true")
    private Boolean sample = false;

    @Schema(description = "Explanation for the test case", example = "The sum of 1 and 2 is 3.")
    private String explanation;

    @Schema(description = "Base64 or URL of the image for this test case")
    private String image;

    @Schema(description = "Scale percentage for the image", example = "40")
    private Integer imageScale = 40;
}
