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
    @Operation(summary = "Create a new problem", description = "Stores core problem data (title, difficulty, solution template, etc.).")
    public ApiResponse<Problem> createProblem(@RequestBody CreateProblemRequest request) {
        return ApiResponse.success("Problem created successfully", adminService.createProblem(request));
    }

    @PostMapping("/problems/{id}/testcases")
    public ApiResponse<String> addTestCases(@PathVariable Long id,
                                @RequestBody List<TestCaseRequest> requests) {
        adminService.addTestCases(id, requests);
        return ApiResponse.success("Testcases added successfully", "Success");
    }

    @PostMapping("/problems/{id}/images")
    public ApiResponse<String> uploadImages(@PathVariable Long id,
                                @RequestParam MultipartFile[] files) throws Exception {
        adminService.uploadImages(id, files);
        return ApiResponse.success("Images uploaded successfully", "Success");
    }
}