package com.codegraph.submission.service;

import com.codegraph.common.enums.ProgrammingLanguage;
import com.codegraph.common.enums.SubmissionStatus;
import com.codegraph.judge.engine.JudgeEngine;
import com.codegraph.submission.dto.SubmitRequest;
import com.codegraph.submission.entity.Submission;
import com.codegraph.submission.repository.SubmissionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final JudgeEngine judgeEngine;

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

//        judgeEngine.judge(s);
        judgeAsync(s);

        return s;
    }

    public Submission getSubmission(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    @Async
    public void judgeAsync(Submission s) {
        judgeEngine.judge(s);
    }
}