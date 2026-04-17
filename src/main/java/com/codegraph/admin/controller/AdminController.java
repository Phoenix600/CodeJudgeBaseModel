package com.codegraph.admin.controller;

import com.codegraph.common.dto.ApiResponse;
import com.codegraph.curriculum.entity.Course;
import com.codegraph.curriculum.entity.Chapter;
import com.codegraph.problem.entity.Problem;
import com.codegraph.admin.service.AdminService;
import com.codegraph.admin.dto.CreateCourseRequest;
import com.codegraph.admin.dto.CreateChapterRequest;
import com.codegraph.admin.dto.CreateProblemRequest;
import com.codegraph.admin.dto.TestCaseRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin API", description = "Endpoints for managing curriculum and problems")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/courses")
    @Operation(summary = "Create a new course")
    public ApiResponse<Course> createCourse(@RequestBody CreateCourseRequest request) {
        return ApiResponse.success(adminService.createCourse(request));
    }

    @PostMapping("/courses/{courseId}/chapters")
    @Operation(summary = "Create a new chapter in a course")
    public ApiResponse<Chapter> createChapter(@PathVariable String courseId, @RequestBody CreateChapterRequest request) {
        return ApiResponse.success(adminService.createChapter(courseId, request));
    }

    @PostMapping("/problems")
    @Operation(summary = "Create a new problem")
    public ApiResponse<Problem> createProblem(@RequestBody CreateProblemRequest request) {
        return ApiResponse.success(adminService.createProblem(request));
    }

    @PutMapping("/problems/{id}")
    @Operation(summary = "Update an existing problem")
    public ApiResponse<Problem> updateProblem(@PathVariable String id, @RequestBody CreateProblemRequest request) {
        return ApiResponse.success(adminService.updateProblem(id, request));
    }

    @PostMapping("/problems/{problemId}/testcases")
    @Operation(summary = "Add test cases to a problem")
    public ApiResponse<String> addTestCases(@PathVariable String problemId, @RequestBody List<TestCaseRequest> requests) {
        adminService.addTestCases(problemId, requests);
        return ApiResponse.success("Test cases added successfully");
    }

    @GetMapping("/problems/{problemId}/testcases/all")
    @Operation(summary = "Get all test cases for a problem")
    public ApiResponse<List<com.codegraph.testcase.entity.TestCase>> getAllTestCases(@PathVariable String problemId) {
        return ApiResponse.success(adminService.fetchAllTestCases(problemId));
    }

    @PostMapping("/problems/{problemId}/images")
    @Operation(summary = "Upload images for a problem description")
    public ApiResponse<List<String>> uploadImages(@PathVariable String problemId, @RequestParam("files") MultipartFile[] files) throws Exception {
        return ApiResponse.success(adminService.uploadImages(problemId, files));
    }

    @PostMapping("/upload")
    @Operation(summary = "Generic image upload to Cloudinary")
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        return ApiResponse.success(adminService.uploadImage(file));
    }

    @DeleteMapping("/problems/{id}")
    @Operation(summary = "Delete an existing problem")
    public ApiResponse<Void> deleteProblem(@PathVariable String id) {
        adminService.deleteProblem(id);
        return ApiResponse.success(null);
    }
}