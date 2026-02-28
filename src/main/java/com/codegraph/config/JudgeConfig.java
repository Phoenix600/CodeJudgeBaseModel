package com.codegraph.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "judge")
public class JudgeConfig {

    private String workspace;
    private int timeoutSeconds;
    private String javaMemory;

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
}