package com.codegraph.draft.repository;

import com.codegraph.draft.entity.CodeDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CodeDraftRepository extends JpaRepository<CodeDraft, String> {
    Optional<CodeDraft> findByUserIdAndProblemId(String userId, String problemId);
}
