package com.codegraph.curriculum.repository;

import com.codegraph.curriculum.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
