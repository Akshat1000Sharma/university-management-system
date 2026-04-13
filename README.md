# HMS - Hall Management System

A production-grade university hall management system with a **Spring Boot 4** backend and **Next.js 16** frontend. Manages student halls, complaints, finances, staff, and mess operations with JWT-based role-based access control.

## Architecture

```
university-management-system/
├── backend/          # Spring Boot 4 (Java 21, Gradle, MySQL)
├── frontend/         # Next.js 16 (React 19, TypeScript, Tailwind CSS 4)
└── README.md
```

## Features

| Module | Description |
|--------|-------------|
| **Authentication** | JWT-based login with BCrypt password hashing and role-based access control |
| **Student Portal** | View dues, file complaints, track payments |
| **Warden Dashboard** | Manage students, complaints (ATR), staff, expenditures, occupancy, salary sheets, annual statements |
| **Controlling Warden** | Cross-hall oversight: occupancy, complaints, hall management |
| **Mess Manager** | Manage monthly mess charges and payment sheets |
| **Hall Clerk** | Staff leave management and salary sheet generation |
| **HMC Chairman** | Grant management, hall/room administration, expenditure oversight |

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 4.0.4, Spring Security, Spring Data JPA |
| Auth | JWT (JJWT 0.12.6), BCrypt password encoding |
| Database | MySQL 8+ with Hibernate ORM |
| Frontend | Next.js 16, React 19, TypeScript 5 |
| Styling | Tailwind CSS 4, Lucide icons |
| API Docs | SpringDoc OpenAPI (Swagger UI) |

## Quick Start

### Prerequisites

- Java 21+
- Node.js 20+
- MySQL 8+ (running on port 3306)

### 1. Backend

```bash
cd backend

# Configure database credentials in src/main/resources/application.properties
# Default: root / Akshat@123

# Start the server (port 8080)
./gradlew bootRun
```

On first run, the application automatically:
- Creates the `hms_db` database if it doesn't exist
- Generates all tables via Hibernate DDL
- Seeds 12 demo user accounts with BCrypt-hashed passwords
- Seeds 25 role-permission mappings

### 2. Frontend

```bash
cd frontend
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) to access the login page.

### 3. Sample Data (Optional)

After the backend is running and tables are created:

```bash
mysql -u root -p hms_db < backend/sample_data.sql
```

This truncates all tables and inserts comprehensive sample data (4 halls, 32 rooms, 20 students, staff, complaints, mess charges, grants, expenditures, payments). Users and permissions are seeded by the application on startup.

## Demo Accounts

| Role | Email | Password |
|------|-------|----------|
| Student | student@hms.edu | student123 |
| Warden | warden@hms.edu | warden123 |
| Controlling Warden | cwarden@hms.edu | cwarden123 |
| Mess Manager | mess@hms.edu | mess123 |
| Clerk | clerk@hms.edu | clerk123 |
| HMC Chairman | hmc@hms.edu | hmc123 |

Additional accounts exist for multi-hall testing (student2-4@hms.edu, warden2-4@hms.edu).

## Authentication Flow

1. User submits email + password to `POST /api/auth/login`
2. Backend validates credentials against BCrypt-hashed passwords in the `users` table
3. On success, returns a JWT token + user profile (role, name, hallId, etc.)
4. Frontend stores the token in `localStorage` and includes it as `Authorization: Bearer <token>` on all API requests
5. Backend `JwtAuthenticationFilter` validates the token on every protected endpoint
6. On 401, the frontend clears the session and redirects to the login page

## API Documentation

Once the backend is running, access Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

See [backend/API_DOCUMENTATION.md](backend/API_DOCUMENTATION.md) for the full endpoint reference.

## Database Schema

15 tables managed by Hibernate:

| Table | Description |
|-------|-------------|
| `users` | Authentication accounts with BCrypt passwords and role assignments |
| `permissions` | Role-to-permission mappings (RBAC) |
| `halls` | University halls with amenity charges |
| `rooms` | Rooms per hall (single/twin-sharing with rent) |
| `students` | Student records linked to halls and rooms |
| `wardens` | Warden assignments to halls |
| `mess_managers` | Mess manager assignments |
| `staff` | Hall staff with daily pay rates |
| `staff_leaves` | Staff leave records |
| `mess_charges` | Monthly mess charges per student |
| `complaints` | Student complaints with ATR tracking |
| `grants` | Annual grant budgets |
| `hall_grants` | Grant allocations per hall |
| `expenditures` | Hall expenditure records |
| `payments` | Student payment records |

## Project Structure

### Backend (`backend/`)

```
src/main/java/com/hms/
├── config/           # SecurityConfig, JwtUtil, JwtAuthenticationFilter, DataInitializer
├── controller/       # REST controllers (14 + AuthController)
├── dto/              # Request/response DTOs
├── entity/           # JPA entities (13 domain + User, Permission)
├── enums/            # Domain enums + UserRole
├── exception/        # Global exception handler
├── repository/       # Spring Data JPA repositories
└── service/          # Service interfaces and implementations
```

### Frontend (`frontend/`)

```
app/
├── lib/              # API client, auth context, types, utilities
├── components/       # Shared UI components
├── (dashboard)/      # Protected dashboard routes
│   ├── student/      # Student pages
│   ├── warden/       # Warden pages
│   ├── controlling-warden/
│   ├── mess-manager/
│   ├── clerk/
│   └── hmc/          # HMC Chairman pages
└── page.tsx          # Login page
```

## Testing

The project includes comprehensive automated tests covering backend unit tests, controller tests, and frontend Selenium UI tests.

### Test Summary

| Category | Framework | Count | What's Covered |
|----------|-----------|-------|----------------|
| Service Unit Tests | JUnit 5 + Mockito | 15 test classes (~95 tests) | All 15 service implementations: CRUD operations, business logic, edge cases, error handling |
| Controller Tests | JUnit 5 + MockMvc | 15 test classes (~100 tests) | All 15 REST controllers: HTTP methods, status codes, JSON responses, 404 handling |
| Selenium UI Tests | Selenium + Chrome | 4 test classes (~30 tests) | Login flow, role-based routing, dashboard navigation, sign out, demo accounts |

### Running Backend Unit Tests

Backend unit tests use an **H2 in-memory database** — no MySQL or external services required.

```bash
cd backend
./gradlew test
```

On Windows:
```bash
cd backend
gradlew.bat test
```

Test reports are generated at `backend/build/reports/tests/test/index.html`.

### Running Selenium UI Tests

Selenium tests require both the **backend** (port 8080) and **frontend** (port 3000) to be running, plus Chrome browser installed.

```bash
# Terminal 1: Start backend
cd backend
./gradlew bootRun

