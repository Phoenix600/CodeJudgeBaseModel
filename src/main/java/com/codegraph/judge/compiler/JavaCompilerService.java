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
        String os = System.getProperty("os.name").toLowerCase();
        String executableName = os.contains("win") ? "javac.exe" : "javac";

        if (judgeConfig.getBundledJdkPath() != null && !judgeConfig.getBundledJdkPath().isBlank()) {
            javacPath = new File(judgeConfig.getBundledJdkPath(), "bin/" + executableName).getAbsolutePath();
        } else {
            javacPath = (judgeConfig.getJavacPath() != null) ? judgeConfig.getJavacPath() : executableName;
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