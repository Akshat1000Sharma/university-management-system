# HMS Testing — Viva Preparation Document

This document explains **from scratch** how automated testing was added to the Hall Management System (HMS): what tools we used, why, how they are wired into the project, how tests are written, what exists per module, and common **viva questions**.

---

## 1. What Is JUnit? Why Use It? How Do We Use It?

### What is JUnit?

**JUnit** is a widely used **unit testing framework for Java**. The project uses **JUnit 5** (Jupiter), which provides:

- **Annotations** such as `@Test`, `@BeforeEach`, `@DisplayName`, `@ExtendWith`
- **Assertions** (`assertEquals`, `assertThrows`, `assertTrue`, etc.)
- **Test lifecycle** hooks (setup/teardown per test or per class)
- **Extensions** (e.g. Mockito integration via `@ExtendWith(MockitoExtension.class)`)

### Why use JUnit here?

1. **Fast feedback** — Run hundreds of checks in seconds after code changes.
2. **Regression safety** — Business rules (dues, salary, admit student) stay correct when you refactor.
3. **Documentation** — Tests describe expected behaviour (e.g. “occupied room must reject admit”).
4. **No manual server for unit tests** — Service tests use **mocks**, not MySQL; controller tests use **MockMvc** without starting Tomcat.

### How we use JUnit in this project

| Layer | Tooling | Purpose |
|-------|---------|---------|
| **Service layer** | JUnit 5 + **Mockito** (`@Mock`, `@InjectMocks`) | Replace `Repository` and other beans with fakes; test business logic in isolation. |
| **Controller layer** | JUnit 5 + **MockMvc** (standalone setup) | Send fake HTTP requests to REST controllers; assert status codes and JSON. |
| **Selenium layer** | JUnit 5 + `@Tag("selenium")` | Same framework; tests are **filtered** so normal `test` task skips them (see below). |

**Arrange–Act–Assert (AAA)** is followed everywhere: prepare data/mocks, invoke the method or HTTP call, assert outcomes.

---

## 2. What Is Selenium? Why Use It? How Do We Use It?

### What is Selenium?

**Selenium** is a **browser automation** toolkit. **Selenium WebDriver** drives a real browser (here: **Google Chrome**) to:

- Open pages (e.g. `http://localhost:3000`)
- Find elements (by CSS, XPath, etc.)
- Click, type, wait for navigation
- Assert URL or visible text

### Why use Selenium here?

1. **End-to-end confidence** — Login, redirects, and sidebar links are verified as a user would see them.
2. **Next.js + API integration** — Catches issues that unit tests miss (wrong URL, broken form, auth redirect).
3. **Role-based flows** — Each demo role lands on the correct dashboard path.

### How we use Selenium in this project

- **Dependencies** (in `backend/build.gradle`):
  - `org.seleniumhq.selenium:selenium-java`
  - `io.github.bonigarcia:webdrivermanager` — downloads/matches **ChromeDriver** automatically.
- **Base class**: `com.hms.selenium.SeleniumTestBase` — starts **headless Chrome**, defines `BASE_URL = http://localhost:3000`, helpers (`loginAs`, `waitForElement`, etc.).
- **Test classes**: `LoginPageTest`, `StudentDashboardTest`, `WardenDashboardTest`, `RoleBasedAccessTest` — each method annotated with `@Tag("selenium")`.
- **Gradle split**:
  - `./gradlew test` — **excludes** tag `selenium` (fast, no browser, no live servers required for those tests).
  - `./gradlew seleniumTest` — **only** runs `@Tag("selenium")` tests; **requires** backend on **8080** and frontend on **3000**.

---

## 3. Integrating Testing With the Project (Commands & Configuration)

### 3.1 Gradle dependencies (integration)

In `backend/build.gradle`:

