package com.codegraph.curriculum.entity;

import com.codegraph.problem.entity.Problem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chapters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Chapter parentChapter;

    @OneToMany(mappedBy = "parentChapter", cascade = CascadeType.ALL)
    private List<Chapter> subChapters = new ArrayList<>();

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Problem> problems = new ArrayList<>();
}
