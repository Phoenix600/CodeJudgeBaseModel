package com.codegraph.judge.runner;

public record RunResult(
        String output,
        long timeMs,
        long memoryKb
) {}