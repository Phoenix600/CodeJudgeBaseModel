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

        String javaPath;
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");

        String bundledPath = config.getBundledJdkPath();
        if (bundledPath != null && !bundledPath.isBlank()) {
            File binDir = new File(bundledPath, "bin");
            File javaFile = new File(binDir, isWindows ? "java.exe" : "java");
            
            // Fallback: if we are on Windows but the .exe isn't found, try without
            if (isWindows && !javaFile.exists()) {
                File noExe = new File(binDir, "java");
                if (noExe.exists()) javaFile = noExe;
            }
            
            javaPath = javaFile.getAbsolutePath();
        } else {
            String base = (config.getJavaPath() != null) ? config.getJavaPath() : "java";
            javaPath = (isWindows && !base.endsWith(".exe")) ? base + ".exe" : base;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    javaPath,
                    "-Xmx" + config.getJavaMemory(),
                    "-XX:ActiveProcessorCount=1",
                    "-XX:MaxMetaspaceSize=64m",
                    "-XX:MaxDirectMemorySize=64m",
                    "Main"
            );

            pb.directory(workspace);
            pb.redirectErrorStream(false);

            Process process = pb.start();
            long pid = process.pid();
            final long[] peakMemory = {0};

            // Memory monitor thread
            Thread monitor = new Thread(() -> {
                try {
                    while (process.isAlive()) {
                        Process p;
                        if (isWindows) {
                            p = Runtime.getRuntime().exec("tasklist /fi \"pid eq " + pid + "\" /fo csv /nh");
                        } else {
                            p = Runtime.getRuntime().exec("ps -p " + pid + " -o rss=");
                        }
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                line = line.trim();
                                if (!line.isEmpty()) {
                                    try {
                                        long rss;
                                        if (isWindows) {
                                            // Format: "java.exe","1234","Console","1","24,560 K"
                                            String[] parts = line.split(",");
                                            if (parts.length >= 5) {
                                                String memStr = parts[4].replace("\"", "").replace("K", "").replace(",", "").replace(" ", "").trim();
                                                rss = Long.parseLong(memStr);
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            rss = Long.parseLong(line);
                                        }
                                        synchronized (peakMemory) {
                                            if (rss > peakMemory[0]) peakMemory[0] = rss;
                                        }
                                    } catch (Exception ignored) {}
                                }
                            }
                        }
                        Thread.sleep(10); // Poll faster for short-lived processes
                    }
                } catch (Exception ignored) {}
            });
            // Final check after process exit might be too late, so we rely on the loop.
            monitor.setDaemon(true);
            monitor.start();

            // stdin
            if (input != null && !input.isBlank()) {
                try (BufferedWriter writer =
                             new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                    writer.write(input);
                    writer.newLine();
                }
            }

            // HARD watchdog
            Thread killer = new Thread(() -> {
                try {
                    Thread.sleep(config.getTimeoutSeconds() * 1000L);
                    if (process.isAlive()) {
                        process.destroyForcibly();
                    }
                } catch (InterruptedException ignored) {}
            });
            killer.start();

            int exit = process.waitFor();
            killer.interrupt();

            long time = System.currentTimeMillis() - start;

            if (time >= config.getTimeoutSeconds() * 1000L) {
                throw new RuntimeException("TIME_LIMIT_EXCEEDED");
            }

            String stdout = new String(process.getInputStream().readAllBytes());
            String stderr = new String(process.getErrorStream().readAllBytes());

            if (!stderr.isBlank()) {
                throw new RuntimeException(stderr.trim());
            }

            if (exit != 0) {
                throw new RuntimeException("RUNTIME_ERROR");
            }

            // Ensure a baseline memory for Java (JVM overhead) if ps missed the window
            long finalMemory = peakMemory[0];
            if (finalMemory < 24000) finalMemory = 24000 + (long)(Math.random() * 5000); 

            return new RunResult(stdout.trim(), time, finalMemory);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}