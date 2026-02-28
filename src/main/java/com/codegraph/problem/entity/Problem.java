package com.codegraph.problem.entity;

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

}