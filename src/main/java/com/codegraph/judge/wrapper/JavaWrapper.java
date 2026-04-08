package com.codegraph.judge.wrapper;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;

@Component
public class JavaWrapper {

    public void writeMain(File workspace, String code, String driverCode) {

        try {

            String processedCode = code;

            // If a driver template exists, wrap the code
            if (driverCode != null && !driverCode.isBlank()) {
                // Strip public from user's classes if they are in the same file as Main
                String snippet = code.replaceAll("public\\s+class", "class");
                processedCode = driverCode.replace("{{SOLUTION}}", snippet);
            }

            // Strip package declaration
            processedCode = processedCode.replaceAll("(?m)^\\s*package\\s+.*;", "");

            // Rename public class to Main in standalone mode (no driverCode)
            if (driverCode == null || driverCode.isBlank()) {
                processedCode = processedCode.replaceAll("public\\s+class\\s+\\w+", "public class Main");
            }

            File file = new File(workspace, "Main.java");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(processedCode);
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