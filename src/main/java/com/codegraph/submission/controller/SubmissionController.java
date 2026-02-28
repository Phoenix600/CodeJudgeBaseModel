package com.codegraph.submission.controller;

import com.codegraph.submission.dto.SubmitRequest;
import com.codegraph.submission.service.SubmissionService;
import com.codegraph.submission.entity.Submission;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public Submission submit(@RequestBody SubmitRequest request) {
        return submissionService.submit(request);
    }

    @GetMapping("/{id}")
    public Submission get(@PathVariable Long id) {
        return submissionService.getSubmission(id);
    }
}