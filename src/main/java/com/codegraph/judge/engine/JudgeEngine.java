package com.codegraph.judge.engine;

import com.codegraph.common.enums.SubmissionStatus;
import com.codegraph.judge.compiler.CompilationException;
import com.codegraph.judge.validator.CodeValidator;
import com.codegraph.judge.workspace.WorkspaceManager;
import com.codegraph.judge.wrapper.JavaWrapper;
import com.codegraph.judge.compiler.JavaCompilerService;
import com.codegraph.judge.runner.JavaRunner;
import com.codegraph.judge.comparator.OutputComparator;
import com.codegraph.submission.entity.Submission;
import com.codegraph.submission.repository.SubmissionRepository;
import com.codegraph.testcase.repository.TestCaseRepository;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JudgeEngine {

    private final WorkspaceManager workspaceManager;
    private final CodeValidator validator;
    private final JavaWrapper wrapper;
    private final JavaCompilerService compiler;
    private final JavaRunner runner;
    private final OutputComparator comparator;
    private final SubmissionRepository submissionRepository;

    public JudgeEngine(WorkspaceManager workspaceManager,
                       CodeValidator validator,
                       JavaWrapper wrapper,
                       JavaCompilerService compiler,
                       JavaRunner runner,
                       OutputComparator comparator,
                       SubmissionRepository submissionRepository) {

        this.workspaceManager = workspaceManager;
        this.validator = validator;
        this.wrapper = wrapper;
        this.compiler = compiler;
        this.runner = runner;
        this.comparator = comparator;
        this.submissionRepository = submissionRepository;
    }

    public void judge(Submission submission, String driverCode, java.util.List<com.codegraph.submission.dto.TestCaseDto> testcases) {
        runFullJudge(submission, driverCode, testcases);
    }

    private void runFullJudge(Submission submission, String driverCode, java.util.List<com.codegraph.submission.dto.TestCaseDto> testcases) {
        File workspace = null;
        long totalTime = 0;
        long peakMemory = 0;

        try {
            validator.validate(submission.getSourceCode());
            workspace = workspaceManager.create(String.valueOf(submission.getId()));

            wrapper.writeMain(workspace, submission.getSourceCode(), driverCode);
            wrapper.writePolicy(workspace);
            compiler.compile(workspace);

            submission.setTotalTestCases(testcases.size());

            if (testcases.isEmpty()) {
                submission.setStatus(SubmissionStatus.RUNTIME_ERROR);
                submission.setRuntimeError("No testcases configured");
                submissionRepository.save(submission);
                return;
            }

            int passedCount = 0;
            for (var tc : testcases) {
                var result = runner.run(workspace, tc.getInput());
                totalTime += result.timeMs();
                peakMemory = Math.max(peakMemory, result.memoryKb());

                if (!comparator.compare(result.output(), tc.getExpectedOutput())) {
                    submission.setStatus(SubmissionStatus.WRONG_ANSWER);
                    submission.setExecutionTimeMs(totalTime);
                    submission.setMemoryKb(peakMemory);
                    submission.setPassedTestCases(passedCount);
                    
                    // Add diagnostic details
                    submission.setFailedInput(tc.getInput());
                    submission.setExpectedOutput(tc.getExpectedOutput());
                    submission.setActualOutput(result.output());
                    submission.setFailedTestCaseImage(tc.getImage());
                    submission.setFailedTestCaseExplanation(tc.getExplanation());
                    submission.setFailedTestCaseImageScale(tc.getImageScale());
                    
                    submissionRepository.save(submission);
                    return;
                }
                passedCount++;
            }

            submission.setPassedTestCases(passedCount);
            submission.setExecutionTimeMs(totalTime);
            submission.setMemoryKb(peakMemory);
            submission.setStatus(SubmissionStatus.ACCEPTED);
            submissionRepository.save(submission);

        } catch (CompilationException ce) {
            submission.setStatus(SubmissionStatus.COMPILATION_ERROR);
            submission.setCompileError(ce.getMessage());
            submissionRepository.save(submission);
        } catch (Exception e) {
            handleException(submission, e, totalTime, peakMemory);
        } finally {
            if (workspace != null) workspaceManager.cleanupBySubmission(String.valueOf(submission.getId()));
        }
    }

    public com.codegraph.submission.dto.RunResult runSampleTestCases(com.codegraph.submission.dto.RunRequest request) {
        File workspace = null;
        String workId = "run-" + java.util.UUID.randomUUID();
        var results = new java.util.ArrayList<com.codegraph.submission.dto.TestCaseRunResult>();
        var testcases = request.getTestCases();

        try {
            validator.validate(request.getSourceCode());
            workspace = workspaceManager.create(workId);

            wrapper.writeMain(workspace, request.getSourceCode(), request.getDriverCode());
            wrapper.writePolicy(workspace);
            compiler.compile(workspace);

            if (testcases == null || testcases.isEmpty()) {
                return com.codegraph.submission.dto.RunResult.builder()
                        .compiled(true)
                        .status("RUNTIME_ERROR")
                        .compileError("No test cases provided")
                        .build();
            }

            boolean allPassed = true;
            for (var tc : testcases) {
                var res = runner.run(workspace, tc.getInput());
                boolean passed = comparator.compare(res.output(), tc.getExpectedOutput());
                if (!passed) allPassed = false;

                results.add(com.codegraph.submission.dto.TestCaseRunResult.builder()
                        .input(tc.getInput())
                        .expectedOutput(tc.getExpectedOutput())
                        .actualOutput(res.output())
                        .passed(passed)
                        .executionTimeMs(res.timeMs())
                        .memoryKb(res.memoryKb())
                        .build());
            }

            var response = com.codegraph.submission.dto.RunResult.builder()
                    .compiled(true)
                    .results(results)
                    .status(allPassed ? "ACCEPTED" : "WRONG_ANSWER")
                    .build();
            return response;

        } catch (CompilationException ce) {
            return com.codegraph.submission.dto.RunResult.builder()
                    .compiled(false)
                    .compileError(ce.getMessage())
                    .status("COMPILATION_ERROR")
                    .build();
        } catch (Exception e) {
            return com.codegraph.submission.dto.RunResult.builder()
                    .compiled(true)
                    .status("RUNTIME_ERROR")
                    .compileError(e.getMessage())
                    .build();
        } finally {
            if (workspace != null) workspaceManager.cleanupBySubmission(workId);
        }
    }

    private void handleException(Submission submission, Exception e, long totalTime, long peakMemory) {
        submission.setExecutionTimeMs(totalTime);
        submission.setMemoryKb(peakMemory);
        if ("TIME_LIMIT_EXCEEDED".equals(e.getMessage())) {
            submission.setStatus(SubmissionStatus.TIME_LIMIT_EXCEEDED);
        } else {
            submission.setStatus(SubmissionStatus.RUNTIME_ERROR);
            submission.setRuntimeError(e.getMessage());
        }
        submissionRepository.save(submission);
    }
}