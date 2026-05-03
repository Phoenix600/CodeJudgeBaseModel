package com.codegraph.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = "judge")
public class JudgeConfig {

    private String workspace;
    private int timeoutSeconds;
    private String javaMemory;
    private String javaPath;
    private String javacPath;
    private String bundledJdkPath;

    public String getWorkspace() {
        if (workspace == null || workspace.isBlank()) return "./workspaces";
        
        File path = new File(workspace);
        if (path.isAbsolute()) return workspace;

        // Try relative to the JAR location (handling jpackage structure)
        try {
            File jarPath = new File(JudgeConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File appRoot = jarPath.getParentFile().getParentFile(); 
            File resolvedPath = new File(appRoot, workspace);
            return resolvedPath.getAbsolutePath();
        } catch (Exception ignored) {}

        return path.getAbsolutePath();
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public String getJavaMemory() {
        return javaMemory;
    }

    public void setJavaMemory(String javaMemory) {
        this.javaMemory = javaMemory;
    }

    public String getJavaPath() {
        return javaPath;
    }

    public void setJavaPath(String javaPath) {
        this.javaPath = javaPath;
    }

    public String getJavacPath() {
        return javacPath;
    }

    public void setJavacPath(String javacPath) {
        this.javacPath = javacPath;
    }

    public String getBundledJdkPath() {
        if (bundledJdkPath == null || bundledJdkPath.isBlank()) return null;
        
        File path = new File(bundledJdkPath);
        if (path.isAbsolute()) return bundledJdkPath;

        // Try relative to CWD
        if (new File(path, "bin").exists()) {
            return path.getAbsolutePath();
        }

        // Try relative to the JAR location (handling jpackage structure: ROOT/app/jar)
        try {
            File jarPath = new File(JudgeConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File appRoot = jarPath.getParentFile().getParentFile(); 
            File bundledPath = new File(appRoot, bundledJdkPath);
            if (new File(bundledPath, "bin").exists()) {
                return bundledPath.getAbsolutePath();
            }
        } catch (Exception ignored) {}

        // Windows specific fallback: C:\CodeGraph\runtime
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            File fallbackPath = new File("C:\\CodeGraph", bundledJdkPath);
            if (new File(fallbackPath, "bin").exists()) {
                return fallbackPath.getAbsolutePath();
            }
        }

        return path.getAbsolutePath();
    }

    public void setBundledJdkPath(String bundledJdkPath) {
        this.bundledJdkPath = bundledJdkPath;
    }
}