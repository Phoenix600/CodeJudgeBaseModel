package com.codegraph.submission.service;

import com.codegraph.common.enums.ProgrammingLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import com.codegraph.common.enums.SubmissionStatus;
import com.codegraph.judge.engine.JudgeEngine;
import com.codegraph.submission.dto.SubmitRequest;
import com.codegraph.submission.entity.Submission;
import com.codegraph.submission.repository.SubmissionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final JudgeEngine judgeEngine;

    protected SubmissionService() {
        this.submissionRepository = null;
        this.judgeEngine = null;
    }

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository,
                             JudgeEngine judgeEngine) {
        this.submissionRepository = submissionRepository;
        this.judgeEngine = judgeEngine;
    }

    public Submission submit(SubmitRequest request) {

        Submission s = new Submission();
        s.setProblemId(request.getProblemId());
        s.setSourceCode(request.getSourceCode());
        s.setLanguage(
                ProgrammingLanguage.valueOf(request.getLanguage().toUpperCase())
        );
        s.setStatus(SubmissionStatus.PENDING);

        s = submissionRepository.save(s);

        judgeAsync(s, request.getDriverCode(), request.getTestCases());

        return s;
    }

    public com.codegraph.submission.dto.RunResult run(com.codegraph.submission.dto.RunRequest request) {
        return judgeEngine.runSampleTestCases(request);
    }

    public Submission getSubmission(String id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    public org.springframework.data.domain.Page<Submission> getAllSubmissions(org.springframework.data.domain.Pageable pageable) {
        return submissionRepository.findAll(pageable);
    }

    public org.springframework.data.domain.Page<Submission> getSubmissionsByProblem(String problemId, org.springframework.data.domain.Pageable pageable) {
        return submissionRepository.findByProblemId(problemId, pageable);
    }

    public void deleteSubmission(String id) {
        if (submissionRepository.existsById(id)) {
            submissionRepository.deleteById(id);
        }
    }

    public java.util.List<String> getSolvedProblemIds() {
        return submissionRepository.findSolvedProblemIds();
    }

    @Async
    public void judgeAsync(Submission s, String driverCode, java.util.List<com.codegraph.submission.dto.TestCaseDto> testCases) {
        judgeEngine.judge(s, driverCode, testCases);
    }

    public String getLastSolvedProblemId() {
        return submissionRepository.findFirstByStatusOrderBySubmittedAtDesc(SubmissionStatus.ACCEPTED)
                .map(Submission::getProblemId)
                .orElse(null);
    }
}