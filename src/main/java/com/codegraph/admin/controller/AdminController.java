package com.codegraph.admin.controller;

import com.codegraph.common.dto.ApiResponse;
import com.codegraph.admin.dto.CreateProblemRequest;
import com.codegraph.admin.dto.TestCaseRequest;
import com.codegraph.admin.service.AdminService;
import com.codegraph.problem.entity.Problem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Problem Management (Admin)", description = "Endpoints for creating and configuring coding challenges")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/problems")
    @Operation(summary = "Create a new problem", description = "Stores core problem data (title, difficulty, solution template, driver code, etc.).")
    public ApiResponse<Problem> createProblem(@RequestBody CreateProblemRequest request) {
        return ApiResponse.success("Problem created successfully", adminService.createProblem(request));
    }

    @PutMapping("/problems/{id}")
    @Operation(summary = "Update problem details", description = "Updates title, description, driver code, solution template, or limits for an existing problem.")
    public ApiResponse<Problem> updateProblem(@PathVariable Long id, @RequestBody CreateProblemRequest request) {
        return ApiResponse.success("Problem updated successfully", adminService.updateProblem(id, request));
    }

    @PostMapping("/problems/{id}/testcases")
    @Operation(summary = "Add test cases to a problem", description = "Adds a list of input/output pairs to the specified problem for validation.")
    public ApiResponse<String> addTestCases(@PathVariable Long id,
                                @RequestBody List<TestCaseRequest> requests) {
        adminService.addTestCases(id, requests);
        return ApiResponse.success("Testcases added successfully", "Success");
    }

    @PostMapping(value = "/problems/{id}/images", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload problem images to Cloudinary", description = "Uploads static images to Cloudinary and returns the hosted URLs.")
    public ApiResponse<List<String>> uploadImages(
            @PathVariable Long id,
            @RequestBody(description = "Files to upload", content = @Content(mediaType = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart("files") MultipartFile[] files) throws Exception {
        return ApiResponse.success("Images uploaded successfully", adminService.uploadImages(id, files));
    }
}