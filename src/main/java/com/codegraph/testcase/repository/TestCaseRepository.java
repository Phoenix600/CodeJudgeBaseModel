package com.codegraph.testcase.repository;

import com.codegraph.testcase.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, String> {

    List<TestCase> findByProblem_Id(String problemId);

    List<TestCase> findByProblem_IdAndSampleTrue(String problemId);
}