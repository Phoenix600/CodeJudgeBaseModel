package com.codegraph.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.codegraph.admin.dto.CreateProblemRequest;
import com.codegraph.admin.dto.TestCaseRequest;
import com.codegraph.problem.entity.Problem;
import com.codegraph.problem.repository.ProblemRepository;
import com.codegraph.testcase.repository.TestCaseRepository;
import com.codegraph.testcase.entity.TestCase;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.codegraph.common.service.CloudinaryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.codegraph.problem.entity.Tag;
import com.codegraph.problem.repository.TagRepository;

import com.codegraph.curriculum.entity.Course;
import com.codegraph.curriculum.entity.Chapter;
import com.codegraph.curriculum.repository.CourseRepository;
import com.codegraph.curriculum.repository.ChapterRepository;
import com.codegraph.admin.dto.CreateCourseRequest;
import com.codegraph.admin.dto.CreateChapterRequest;
import com.codegraph.submission.repository.SubmissionRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final CloudinaryService cloudinaryService;
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final SubmissionRepository submissionRepository;
    private final TagRepository tagRepository;
    
    protected AdminService() {
        this.problemRepository = null;
        this.testCaseRepository = null;
        this.cloudinaryService = null;
        this.courseRepository = null;
        this.chapterRepository = null;
        this.submissionRepository = null;
        this.tagRepository = null;
    }

    @Autowired
    public AdminService(ProblemRepository problemRepository,
                        TestCaseRepository testCaseRepository,
                        CloudinaryService cloudinaryService,
                        CourseRepository courseRepository,
                        ChapterRepository chapterRepository,
                        SubmissionRepository submissionRepository,
                        TagRepository tagRepository) {
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.cloudinaryService = cloudinaryService;
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
        this.submissionRepository = submissionRepository;
        this.tagRepository = tagRepository;
    }

    public Course createCourse(CreateCourseRequest request) {
        Course c = new Course();
        c.setTitle(request.title);
        c.setDescription(request.description);
        c.setImageUrl(request.imageUrl);
        return courseRepository.save(c);
    }

    public Chapter createChapter(String courseId, CreateChapterRequest request) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        Chapter ch = new Chapter();
        ch.setCourse(course);
        ch.setTitle(request.title);
        ch.setOrderIndex(request.orderIndex);
        return chapterRepository.save(ch);
    }

    public Problem createProblem(CreateProblemRequest request) {
        Problem p = new Problem();
        p.setTitle(request.getTitle());
        p.setDescription(request.getDescription());
        p.setDifficulty(request.getDifficulty());
        p.setTags(resolveTags(request.getTags()));
        p.setDriverCode(request.getDriverCode());
        p.setSolutionTemplate(request.getSolutionTemplate());
        if (request.getTimeLimitMs() != null) p.setTimeLimitMs(request.getTimeLimitMs());
        if (request.getMemoryLimitMb() != null) p.setMemoryLimitMb(request.getMemoryLimitMb());
        if (request.getFooter() != null) p.setFooter(request.getFooter());
        if (request.getImage() != null) p.setImage(request.getImage());
        if (request.getImageScale() != null) p.setImageScale(request.getImageScale());
        if (request.getEditorialVideoUrl() != null) p.setEditorialVideoUrl(request.getEditorialVideoUrl());
        if (request.getEditorialPdfUrl() != null) p.setEditorialPdfUrl(request.getEditorialPdfUrl());
        if (request.getQuizQuestion() != null) p.setQuizQuestion(request.getQuizQuestion());
        if (request.getQuizOptions() != null) p.setQuizOptions(request.getQuizOptions());
        if (request.getQuizCorrectAnswer() != null) p.setQuizCorrectAnswer(request.getQuizCorrectAnswer());
        
        if (request.getChapterId() != null) {
            Chapter chapter = chapterRepository.findById(request.getChapterId()).orElseThrow();
            p.setChapter(chapter);
        }
        
        return problemRepository.save(p);
    }

    public Problem updateProblem(String id, CreateProblemRequest request) {
        Problem p = problemRepository.findById(id).orElseThrow();
        if (request.getTitle() != null) p.setTitle(request.getTitle());
        if (request.getDescription() != null) p.setDescription(request.getDescription());
        if (request.getDifficulty() != null) p.setDifficulty(request.getDifficulty());
        if (request.getTags() != null) p.setTags(resolveTags(request.getTags()));
        if (request.getDriverCode() != null) p.setDriverCode(request.getDriverCode());
        if (request.getSolutionTemplate() != null) p.setSolutionTemplate(request.getSolutionTemplate());
        if (request.getTimeLimitMs() != null) p.setTimeLimitMs(request.getTimeLimitMs());
        if (request.getMemoryLimitMb() != null) p.setMemoryLimitMb(request.getMemoryLimitMb());
        if (request.getFooter() != null) p.setFooter(request.getFooter());
        if (request.getImage() != null) p.setImage(request.getImage());
        if (request.getImageScale() != null) p.setImageScale(request.getImageScale());
        if (request.getEditorialVideoUrl() != null) p.setEditorialVideoUrl(request.getEditorialVideoUrl());
        if (request.getEditorialPdfUrl() != null) p.setEditorialPdfUrl(request.getEditorialPdfUrl());
        if (request.getQuizQuestion() != null) p.setQuizQuestion(request.getQuizQuestion());
        if (request.getQuizOptions() != null) p.setQuizOptions(request.getQuizOptions());
        if (request.getQuizCorrectAnswer() != null) p.setQuizCorrectAnswer(request.getQuizCorrectAnswer());
        return problemRepository.save(p);
    }

    @Transactional
    public void addTestCases(String problemId, List<TestCaseRequest> requests) {
        Problem problem = problemRepository.findById(problemId).orElseThrow();

        // Fetch existing test cases to handle deletions
        List<TestCase> existingTestCases = testCaseRepository.findByProblem_Id(problemId);
        List<String> incomingIds = requests.stream()
                .map(r -> r.id)
                .filter(java.util.Objects::nonNull)
                .toList();

        // Delete test cases not in the incoming request
        List<TestCase> toDelete = existingTestCases.stream()
                .filter(etc -> !incomingIds.contains(etc.getId()))
                .toList();
        testCaseRepository.deleteAll(toDelete);

        // Update existing or add new
        for (TestCaseRequest r : requests) {
            TestCase tc;
            if (r.id != null) {
                tc = testCaseRepository.findById(r.id).orElse(new TestCase());
            } else {
                tc = new TestCase();
            }
            
            tc.setProblem(problem);
            tc.setInput(r.input);
            tc.setExpectedOutput(r.expectedOutput);
            tc.setSample(r.sample);
            tc.setExplanation(r.explanation);
            if (r.image != null) tc.setImage(r.image);
            tc.setImageScale(r.imageScale);
            testCaseRepository.save(tc);
        }
    }

    public List<String> uploadImages(String problemId, MultipartFile[] files) throws Exception {
        List<String> urls = new ArrayList<>();
        String folder = "codegraph/problems/" + problemId;

        for (MultipartFile file : files) {
            String url = cloudinaryService.uploadImage(file, folder);
            urls.add(url);
        }
        return urls;
    }

    public String uploadImage(MultipartFile file) throws Exception {
        return cloudinaryService.uploadImage(file, "codegraph/assets");
    }

    public org.springframework.data.domain.Page<Problem> fetchAllProblems(org.springframework.data.domain.Pageable pageable) {
        return problemRepository.findAll(pageable);
    }

    public Problem fetchProblemById(String id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
    }

    public List<TestCase> fetchSampleTestCases(String problemId) {
        return testCaseRepository.findByProblem_IdAndSampleTrue(problemId);
    }

    public List<TestCase> fetchAllTestCases(String problemId) {
        return testCaseRepository.findByProblem_Id(problemId);
    }

    private Set<Tag> resolveTags(List<String> tagNames) {
        if (tagNames == null) return Set.of();
        return tagNames.stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void deleteProblem(String id) {
        submissionRepository.deleteByProblemId(id);
        problemRepository.deleteById(id);
    }
}