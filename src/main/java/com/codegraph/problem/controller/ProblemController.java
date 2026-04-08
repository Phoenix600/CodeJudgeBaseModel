package com.codegraph.problem.controller;

import com.codegraph.common.dto.ApiResponse;
import com.codegraph.problem.entity.Problem;
import com.codegraph.testcase.entity.TestCase;
import com.codegraph.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problems")
@Tag(name = "Problem Learner API", description = "Endpoints for discovering and viewing coding challenges")
public class ProblemController {

    private final AdminService adminService;

    public ProblemController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    @Operation(summary = "List all problems (Paginated)", description = "Retrieves a paginated list of all active coding challenges.")
    public ApiResponse<Page<Problem>> getAllProblems(Pageable pageable) {
        return ApiResponse.success(adminService.fetchAllProblems(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get problem details by ID", description = "Retrieves full details of a specific problem including its solution template.")
    public ApiResponse<Problem> getProblemById(@PathVariable Long id) {
        return ApiResponse.success(adminService.fetchProblemById(id));
    }

    @GetMapping("/{id}/testcases")
    @Operation(summary = "Get sample test cases", description = "Retrieves ONLY the visible sample test cases for a problem.")
    public ApiResponse<List<TestCase>> getSampleTestCases(@PathVariable Long id) {
        return ApiResponse.success(adminService.fetchSampleTestCases(id));
    }
}
