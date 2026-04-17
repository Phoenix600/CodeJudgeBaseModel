# AI Agent Guide: Frontend Integration with CodeGraph API

This guide is designed for AI coding assistants (like Gemini, Claude, or GPT) or frontend developers tasked with building the UI for CodeGraph. It outlines the specific patterns and logic required to integrate the backend APIs effectively.

---

## 🏗 Core Workflows

### 1. The Problem Loading Flow
When a user navigates to a problem page (`/problem/:id`):

1.  **API Call**: `GET /problems/{id}`
2.  **Display Logic**: Render the `description` as Markdown.
3.  **Code Editor State (Recovery Order)**:
    -   Priority 1: `GET /drafts/{problemId}?userId={currentUserId}`. If data exists, use `draft.code`.
    -   Priority 2: `localStorage.getItem('code_' + problemId)`.
    -   Priority 3: `problem.solutionTemplate` (Default fallback).

### 2. Execution Workflows ("Run" vs "Submit")

| Action | API Endpoint | Description | Behavior |
| :--- | :--- | :--- | :--- |
| **RUN** | `POST /submit/run` | Fast check against samples. | Synchronous. Show result immediately. |
| **SUBMIT** | `POST /submit` | Final evaluation. | Asynchronous. Must poll `/submit/{id}` until status is `ACCEPTED` or `FAILED`. |

**Submit Polling Logic**:
```javascript
async function pollStatus(id) {
    const res = await axios.get(`/submit/${id}`);
    if (res.data.data.status === 'PENDING') {
        setTimeout(() => pollStatus(id), 1000); // Poll every second
    } else {
        updateUIWithResult(res.data.data);
    }
}
```

### 3. The Persistence Workflow (Auto-Save)
Implement a debounced auto-save function to avoid overwhelming the server.

-   **Trigger**: Code change in the editor.
-   **Delay**: 2–5 seconds of inactivity.
-   **API**: `POST /drafts`
    ```json
    {
      "userId": 1,
      "problemId": 10,
      "code": "...",
      "language": "JAVA"
    }
    ```

---

## 🎨 Design & UX Expectations

### Code Editor
-   Use **Monaco Editor** (VS Code engine) or **CodeMirror 6**.
-   Ensure the language is set to `java`.
-   Provide a "Reset to Template" button that clears the Draft/Local Storage and reloads `solutionTemplate`.

### Result Feedback
-   **Success**: Green "Accepted" badge with runtime and memory metrics.
-   **Error**: Distinct UI for `COMPILE_ERROR` (show the provided `compileError` string) vs `WRONG_ANSWER` (show diff between `actualOutput` and `expectedOutput`).
-   **Limits**: Show "Time Limit Exceeded" specifically if the status is `TIME_LIMIT_EXCEEDED`.

---

## 🛠 Admin Integration (Problem Creation)

### Image Upload Workflow
When creating a problem with images:
1.  Upload image file(s) to `POST /admin/problems/{id}/images`.
2.  The API returns a JSON list of Cloudinary URLs.
3.  **Instruction**: Immediately insert these URLs into the Markdown editor at the cursor position using standard `![](url)` syntax.

### Test Case Management
-   Support multiple test cases in a single payload.
-   Provide a toggle for `sample: true/false`.
-   **Sample** test cases are visible to users. **Hidden** cases (`sample: false`) are only used for the final result.

---

## 🚦 Error Handling Summary
The backend wraps all responses in an `ApiResponse` object.
-   Always check `response.data.success`.
-   If `false`, display `response.data.message` in a Toast or Alert notification.
