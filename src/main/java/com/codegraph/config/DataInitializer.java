package com.codegraph.config;

import com.codegraph.problem.entity.Problem;
import com.codegraph.problem.repository.ProblemRepository;
import com.codegraph.testcase.entity.TestCase;
import com.codegraph.testcase.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataInitializer(ProblemRepository problemRepository,
            TestCaseRepository testCaseRepository,
            ObjectMapper objectMapper) {
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        // ALWAYS clean the DB to start fresh with Blind 75
        System.out.println("Cleaning database...");
        testCaseRepository.deleteAll();
        problemRepository.deleteAll();

        System.out.println("Seeding Blind 75 problems from JSON...");
        loadProblemsFromJson();
        System.out.println("Seed complete!");
    }

    private void loadProblemsFromJson() throws Exception {
        InputStream is = new ClassPathResource("blind75.json").getInputStream();
        JsonNode rootNode = objectMapper.readTree(is);

        for (JsonNode problemNode : rootNode) {
            Problem p = new Problem();
            p.setTitle(problemNode.get("title").asText());
            p.setDescription(problemNode.get("description").asText());
            p.setDifficulty(problemNode.get("difficulty").asText());
            p.setTags(problemNode.get("tags").asText());
            p.setSolutionTemplate(problemNode.get("solutionTemplate").asText());
            p.setDriverCode(problemNode.get("driverCode").asText());
            problemRepository.save(p);

            JsonNode testCasesNode = problemNode.get("testCases");
            if (testCasesNode != null) {
                for (JsonNode tcNode : testCasesNode) {
                    addTestCase(p,
                            tcNode.get("input").asText(),
                            tcNode.get("output").asText(),
                            tcNode.get("sample").asBoolean());
                }
            }
        }
    }

    private void addTestCase(Problem problem, String input, String expected, boolean isSample) {
        TestCase tc = new TestCase();
        tc.setProblem(problem);
        tc.setInput(input);
        tc.setExpectedOutput(expected);
        tc.setSample(isSample);
        testCaseRepository.save(tc);
    }
}
