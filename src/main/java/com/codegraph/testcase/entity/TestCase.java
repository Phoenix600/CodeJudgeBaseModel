package com.codegraph.testcase.entity;

import com.codegraph.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "testcases")
@Getter
@Setter
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Lob
    @Column(nullable = false)
    private String input;

    @Lob
    @Column(nullable = false)
    private String expectedOutput;

    private Boolean sample = false;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String explanation;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String image;

    private Integer imageScale = 40;

    // Getters & Setters
}