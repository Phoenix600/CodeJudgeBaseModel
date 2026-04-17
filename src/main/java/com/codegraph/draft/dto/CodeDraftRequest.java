package com.codegraph.draft.dto;

import com.codegraph.common.enums.ProgrammingLanguage;
import lombok.Data;

@Data
public class CodeDraftRequest {
    private String userId;
    private String problemId;
    private String code;
    private ProgrammingLanguage language;
}
