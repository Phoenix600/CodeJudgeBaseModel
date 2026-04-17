package com.codegraph.curriculum.controller;

import com.codegraph.common.dto.ApiResponse;
import com.codegraph.curriculum.entity.Chapter;
import com.codegraph.curriculum.entity.Course;
import com.codegraph.curriculum.repository.ChapterRepository;
import com.codegraph.curriculum.repository.CourseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curriculum")
@Tag(name = "Curriculum", description = "Course and Chapter management")
public class CurriculumController {

    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;

    @Autowired
    public CurriculumController(CourseRepository courseRepository, ChapterRepository chapterRepository) {
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
    }

    @GetMapping("/courses")
    @Operation(summary = "Get all courses")
    public ApiResponse<List<Course>> getAllCourses() {
        return ApiResponse.success(courseRepository.findAll());
    }

    @GetMapping("/courses/{courseId}/chapters")
    @Operation(summary = "Get top-level chapters for a course")
    public ApiResponse<List<Chapter>> getChaptersByCourse(@PathVariable String courseId) {
        return ApiResponse.success(chapterRepository.findByCourseIdAndParentChapterIsNullOrderByOrderIndexAsc(courseId));
    }
}
