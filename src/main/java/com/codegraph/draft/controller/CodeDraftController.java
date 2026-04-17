package com.codegraph.draft.controller;

import com.codegraph.common.dto.ApiResponse;
import com.codegraph.draft.dto.CodeDraftRequest;
import com.codegraph.draft.entity.CodeDraft;
import com.codegraph.draft.service.CodeDraftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drafts")
@Tag(name = "Code Draft API", description = "Endpoints for auto-saving and recovering user code progress")
public class CodeDraftController {

    private final CodeDraftService service;

    public CodeDraftController(CodeDraftService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Save or Update a code draft", description = "Persists the current code state for a specific user and problem.")
    public ApiResponse<CodeDraft> saveDraft(@RequestBody CodeDraftRequest request) {
        return ApiResponse.success("Draft saved successfully", service.saveOrUpdateDraft(request));
    }

    @GetMapping("/{problemId}")
    @Operation(summary = "Recover a code draft", description = "Retrieves the last saved code state for the specified problem and user.")
    public ApiResponse<CodeDraft> getDraft(
            @PathVariable String problemId,
            @RequestParam String userId) {
        return service.getDraft(userId, problemId)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("No draft found"));
    }
}
