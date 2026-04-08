package com.codegraph.problem.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a coding challenge")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    private String difficulty;   // EASY, MEDIUM, HARD

    private String tags;         // arrays, dp, strings (comma separated)

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

}