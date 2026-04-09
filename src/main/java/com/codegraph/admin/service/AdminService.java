package com.codegraph.admin.service;

import com.codegraph.admin.dto.CreateProblemRequest;
import com.codegraph.admin.dto.TestCaseRequest;
import com.codegraph.problem.entity.Problem;
import com.codegraph.problem.repository.ProblemRepository;
import com.codegraph.testcase.repository.TestCaseRepository;
import com.codegraph.testcase.entity.TestCase;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.codegraph.common.service.CloudinaryService;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final CloudinaryService cloudinaryService;

    public AdminService(ProblemRepository problemRepository,
                        TestCaseRepository testCaseRepository,
                        CloudinaryService cloudinaryService) {
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Problem createProblem(CreateProblemRequest request) {
        Problem p = new Problem();
        p.setTitle(request.title);
        p.setDescription(request.description);
        p.setDifficulty(request.difficulty);
        p.setTags(request.tags);
        p.setDriverCode(request.driverCode);
        p.setSolutionTemplate(request.solutionTemplate);
        if (request.timeLimitMs != null) p.setTimeLimitMs(request.timeLimitMs);
        if (request.memoryLimitMb != null) p.setMemoryLimitMb(request.memoryLimitMb);
        return problemRepository.save(p);
    }

    public Problem updateProblem(Long id, CreateProblemRequest request) {
        Problem p = problemRepository.findById(id).orElseThrow();
        if (request.title != null) p.setTitle(request.title);
        if (request.description != null) p.setDescription(request.description);
        if (request.difficulty != null) p.setDifficulty(request.difficulty);
        if (request.tags != null) p.setTags(request.tags);
        if (request.driverCode != null) p.setDriverCode(request.driverCode);
        if (request.solutionTemplate != null) p.setSolutionTemplate(request.solutionTemplate);
        if (request.timeLimitMs != null) p.setTimeLimitMs(request.timeLimitMs);
        if (request.memoryLimitMb != null) p.setMemoryLimitMb(request.memoryLimitMb);
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

    public List<String> uploadImages(Long problemId, MultipartFile[] files) throws Exception {
        List<String> urls = new ArrayList<>();
        String folder = "codegraph/problems/" + problemId;

        for (MultipartFile file : files) {
            String url = cloudinaryService.uploadImage(file, folder);
            urls.add(url);
        }
        return urls;
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