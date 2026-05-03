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
        boolean isWindows = os.contains("win");
        
        String bundledPath = judgeConfig.getBundledJdkPath();
        if (bundledPath != null && !bundledPath.isBlank()) {
            File binDir = new File(bundledPath, "bin");
            File javacFile = new File(binDir, isWindows ? "javac.exe" : "javac");
            
            // Fallback: if we are on Windows but the .exe isn't found, try without (unlikely but safe)
            if (isWindows && !javacFile.exists()) {
                File noExe = new File(binDir, "javac");
                if (noExe.exists()) javacFile = noExe;
            }
            
            javacPath = javacFile.getAbsolutePath();
        } else {
            String base = (judgeConfig.getJavacPath() != null) ? judgeConfig.getJavacPath() : "javac";
            javacPath = (isWindows && !base.endsWith(".exe")) ? base + ".exe" : base;
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