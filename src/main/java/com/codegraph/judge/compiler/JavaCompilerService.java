package com.codegraph.judge.compiler;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JavaCompilerService {

    public void compile(File workspace) {

        try {

            ProcessBuilder pb =
                    new ProcessBuilder("javac", "Main.java");

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