- `testImplementation 'org.springframework.boot:spring-boot-starter-test'` — pulls JUnit 5, Mockito, AssertJ, MockMvc support, etc.
- `testImplementation 'org.springframework.security:spring-security-test'` — useful for security-related tests if extended later.
- `testRuntimeOnly 'com.h2database:h2'` — H2 is on the classpath for optional Spring Boot tests that activate `application-test.properties` (in-memory DB profile if you add `@ActiveProfiles("test")` to integration tests).
- `testImplementation` Selenium + WebDriverManager — UI tests.
- `tasks.named('test') { useJUnitPlatform { excludeTags 'selenium' } }` — default test run stays JVM-only.
- `tasks.register('seleniumTest', Test) { ... includeTags 'selenium' }` — dedicated UI test task.

### 3.2 Test resources

- `backend/src/test/resources/application-test.properties` — H2 URL, `ddl-auto=create-drop`, test JWT secret, `server.port=0` for any future `@SpringBootTest` usage.

### 3.3 Root-level scripts

- `run-tests.sh` (Git Bash / Linux / macOS)
- `run-tests.bat` (Windows)

They wrap Gradle so you run everything from the **repository root** (outside `backend/` and `frontend/`).

### 3.4 Commands you should know (viva)

| Goal | Command |
|------|---------|
| Run **all unit + controller** tests (no Selenium) | `cd backend && ./gradlew test` (Windows: `gradlew.bat test`) |
| Run **only Selenium** tests | Start backend (`bootRun`) and frontend (`npm run dev`), then `cd backend && ./gradlew seleniumTest` |
| Run **one test class** | `./gradlew test --tests "com.hms.service.impl.AuthServiceImplTest"` |
| Run **one test method** | `./gradlew test --tests "com.hms.service.impl.AuthServiceImplTest.login_validCredentials_returnsLoginResponse"` |
| Root script: unit tests only | `./run-tests.sh` or `run-tests.bat` |
| Root script: unit + Selenium | `./run-tests.sh --all` or `run-tests.bat --all` |

### 3.5 Manual test documentation

- `TEST_CASES.md` — structured **manual** scenarios (IDs, steps, expected results), not executed by Gradle.

---

## 4. How a Single Test Case Is Written (Step by Step)

### 4.1 Service unit test (Mockito + JUnit 5)

**Idea:** Test `AuthServiceImpl` without a database.

1. Annotate class with `@ExtendWith(MockitoExtension.class)`.
2. Declare `@Mock UserRepository userRepository` (and other dependencies).
3. Declare `@InjectMocks AuthServiceImpl authService` — Mockito injects mocks into the constructor.
4. In the test method:
   - **Arrange:** `when(userRepository.findByEmail(...)).thenReturn(Optional.of(user));`
   - **Act:** `LoginResponse r = authService.login(request);`
   - **Assert:** `assertEquals("jwt-token", r.getToken());`
5. For exceptions: `assertThrows(RuntimeException.class, () -> authService.login(...));`

**File reference:** `backend/src/test/java/com/hms/service/impl/AuthServiceImplTest.java`

### 4.2 Controller test (MockMvc + JUnit 5)

**Idea:** Test `HallController` HTTP mapping without starting the full Spring context.

1. `@Mock HallService service;` `@InjectMocks HallController controller;`
2. In `@BeforeEach`:  
   `mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();`
3. Perform request:  
   `mockMvc.perform(post("/api/halls").contentType(APPLICATION_JSON).content(json))`
4. Assert: `.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Tagore Hall"));`

**Why `GlobalExceptionHandler`?** So `ResourceNotFoundException` returns **404** JSON like production.

**JSON gotcha (viva-ready):** Lombok + Jackson on entities like `Hall` (`boolean isNew`) can expose **both** `new` and `isNew` in JSON. Tests send **both** `new` and `isNew` in request bodies where needed to avoid `null` mapping into primitive `boolean`.

**File reference:** `backend/src/test/java/com/hms/controller/HallControllerTest.java`

### 4.3 Selenium test (JUnit 5 + WebDriver)