# Terminal 2: Start frontend
cd frontend
npm run dev

# Terminal 3: Run Selenium tests
cd backend
./gradlew seleniumTest
```

### Run-All-Tests Script

A convenience script is provided at the project root to run all tests:

```bash
# Linux/Mac
chmod +x run-tests.sh
./run-tests.sh              # Backend unit tests only
./run-tests.sh --all        # Backend + Selenium tests
./run-tests.sh --selenium   # Selenium tests only

# Windows
run-tests.bat               # Backend unit tests only
run-tests.bat --all         # Backend + Selenium tests
run-tests.bat --selenium    # Selenium tests only
```

### Manual Test Cases

See [TEST_CASES.md](TEST_CASES.md) for a structured manual test case document covering 46 test scenarios across all modules.

### Test Architecture

```
backend/src/test/java/com/hms/
├── service/impl/          # Service layer unit tests (Mockito)
│   ├── AuthServiceImplTest.java
│   ├── BusinessServiceImplTest.java
│   ├── StudentServiceImplTest.java
│   ├── ComplaintServiceImplTest.java
│   ├── HallServiceImplTest.java
│   ├── RoomServiceImplTest.java
│   ├── StaffServiceImplTest.java
│   ├── PaymentServiceImplTest.java
│   ├── ExpenditureServiceImplTest.java
│   ├── GrantServiceImplTest.java
│   ├── WardenServiceImplTest.java
│   ├── MessManagerServiceImplTest.java
│   ├── MessChargeServiceImplTest.java
│   ├── HallGrantServiceImplTest.java
│   └── StaffLeaveServiceImplTest.java
├── controller/            # Controller layer tests (MockMvc)
│   ├── AuthControllerTest.java
│   ├── BusinessControllerTest.java
│   ├── StudentControllerTest.java
│   ├── ComplaintControllerTest.java
│   ├── HallControllerTest.java
│   ├── RoomControllerTest.java
│   ├── StaffControllerTest.java
│   ├── PaymentControllerTest.java
│   ├── ExpenditureControllerTest.java
│   ├── GrantControllerTest.java
│   ├── WardenControllerTest.java
│   ├── MessManagerControllerTest.java
│   ├── MessChargeControllerTest.java
│   ├── HallGrantControllerTest.java
│   └── StaffLeaveControllerTest.java
└── selenium/              # Selenium UI tests (Chrome WebDriver)
    ├── SeleniumTestBase.java
    ├── LoginPageTest.java
    ├── StudentDashboardTest.java
    ├── WardenDashboardTest.java
    └── RoleBasedAccessTest.java
```

## License

This project is for educational purposes as part of a university management systems course.
