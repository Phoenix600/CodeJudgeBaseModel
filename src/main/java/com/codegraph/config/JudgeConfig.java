package com.codegraph.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
        return workspace;
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
        return bundledJdkPath;
    }

    public void setBundledJdkPath(String bundledJdkPath) {
        this.bundledJdkPath = bundledJdkPath;
    }
}