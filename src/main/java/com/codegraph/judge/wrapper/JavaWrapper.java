package com.codegraph.judge.wrapper;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;

@Component
public class JavaWrapper {

    public void writeMain(File workspace, String code) {

        try {

            File file = new File(workspace, "Main.java");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(code);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to write Main.java", e);
        }
    }

    public void writePolicy(File workspace) {

        try {

            File policy = new File(workspace, "policy.policy");

            try (FileWriter fw = new FileWriter(policy)) {
                fw.write("""
                grant {
                  permission java.lang.RuntimePermission "exitVM";
                };
                """);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to write security policy", e);
        }
    }
}