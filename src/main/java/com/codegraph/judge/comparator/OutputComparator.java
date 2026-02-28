package com.codegraph.judge.comparator;

import org.springframework.stereotype.Component;

@Component
public class OutputComparator {

    public boolean compare(String actual, String expected) {
        return actual.trim().equals(expected.trim());
    }
}