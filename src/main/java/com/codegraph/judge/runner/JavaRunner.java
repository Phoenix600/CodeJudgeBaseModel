package com.codegraph.judge.runner;

import com.codegraph.config.JudgeConfig;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class JavaRunner {

    private final JudgeConfig config;

    public JavaRunner(JudgeConfig config) {
        this.config = config;
    }

    public RunResult run(File workspace, String input) {

        long start = System.currentTimeMillis();

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    "-Xmx" + config.getJavaMemory(),
                    "-XX:ActiveProcessorCount=1",
                    "-XX:MaxMetaspaceSize=64m",
                    "-XX:MaxDirectMemorySize=64m",
                    "Main"
            );

            pb.directory(workspace);
            pb.redirectErrorStream(false);

            Process process = pb.start();

            // stdin
            if (input != null && !input.isBlank()) {
                try (BufferedWriter writer =
                             new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                    writer.write(input);
                    writer.newLine();
                }
            }

            // HARD watchdog (REAL timeout)
            Thread killer = new Thread(() -> {
                try {
                    Thread.sleep(config.getTimeoutSeconds() * 1000L);
                    if (process.isAlive()) {
                        process.destroyForcibly();
                    }
                } catch (InterruptedException ignored) {
                }
            });

            killer.start();

            int exit = process.waitFor();

            long time = System.currentTimeMillis() - start;

            if (time >= config.getTimeoutSeconds() * 1000L) {
                throw new RuntimeException("TIME_LIMIT_EXCEEDED");
            }

            String stdout =
                    new String(process.getInputStream().readAllBytes());

            String stderr =
                    new String(process.getErrorStream().readAllBytes());

            if (!stderr.isBlank()) {
                throw new RuntimeException(stderr.trim());
            }

            if (exit != 0) {
                throw new RuntimeException("RUNTIME_ERROR");
            }

            return new RunResult(stdout.trim(), time, 0L);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}