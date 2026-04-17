# CodeGraph - Ultimate Online Judge & Learning Platform

CodeGraph is a robust, production-ready **Online Judge System** and **Coding Challenge Platform**. It is designed to host, execute, and evaluate code submissions with high performance and security, mimicking the experience of industry-leading platforms like LeetCode.

---

## 🏗 Key Features

### ⚖️ Judge Engine
-   **Smart Code Wrapping**: Automatically isolates user code and reinjects it into a hidden `Main` execution shell.
-   **Driver Template Engine**: Problems use a `driverCode` system with `{{SOLUTION}}` placeholders for seamless integration.
-   **Real-time Monitoring**: Strict Time Limit (TTL) and Memory Limit enforcement.
-   **Asynchronous Processing**: Final submissions are judged in parallel background thread pools.

### 📝 Problem Management
-   **Markdown Support**: Full Markdown support for problem descriptions.
-   **Rich Media**: Native integration with **Cloudinary** for image hosting.
-   **Multi-phase Testing**: Support for both "Sample" test cases (for quick runs) and "Hidden" test cases (for final evaluation).

### 💾 Persistence & UX
-   **Code Drafts (Auto-Save)**: Backend persistence for in-progress code to prevent loss on page refreshes or device switches.
-   **Global API Wrapper**: Standardized `ApiResponse<T>` for consistent frontend integration.
-   **Swagger Documentation**: Fully interactive OpenAPI 3.0 specs at `/swagger-ui/index.html`.

### 📦 Portability & Deployment
-   **Zero-Dependency Build**: Bundle an isolated Java Runtime (JRE) directly into the installer.
-   **Platform Ready**: Specialized build scripts for **Ubuntu (DEB)**, **macOS (DMG)**, and **Windows (EXE/MSI)**.

---

## 🛠 Tech Stack
-   **Back End**: Java 17, Spring Boot 3.x, Hibernate/JPA.
-   **Database**: H2 (Development/Embedded), easily swappable to PostgreSQL/MySQL.
-   **Images**: Cloudinary HTTP5 Integration.
-   **Documentation**: SpringDoc OpenAPI (Swagger).
-   **Build System**: Maven Wrapper (`mvnw`).

---

## 🚀 Getting Started

### Prerequisites
-   Java 17 JDK
-   Environment variables for Cloudinary (Optional, for image uploads):
    - `cloudinary.cloud_name`
    - `cloudinary.api_key`
    - `cloudinary.api_secret`

### Running the Server
```bash
./mvnw clean spring-boot:run
```
The server will start on [http://localhost:8080](http://localhost:8080).

---

## 📂 API Reference (Summary)

### 👨‍💻 Learner API (General Access)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/problems` | List challenges with paging/sorting |
| `GET` | `/problems/{id}` | Detailed problem data + template |
| `GET` | `/problems/{id}/testcases` | Fetch sample inputs/outputs |

### ⚖️ Judge API
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/submit/run` | Synchronous run against sample cases |
| `POST` | `/submit` | Asynchronous submission against hidden cases |
| `GET` | `/submit/{id}` | Poll for submission status & metrics |
| `GET` | `/submit/problem/{id}`| History of attempts for a problem |

### 💾 Code Draft API (Persistence)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/drafts` | Persist current editor code (Auto-save) |
| `GET` | `/drafts/{problemId}`| Recover draft for a user/problem pair |

### 🛠 Admin API (Secured)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/admin/problems` | Create core problem metadata |
| `POST` | `/admin/problems/{id}/images`| Upload images to Cloudinary (returns URLs) |
| `POST` | `/admin/problems/{id}/testcases`| Link hidden/sample test cases |

---

## 📦 Native Deployment (Production)

Use the provided scripts in the `scripts/` folder to create a production-ready, zero-dependency installation package for your OS.

- **Ubuntu/Debian**: `./scripts/build-linux.sh` (Produces `.deb`)
- **macOS**: `./scripts/build-mac.sh` (Produces `.dmg`)
- **Windows**: `./scripts/build-win.ps1` (Produces `.msi`)

---

## 🧩 Integration Guide for AI Agents
For instructions on how to integrate these APIs into a Frontend application (React/Vue), please see the **[AI_AGENT_GUIDE.md](./AI_AGENT_GUIDE.md)**.
