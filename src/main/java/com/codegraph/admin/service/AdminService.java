package com.codegraph.admin.service;

import com.codegraph.admin.dto.CreateProblemRequest;
import com.codegraph.admin.dto.TestCaseRequest;
import com.codegraph.problem.entity.Problem;
import com.codegraph.problem.repository.ProblemRepository;
import com.codegraph.testcase.repository.TestCaseRepository;
import com.codegraph.testcase.entity.TestCase;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class AdminService {

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;

    public AdminService(ProblemRepository problemRepository,
                        TestCaseRepository testCaseRepository) {
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
    }

    public Problem createProblem(CreateProblemRequest request) {
        Problem p = new Problem();
        p.setTitle(request.title);
        p.setDescription(request.description);
        p.setDifficulty(request.difficulty);
        p.setTags(request.tags);
        return problemRepository.save(p);
    }

    public void addTestCases(Long problemId, List<TestCaseRequest> requests) {
        Problem problem = problemRepository.findById(problemId).orElseThrow();

        for (TestCaseRequest r : requests) {
            TestCase tc = new TestCase();
            tc.setProblem(problem);
            tc.setInput(r.input);
            tc.setExpectedOutput(r.expectedOutput);
            tc.setSample(r.sample);
            testCaseRepository.save(tc);
        }
    }

    public void uploadImages(Long problemId, MultipartFile[] files) throws Exception {

        File dir = new File("uploads/problems/" + problemId);
        dir.mkdirs();

        for (MultipartFile file : files) {
            file.transferTo(new File(dir, file.getOriginalFilename()));
        }
    }

    public org.springframework.data.domain.Page<Problem> fetchAllProblems(org.springframework.data.domain.Pageable pageable) {
        return problemRepository.findAll(pageable);
    }

    public Problem fetchProblemById(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
    }

    public List<TestCase> fetchSampleTestCases(Long problemId) {
        return testCaseRepository.findByProblem_IdAndSampleTrue(problemId);
    }
}