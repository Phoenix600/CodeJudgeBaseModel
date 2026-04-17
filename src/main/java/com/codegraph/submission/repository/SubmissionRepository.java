package com.codegraph.submission.repository;

import com.codegraph.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {

    org.springframework.data.domain.Page<Submission> findByProblemId(String problemId, org.springframework.data.domain.Pageable pageable);

    void deleteByProblemId(String problemId);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT s.problemId FROM Submission s WHERE s.status = 'ACCEPTED'")
    java.util.List<String> findSolvedProblemIds();

    java.util.Optional<Submission> findFirstByStatusOrderBySubmittedAtDesc(com.codegraph.common.enums.SubmissionStatus status);
}