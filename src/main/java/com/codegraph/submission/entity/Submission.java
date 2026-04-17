package com.codegraph.submission.entity;

import com.codegraph.common.enums.ProgrammingLanguage;
import com.codegraph.common.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Getter
@Setter
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String problemId;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    private ProgrammingLanguage language;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    private Long executionTimeMs;

    private Long memoryKb;

    @Lob
    private String compileError;

    @Lob
    private String runtimeError;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String failedInput;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String expectedOutput;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String actualOutput;

    private Integer passedTestCases = 0;
    private Integer totalTestCases = 0;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String failedTestCaseImage;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String failedTestCaseExplanation;

    private Integer failedTestCaseImageScale;

    private LocalDateTime submittedAt = LocalDateTime.now();

    // Getters and Setters
}