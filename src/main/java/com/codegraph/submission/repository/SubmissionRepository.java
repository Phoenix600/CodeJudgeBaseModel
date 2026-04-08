package com.codegraph.submission.repository;

import com.codegraph.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    org.springframework.data.domain.Page<Submission> findByProblemId(Long problemId, org.springframework.data.domain.Pageable pageable);
}