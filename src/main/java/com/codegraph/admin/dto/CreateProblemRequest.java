package com.codegraph.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateProblemRequest {

    public String title;
    public String description;
    public String difficulty;
    public String tags;
}