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
@Schema(description = "Request object for adding a test case")
public class TestCaseRequest {

    public String id;

    @Schema(description = "The input to provide to the stdin", example = "5\\n1 2 3 4 5")
    public String input;

    @Schema(description = "The expected output from stdout", example = "[5, 4, 3, 2, 1]")
    public String expectedOutput;

    @Schema(description = "Whether this is a visible sample test case for the learner", example = "true")
    public Boolean sample = false;

    @Schema(description = "Explanation for the test case", example = "The sum of 1 and 2 is 3.")
    public String explanation;

    @Schema(description = "Base64 or URL of the image for this test case")
    public String image;

    @Schema(description = "Scale percentage for the image", example = "40")
    public Integer imageScale = 40;
}