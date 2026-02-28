package com.codegraph.admin.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TestCaseRequest {

    public String input;
    public String expectedOutput;
    public Boolean sample = false;
}