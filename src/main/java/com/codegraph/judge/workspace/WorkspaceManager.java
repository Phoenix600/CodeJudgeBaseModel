package com.codegraph.judge.workspace;

import com.codegraph.config.JudgeConfig;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;

@Component
public class WorkspaceManager {

    private final JudgeConfig config;

    public WorkspaceManager(JudgeConfig config) {
        this.config = config;
    }

    public File create(Long submissionId) {

        File dir = new File(config.getWorkspace() + "/" + submissionId);
        dir.mkdirs();
        return dir;
    }

    public void cleanup(File dir) {

        if (dir == null || !dir.exists()) return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }

        dir.delete();
    }

    public void cleanupBySubmission(Long id) {

        cleanup(new File(config.getWorkspace() + "/" + id));
    }
}