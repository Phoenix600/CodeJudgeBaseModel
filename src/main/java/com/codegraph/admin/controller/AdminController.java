package com.codegraph.admin.controller;

import com.codegraph.admin.dto.CreateProblemRequest;
import com.codegraph.admin.dto.TestCaseRequest;
import com.codegraph.admin.service.AdminService;
import com.codegraph.problem.entity.Problem;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/problems")
    public Problem createProblem(@RequestBody CreateProblemRequest request) {
        return adminService.createProblem(request);
    }

    @PostMapping("/problems/{id}/testcases")
    public String addTestCases(@PathVariable Long id,
                               @RequestBody List<TestCaseRequest> requests) {
        adminService.addTestCases(id, requests);
        return "Testcases added";
    }

    @PostMapping("/problems/{id}/images")
    public String uploadImages(@PathVariable Long id,
                               @RequestParam MultipartFile[] files) throws Exception {
        adminService.uploadImages(id, files);
        return "Images uploaded";
    }
}