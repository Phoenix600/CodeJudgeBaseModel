package com.codegraph.problem.entity;

import com.codegraph.curriculum.entity.Chapter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.codegraph.testcase.entity.TestCase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a coding challenge")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    private String difficulty;   // EASY, MEDIUM, HARD

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "problem_tags",
        joinColumns = @JoinColumn(name = "problem_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    private Integer timeLimitMs = 2000;

    private Integer memoryLimitMb = 256;

    private Boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Lob
    @Column(columnDefinition = "CLOB")
    private String driverCode;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String solutionTemplate;

    private String editorialVideoUrl;

    private String editorialPdfUrl;

    private String quizQuestion;

    private String quizOptions;

    private String quizCorrectAnswer;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String footer;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String image;

    private Integer imageScale = 40;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    @JsonIgnore
    private Chapter chapter;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TestCase> testCases = new ArrayList<>();

}