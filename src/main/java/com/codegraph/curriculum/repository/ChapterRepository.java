package com.codegraph.curriculum.repository;

import com.codegraph.curriculum.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, String> {
    List<Chapter> findByCourseIdOrderByOrderIndexAsc(String courseId);
    List<Chapter> findByCourseIdAndParentChapterIsNullOrderByOrderIndexAsc(String courseId);
}
