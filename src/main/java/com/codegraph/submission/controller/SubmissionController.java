package com.codegraph.submission.controller;

import com.codegraph.common.dto.ApiResponse;
import com.codegraph.submission.dto.SubmitRequest;
import com.codegraph.submission.service.SubmissionService;
import com.codegraph.submission.entity.Submission;
import com.codegraph.submission.dto.RunResult;
import com.codegraph.submission.dto.RunRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
@Tag(name = "Judge API", description = "Endpoints for running and submitting code solutions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    @Operation(summary = "Submit code (Final)", description = "Saves code locally and judges against ALL test cases in the background.")
    public ApiResponse<Submission> submit(@RequestBody SubmitRequest request) {
        return ApiResponse.success(submissionService.submit(request));
    }

    @PostMapping("/run")
    @Operation(summary = "Run code (Sample Only)", description = "Fast, synchronous execution against sample test cases only. No permanent record is saved.")
    public ApiResponse<RunResult> run(@RequestBody RunRequest request) {
        return ApiResponse.success(submissionService.run(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get submission status", description = "Retrieves the status, results, and execution metrics for a specific submission.")
    public ApiResponse<Submission> get(@PathVariable Long id) {
        return ApiResponse.success(submissionService.getSubmission(id));
    }

    @GetMapping
    @Operation(summary = "List all submissions (Paginated)", description = "Retrieves a paginated list of all submissions across all problems.")
    public ApiResponse<Page<Submission>> getAll(Pageable pageable) {
        return ApiResponse.success(submissionService.getAllSubmissions(pageable));
    }

    @GetMapping("/problem/{problemId}")
    @Operation(summary = "List submissions by Problem ID", description = "Retrieves a paginated list of submissions for a specific challenge.")
    public ApiResponse<Page<Submission>> getByProblem(@PathVariable Long problemId, Pageable pageable) {
        return ApiResponse.success(submissionService.getSubmissionsByProblem(problemId, pageable));
    }
}