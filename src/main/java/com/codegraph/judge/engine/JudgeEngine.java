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
import org.springframework.scheduling.annotation.Async;
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
    private final TestCaseRepository testCaseRepository;

    public JudgeEngine(WorkspaceManager workspaceManager,
                       CodeValidator validator,
                       JavaWrapper wrapper,
                       JavaCompilerService compiler,
                       JavaRunner runner,
                       OutputComparator comparator,
                       SubmissionRepository submissionRepository,
                       TestCaseRepository testCaseRepository) {

        this.workspaceManager = workspaceManager;
        this.validator = validator;
        this.wrapper = wrapper;
        this.compiler = compiler;
        this.runner = runner;
        this.comparator = comparator;
        this.submissionRepository = submissionRepository;
        this.testCaseRepository = testCaseRepository;
    }

    public void judge(Submission submission) {

        File workspace = null;

        long totalTime = 0;
        long peakMemory = 0;

        try {

            validator.validate(submission.getSourceCode());

            workspace = workspaceManager.create(submission.getId());

            wrapper.writeMain(workspace, submission.getSourceCode());
            wrapper.writePolicy(workspace);

            compiler.compile(workspace);

            var testcases =
                    testCaseRepository.findByProblem_Id(submission.getProblemId());

            if (testcases.isEmpty()) {
                submission.setStatus(SubmissionStatus.RUNTIME_ERROR);
                submission.setRuntimeError("No testcases configured");
                submissionRepository.save(submission);
                return;
            }

            for (var tc : testcases) {

                var result = runner.run(workspace, tc.getInput());

                totalTime += result.timeMs();
                peakMemory = Math.max(peakMemory, result.memoryKb());

                String output = result.output();

                if (!comparator.compare(output, tc.getExpectedOutput())) {

                    submission.setStatus(SubmissionStatus.WRONG_ANSWER);
                    submission.setExecutionTimeMs(totalTime);
                    submission.setMemoryKb(peakMemory);
                    submissionRepository.save(submission);
                    return;
                }
            }

            submission.setExecutionTimeMs(totalTime);
            submission.setMemoryKb(peakMemory);
            submission.setStatus(SubmissionStatus.ACCEPTED);
            submissionRepository.save(submission);

        } catch (CompilationException ce) {

            submission.setStatus(SubmissionStatus.COMPILATION_ERROR);
            submission.setCompileError(ce.getMessage());
            submissionRepository.save(submission);

        } catch (RuntimeException e) {

            submission.setExecutionTimeMs(totalTime);
            submission.setMemoryKb(peakMemory);

            if ("TIME_LIMIT_EXCEEDED".equals(e.getMessage())) {
                submission.setStatus(SubmissionStatus.TIME_LIMIT_EXCEEDED);
            } else {
                submission.setStatus(SubmissionStatus.RUNTIME_ERROR);
                submission.setRuntimeError(e.getMessage());
            }

            submissionRepository.save(submission);

        } catch (Exception e) {

            submission.setStatus(SubmissionStatus.RUNTIME_ERROR);
            submission.setRuntimeError(e.getMessage());
            submissionRepository.save(submission);

        } finally {

            workspaceManager.cleanupBySubmission(submission.getId());
        }
    }
}