1. Class extends `SeleniumTestBase`.
2. Class-level `@Tag("selenium")` so `test` task skips it.
3. Use `loginAs("student@hms.edu", "student123");` then `waitForUrlContains("/student");`
4. Find elements: `By.cssSelector("input[type='email']")`, `By.xpath("//a[contains(@href, '/student/dues')]")`, etc.

**File reference:** `backend/src/test/java/com/hms/selenium/LoginPageTest.java`

---

## 5. Test Cases by Module — What Was Written & Technical Details

Below, **“module”** maps to **backend domain** + **matching REST API** + **Selenium flows** where applicable.

### 5.1 Authentication (`/api/auth`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `AuthServiceImplTest` | Valid login → token + profile fields; wrong email/password; email lowercasing; `getCurrentUser` success + `ResourceNotFoundException`. |
| **Controller** `AuthControllerTest` | `POST /api/auth/login` 200 vs 401; `GET /api/auth/me` with `User` principal vs unauthenticated 401. Uses `GlobalExceptionHandler` + `UsernamePasswordAuthenticationToken` for `/me`. |

**Technical:** Mockito stubs `UserRepository`, `PasswordEncoder.matches`, `JwtUtil.generateToken`. No real JWT crypto validation in service tests — token string is mocked.

---

### 5.2 Business / reporting (`/api/business`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `BusinessServiceImplTest` | `admitStudent`: happy path, room missing, room occupied; `getStudentDues` with/without mess charges; `getMessManagerPayment` with/without manager; `getSalarySheet` leave deduction math; `getHallOccupancy` percentages + empty hall; `getOverallOccupancy`; `postATR` status → `RESOLVED`; `getAnnualStatement` grant/salary/expenditure/balance + year filter on expenditures. |
| **Controller** `BusinessControllerTest` | All endpoints return 200 with mocked DTOs; `getStudentDues` path for 404 via `ResourceNotFoundException`. |

**Technical:** Heavy use of `when(...).thenReturn(...)` on many repositories; `argThat` for verifying `save()` on `Complaint` (ATR + status). Occupancy test asserts rounded percentage (e.g. 66.67).

---

### 5.3 Students (`/api/students`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `StudentServiceImplTest` | CRUD + `getByHallId`; `getById` throws `ResourceNotFoundException`. |
| **Controller** `StudentControllerTest` | POST/GET/PUT/DELETE/GET-by-hall; 404 on missing id. |

**Technical:** Entity serialized with `ObjectMapper` where safe; exceptions translated to HTTP by `GlobalExceptionHandler`.

---

### 5.4 Complaints (`/api/complaints`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `ComplaintServiceImplTest` | CRUD; `getByHallId` / `getByStudentId`; update copies all fields including `atr`. |
| **Controller** `ComplaintControllerTest` | Full CRUD + hall/student filters; delete returns message body. |

---

### 5.5 Halls (`/api/halls`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `HallServiceImplTest` | CRUD; `getById` not found. |
| **Controller** `HallControllerTest` | POST/PUT use explicit JSON including `new` + `isNew` for boolean mapping; GET/DELETE. |

---

### 5.6 Rooms (`/api/rooms`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `RoomServiceImplTest` | CRUD + `getByHallId`; enum `RoomType`. |
| **Controller** `RoomControllerTest` | POST/PUT JSON includes `occupied` + `isOccupied`. |

---

### 5.7 Staff (`/api/staff`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `StaffServiceImplTest` | CRUD + `getByHallId`; `StaffType` enum. |
| **Controller** `StaffControllerTest` | Standard CRUD + hall listing. |

---

### 5.8 Staff leave (`/api/staff-leaves`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `StaffLeaveServiceImplTest` | CRUD + `getByStaffId`; `LocalDate` in entity. |
| **Controller** `StaffLeaveControllerTest` | `ObjectMapper` + `JavaTimeModule` for JSON dates. |

---

### 5.9 Payments (`/api/payments`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `PaymentServiceImplTest` | CRUD + `getByStudentId`; `LocalDate`. |
| **Controller** `PaymentControllerTest` | `JavaTimeModule` registered on mapper. |

---

