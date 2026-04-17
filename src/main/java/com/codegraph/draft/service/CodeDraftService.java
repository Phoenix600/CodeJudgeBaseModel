package com.codegraph.draft.service;

import com.codegraph.draft.dto.CodeDraftRequest;
import com.codegraph.draft.entity.CodeDraft;
import com.codegraph.draft.repository.CodeDraftRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CodeDraftService {

    private final CodeDraftRepository repository;

    public CodeDraftService(CodeDraftRepository repository) {
        this.repository = repository;
    }

    public CodeDraft saveOrUpdateDraft(CodeDraftRequest request) {
        Optional<CodeDraft> existing = repository.findByUserIdAndProblemId(request.getUserId(), request.getProblemId());
        
        CodeDraft draft = existing.orElse(new CodeDraft());
        draft.setUserId(request.getUserId());
        draft.setProblemId(request.getProblemId());
        draft.setCode(request.getCode());
        draft.setLanguage(request.getLanguage());
        draft.setUpdatedAt(LocalDateTime.now());
        
        return repository.save(draft);
    }

    public Optional<CodeDraft> getDraft(String userId, String problemId) {
        return repository.findByUserIdAndProblemId(userId, problemId);
    }
}
