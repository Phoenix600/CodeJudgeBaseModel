package com.codegraph.judge.compiler;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JavaCompilerService {
    private final com.codegraph.config.JudgeConfig judgeConfig;

    public JavaCompilerService(com.codegraph.config.JudgeConfig judgeConfig) {
        this.judgeConfig = judgeConfig;
    }

    public void compile(File workspace) {

        String javacPath;
        if (judgeConfig.getBundledJdkPath() != null && !judgeConfig.getBundledJdkPath().isBlank()) {
            javacPath = new File(judgeConfig.getBundledJdkPath(), "bin/javac").getAbsolutePath();
        } else {
            javacPath = (judgeConfig.getJavacPath() != null) ? judgeConfig.getJavacPath() : "javac";
        }

        try {

            ProcessBuilder pb =
                    new ProcessBuilder(javacPath, "Main.java");

            pb.directory(workspace);
            pb.redirectErrorStream(true);

            Process p = pb.start();

            String errors = new String(p.getInputStream().readAllBytes());

            int exit = p.waitFor();

            if (exit != 0) {
                throw new CompilationException(errors);
            }

        } catch (IOException | InterruptedException e) {
            throw new CompilationException(e.getMessage());
        }
    }
}