package com.codegraph.admin.dto;

import lombok.Data;

@Data
public class CreateCourseRequest {
    public String title;
    public String description;
    public String imageUrl;
}
