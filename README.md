# CodeGraph Judge Engine

CodeGraph is a robust, Spring Boot-based **Online Judge System** designed for compiling, executing, and evaluating code submissions against predefined problem test cases.

## ✨ Features

-   **Multi-Problem Support**: Pre-initialized with standard LeetCode problems (Two Sum, Palindrome Number, Reverse String).
-   **Smart Code Wrapping**: Automatically detects and renames public classes to `Main` for standalone mode.
-   **Driver Template Engine**: Supports injection of user logic into a hidden `Main` class, providing a seamless LeetCode-like experience.
-   **Solution Templates**: Each problem can store a starting snippet (e.g., `class Solution { ... }`) to be displayed to users in the editor.
-   **Package Stripping**: Automatically removes `package` declarations from submissions to prevent execution errors.
-   **Resource Monitoring**: Execution includes constraints on memory usage and provides a strict time-limit watchdog.
-   **Advanced Paging & Sorting**: All major listing endpoints (Problems and Submissions) support standard Spring Data Paging and Sorting.
-   **Asynchronous Judging**: Code execution happens in a separate thread pool to ensure high responsiveness.

## 🚀 Getting Started

### Prerequisites
-   Java 17 or higher
-   Maven (or use the provided `./mvnw`)

### Running the Server
In the project root, run:
```bash
./mvnw spring-boot:run
```
The server will be available at [http://localhost:8080](http://localhost:8080).
Interactive documentation is available at [Swagger UI](http://localhost:8080/swagger-ui/index.html).

## 🛠 API Reference

### Public API (Learner)
-   **List Problems**: `GET /problems`
    -   *Example*: `/problems?page=0&size=5&sort=difficulty,asc`
-   **Get Problem Details**: `GET /problems/{id}`

### Admin API (Management)
-   **Create Problem**: `POST /admin/problems`
-   **Add Testcases**: `POST /admin/problems/{id}/testcases`

## 🧪 Testing Scenario: Run vs Submit

This scenario demonstrates the difference between "Running" code and "Submitting" it.

### 1. Setup Hidden Test Cases (Admin)
The **Two Sum** problem has sample test cases (e.g., `[2,7,11,15], target=9`). We will add a **Hidden** test case involving negative numbers.
```bash
# Add a hidden test case (sample=false)
POST /admin/problems/1/testcases
[
  { "input": "3 -2 4 1", "expectedOutput": "[0, 1]", "sample": false }
]
```

### 2. The Naive Solution (Common Beginner Mistake)
A common mistake is trying to use the **Two-Pointer** approach on an array that isn't sorted (a technique that only works if the array is pre-sorted).

```java
// User Code in Editor
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // MISTAKE: Assumes array is sorted!
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) return new int[]{left, right};
            if (sum < target) left++;
            else right--;
        }
        return new int[]{0, 0};
    }
}
```

### 3. Execution Results
*   **Action: RUN**: The system executes against `[2,7,11,15], target=9` (which is already sorted).
    *   **Result**: `ACCEPTED` (The mistake isn't caught yet!)
*   **Action: SUBMIT**: The system executes against **all** tests, including the hidden unsorted case `[3,2,4], target=6` or negative cases.
    *   **Result**: `WRONG_ANSWER`
    *   **Diagnostic**:
        *   **Failed Input**: `6\n3\n3 2 4`
        *   **Expected**: `[1, 2]`
        *   **Actual**: `[0, 0]` (or something incorrect)

This perfectly demonstrates how **Hidden Test Cases** protect the integrity of the problem by catching logic that only works on specific kinds of input!

### Submissions
-   **Submit Code**: `POST /submit`
-   **Submission History**: `GET /submit`
-   **Problem History**: `GET /submit/problem/{problemId}`
-   **Get Specific Result**: `GET /submit/{id}`

## 📝 Problem Configuration

Each `Problem` now supports:
-   `driverCode`: The "hidden" code containing `public class Main` and test case runner logic. It uses `{{SOLUTION}}` as a placeholder for user code.
-   `solutionTemplate`: The initial snippet displayed to users in their code editor (e.g., `public class Solution { public int twoSum(...) { } }`).

## ⚙️ Configuration
The engine can be tuned via `src/main/resources/application.properties`:
-   `judge.timeout-seconds`: Maximum execution time.
-   `judge.java-memory`: Maximum JVM heap memory for the user code.
-   `judge.java-path` / `judge.javac-path`: Path to JDK binaries.