### 5.10 Expenditures (`/api/expenditures`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `ExpenditureServiceImplTest` | CRUD + `getByHallId`; `ExpenseCategory`. |
| **Controller** `ExpenditureControllerTest` | JSON with enum + date. |

---

### 5.11 Grants (`/api/grants`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `GrantServiceImplTest` | CRUD. |
| **Controller** `GrantControllerTest` | CRUD HTTP contract. |

---

### 5.12 Hall–grant links (`/api/hall-grants`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `HallGrantServiceImplTest` | CRUD + `getByGrantId` / `getByHallId`. |
| **Controller** `HallGrantControllerTest` | CRUD + sub-paths `/grant/{id}`, `/hall/{id}`. |

---

### 5.13 Wardens (`/api/wardens`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `WardenServiceImplTest` | CRUD; `isControlling` boolean. |
| **Controller** `WardenControllerTest` | POST/PUT JSON includes `controlling` + `isControlling`. |

---

### 5.14 Mess managers (`/api/mess-managers`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `MessManagerServiceImplTest` | CRUD. |
| **Controller** `MessManagerControllerTest` | CRUD. |

---

### 5.15 Mess charges (`/api/mess-charges`)

| Artifact | Tests / focus |
|----------|----------------|
| **Service** `MessChargeServiceImplTest` | CRUD + `getByStudentId` + `getByHallIdAndMonthAndYear`. |
| **Controller** `MessChargeControllerTest` | Query params `month`, `year` on hall endpoint. |

---

### 5.16 Selenium — Frontend (Next.js) flows

| Class | What is verified |
|-------|-------------------|
| `LoginPageTest` | Login form visible; branding/demo section; demo button fills credentials; valid student/warden login redirects; invalid login shows error; password visibility toggle. |
| `StudentDashboardTest` | After login: dashboard text; sidebar mentions dues/complaints/payments; navigation to `/student/dues`, `/complaints`, `/payments`; sign out returns to login. |
| `WardenDashboardTest` | Navigation to students, complaints, occupancy, salary, expenditures, accounts. |
| `RoleBasedAccessTest` | Each demo role URL: `/student`, `/warden`, `/controlling-warden`, `/mess-manager`, `/clerk`, `/hmc`; unauthenticated `/student` shows login; HMC/mess/clerk dashboards contain expected keywords. |

**Technical:** Headless Chrome; explicit waits via `WebDriverWait` + `ExpectedConditions`; XPath/CSS locators; **fragile** if UI text changes — improvement would be adding `data-testid` in React and selecting by `By.cssSelector("[data-testid='...']")`.

---

## 6. How We “Completed” Testing (Project Narrative)

1. **Analysed** Spring Boot controllers, services, entities, security (JWT + `/api/auth/**` permitAll).
2. **Fixed** Gradle test dependencies to use **`spring-boot-starter-test`** (correct BOM-managed stack).
3. **Added** H2 + `application-test.properties` for future integration tests.
4. **Implemented** **15 × service** test classes (Mockito, no DB).
5. **Implemented** **15 × controller** test classes (MockMvc standalone + `GlobalExceptionHandler`).
6. **Implemented** **4 × Selenium** suites with a shared base class; **tagged** and **split** Gradle tasks.
7. **Documented** manual cases in `TEST_CASES.md` and user-facing commands in `README.md`.
8. **Automated** root scripts `run-tests.sh` / `run-tests.bat` for one-place execution.

---

## 7. Important Viva Questions & Concise Answers

### JUnit & general testing

1. **What is unit testing?**  
   Testing the smallest piece (e.g. one class/method) in isolation, with dependencies replaced by mocks/stubs.

2. **Why JUnit 5 over JUnit 4?**  
   Jupiter API, better extensions, parameterized tests, cleaner lifecycle, `@DisplayName` for readable reports.

3. **What is Arrange–Act–Assert?**  
   Setup → invoke behaviour → verify outcomes.

4. **What is Mockito?**  
   Library to create mock objects and define stubbed behaviour (`when`/`thenReturn`) and verify interactions (`verify`).

