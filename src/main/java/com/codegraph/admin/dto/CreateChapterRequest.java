package com.codegraph.admin.dto;

import lombok.Data;

@Data
public class CreateChapterRequest {
    public String title;
    public Integer orderIndex;
}
