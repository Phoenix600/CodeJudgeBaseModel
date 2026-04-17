package com.codegraph.config;

import com.codegraph.curriculum.entity.Chapter;
import com.codegraph.curriculum.entity.Course;
import com.codegraph.curriculum.repository.ChapterRepository;
import com.codegraph.curriculum.repository.CourseRepository;
import com.codegraph.problem.entity.Problem;
import com.codegraph.problem.repository.ProblemRepository;
import com.codegraph.testcase.entity.TestCase;
import com.codegraph.testcase.repository.TestCaseRepository;
import com.codegraph.problem.entity.Tag;
import com.codegraph.problem.repository.TagRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataInitializer(ProblemRepository problemRepository,
                           TestCaseRepository testCaseRepository,
                           CourseRepository courseRepository,
                           ChapterRepository chapterRepository,
                           TagRepository tagRepository,
                           ObjectMapper objectMapper) {
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.tagRepository = tagRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Cleaning database...");
//        testCaseRepository.deleteAll();
//        problemRepository.deleteAll();
//        chapterRepository.deleteAll();
//        courseRepository.deleteAll();

        System.out.println("Seeding Curriculum and Blind 75 problems...");
        
        // 1. Create Main Course
        Course course = new Course();
        course.setTitle("Data Structures & Algorithms (Blind 75)");
        course.setDescription("Master the top 75 patterns for coding interviews.");
        courseRepository.save(course);

        // 2. Load Problems
        loadProblemsFromJson(course);
        
        System.out.println("Seed complete!");
    }

    private void loadProblemsFromJson(Course course) throws Exception {
        InputStream is = new ClassPathResource("blind75.json").getInputStream();
        JsonNode rootNode = objectMapper.readTree(is);

        Map<String, Chapter> chapterMap = new HashMap<>();
        Map<String, Map<String, Chapter>> subChapterCache = new HashMap<>();
        int chapterOrder = 0;

        // Standard subchapter types
        String[] standardSubTitles = {"Fundamentals", "Logic Building", "FAQs(Medium)", "FAQs(Hard)", "Contest"};

        for (JsonNode problemNode : rootNode) {
            String chapterTitle = problemNode.has("chapterTitle") ? problemNode.get("chapterTitle").asText() : "Miscellaneous";
            String problemType = problemNode.has("problemType") ? problemNode.get("problemType").asText().toLowerCase() : "fundamentals";

            // Create or get root chapter
            Chapter rootChapter = chapterMap.get(chapterTitle);
            if (rootChapter == null) {
                rootChapter = new Chapter();
                rootChapter.setTitle(chapterTitle);
                rootChapter.setCourse(course);
                rootChapter.setOrderIndex(chapterOrder++);
                chapterRepository.save(rootChapter);
                chapterMap.put(chapterTitle, rootChapter);
                
                // Initialize subchapter cache for this root chapter
                Map<String, Chapter> subChapters = new HashMap<>();
                for (int i = 0; i < standardSubTitles.length; i++) {
                    Chapter subChapter = new Chapter();
                    subChapter.setTitle(standardSubTitles[i]);
                    subChapter.setCourse(course);
                    subChapter.setParentChapter(rootChapter);
                    subChapter.setOrderIndex(i);
                    chapterRepository.save(subChapter);
                    subChapters.put(standardSubTitles[i], subChapter);
                }
                subChapterCache.put(chapterTitle, subChapters);
            }

            // Map problem type to subchapter title
            String subChapterTitle = null;
            if (problemType.equals("fundamentals")) {
                subChapterTitle = "Fundamentals";
            } else if (problemType.equals("logic")) {
                subChapterTitle = "Logic Building";
            } else if (problemType.equals("faq_medium")) {
                subChapterTitle = "FAQs(Medium)";
            } else if (problemType.equals("faq_hard")) {
                subChapterTitle = "FAQs(Hard)";
            } else if (problemType.equals("contest")) {
                subChapterTitle = "Contest";
            } else {
                subChapterTitle = "Fundamentals";
            }

            Map<String, Chapter> subChapters = subChapterCache.get(chapterTitle);
            Chapter targetChapter = subChapters.get(subChapterTitle);

            Problem p = new Problem();
            p.setTitle(problemNode.get("title").asText());
            p.setDescription(problemNode.get("description").asText());
            p.setDifficulty(problemNode.get("difficulty").asText());
            p.setTags(resolveTags(problemNode.get("tags")));
            p.setSolutionTemplate(problemNode.get("solutionTemplate").asText());
            p.setDriverCode(problemNode.get("driverCode").asText());
            p.setChapter(targetChapter);

            if (problemNode.has("editorialVideoUrl")) {
                p.setEditorialVideoUrl(problemNode.get("editorialVideoUrl").asText());
            }
            if (problemNode.has("editorialPdfUrl")) {
                p.setEditorialPdfUrl(problemNode.get("editorialPdfUrl").asText());
            }
            if (problemNode.has("quizQuestion")) {
                p.setQuizQuestion(problemNode.get("quizQuestion").asText());
            }
            if (problemNode.has("quizOptions")) {
                p.setQuizOptions(problemNode.get("quizOptions").asText());
            }
            if (problemNode.has("quizCorrectAnswer")) {
                p.setQuizCorrectAnswer(problemNode.get("quizCorrectAnswer").asText());
            }

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

    private Set<Tag> resolveTags(JsonNode tagsNode) {
        if (tagsNode == null || tagsNode.isNull()) return Set.of();
        
        if (tagsNode.isArray()) {
            Set<String> names = new HashSet<>();
            for (JsonNode node : tagsNode) {
                names.add(node.asText().trim());
            }
            return names.stream()
                    .filter(name -> !name.isEmpty())
                    .map(name -> tagRepository.findByName(name)
                            .orElseGet(() -> tagRepository.save(new Tag(name))))
                    .collect(Collectors.toSet());
        } else {
            String tagsString = tagsNode.asText();
            if (tagsString.isEmpty()) return Set.of();
            return Arrays.stream(tagsString.split(","))
                    .map(String::trim)
                    .filter(name -> !name.isEmpty())
                    .map(name -> tagRepository.findByName(name)
                            .orElseGet(() -> tagRepository.save(new Tag(name))))
                    .collect(Collectors.toSet());
        }
    }
}
