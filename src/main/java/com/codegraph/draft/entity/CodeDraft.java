package com.codegraph.draft.entity;

import com.codegraph.common.enums.ProgrammingLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "code_drafts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"userId", "problemId"})
})
@Getter
@Setter
public class CodeDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String problemId;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String code;

    @Enumerated(EnumType.STRING)
    private ProgrammingLanguage language;

    private LocalDateTime updatedAt = LocalDateTime.now();
}