5. **Why mock repositories in service tests?**  
   To avoid MySQL, keep tests fast and deterministic, and to force specific scenarios (empty list, optional empty, etc.).

6. **What is MockMvc?**  
   Spring’s servlet-layer test client that simulates HTTP against controllers without starting a real server (in standalone mode, only registered controllers/advices apply).

7. **Why add `GlobalExceptionHandler` to standalone MockMvc?**  
   So exceptions map to the same HTTP status and JSON body as in the running app.

8. **Difference between `@Mock` and `@InjectMocks`?**  
   `@Mock` creates a fake dependency; `@InjectMocks` instantiates the class under test and injects those mocks into its constructor/fields.

### Spring Boot & REST

9. **Do our default Gradle tests start Spring Boot?**  
   No — service/controller tests use Mockito and **standalone** MockMvc; no `@SpringBootTest` in the current suite.

10. **How would you test with a real context?**  
    `@SpringBootTest` + `@AutoConfigureMockMvc` + test profile + H2, or Testcontainers for MySQL.

11. **What does `assertThrows` test?**  
    That a specific exception type is thrown for invalid input or missing entities.

### Selenium

12. **What is Selenium WebDriver?**  
    API to control a browser programmatically.

13. **Why WebDriverManager?**  
    Automatically resolves the correct ChromeDriver version for the installed Chrome.

14. **Why headless mode?**  
    Runs without a visible window — suitable for CI and local scripted runs.

15. **Why are Selenium tests excluded from `./gradlew test`?**  
    They need Chrome, network, and **live** backend/frontend; separating them keeps the default build fast and reliable.

16. **What is a flaky test?**  
    Sometimes passes, sometimes fails — often due to timing; mitigated with explicit waits (`WebDriverWait`), not only `Thread.sleep`.

### Domain-specific (HMS)

17. **What does `BusinessServiceImpl.admitStudent` test cover?**  
    Room existence, occupancy flag, student save, room marked occupied, dues response (rent + amenity).

18. **How is salary calculated in tests?**  
    `getSalarySheet` uses month length, leave days in range, `workingDays = totalDays - leaveDays`, `salary = workingDays * dailyPay`.

19. **What does `postATR` do in tests?**  
    Sets ATR text and complaint status to `RESOLVED`.

20. **Why did Hall/Room/Warden JSON need extra boolean properties in controller tests?**  
    Jackson + Lombok naming: primitive `boolean` fields must not receive implicit `null` from missing JSON properties; both logical JSON names may need to be supplied.

### Security

21. **Are APIs authenticated in MockMvc tests?**  
    Security filter chain is **not** applied in standalone setup unless you add it — these tests focus on controller + exception handling, not JWT filter behaviour.

22. **How is login security tested?**  
    `AuthServiceImplTest` checks password matching and user lookup; `AuthControllerTest` checks service delegation and `/me` principal type.

### Process & documentation

23. **Difference between automated tests and `TEST_CASES.md`?**  
    Automated = run by Gradle; manual doc = human-executable checklist with placeholders for actual results/status.

24. **How do you run one failing test in isolation?**  
    `./gradlew test --tests "fully.qualified.ClassName.methodName"`.

25. **What is CI-friendly order for this repo?**  
    Run `./gradlew test` on every commit; run `seleniumTest` optionally when full stack is up, or in a separate pipeline job with Chrome installed.

---

## 8. Quick File Map (for demo / viva)

```
backend/build.gradle                          ← test + seleniumTest tasks, dependencies
backend/src/test/resources/application-test.properties
backend/src/test/java/com/hms/service/impl/*Test.java    ← service unit tests
backend/src/test/java/com/hms/controller/*Test.java      ← MockMvc controller tests
backend/src/test/java/com/hms/selenium/*.java            ← UI tests + base class
run-tests.sh / run-tests.bat                  ← root runner
TEST_CASES.md                                 ← manual cases
README.md                                     ← how to run tests
```

---

*End of document — HMS testing viva preparation.*
