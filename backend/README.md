# HMS Backend - Spring Boot API

REST API backend for the Hall Management System built with Spring Boot 4, Spring Security (JWT), and MySQL.

## Requirements

- Java **21+**
- MySQL **8+** (running on port 3306)
- Gradle (wrapper included)

## Setup

### 1. Database Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hms_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

The database and all tables are created automatically on first run via Hibernate DDL.

### 2. Run the Application

```bash
# Linux / macOS
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

Server starts at `http://localhost:8080`.

### 3. Automatic Data Seeding

On first startup, the `DataInitializer` component checks if the `users` and `permissions` tables are empty and seeds:

- **12 user accounts** with BCrypt-hashed passwords (6 roles across 4 halls)
- **25 role-permission mappings** covering all system operations

### 4. Sample Domain Data (Optional)

After tables are created, load comprehensive sample data:

```bash
mysql -u root -p hms_db < sample_data.sql
```

This provides 4 halls, 32 rooms, 20 students, 18 staff members, complaints, mess charges, grants, expenditures, and payment records.

## Authentication

### JWT Flow

1. `POST /api/auth/login` with `{ email, password }`
2. Backend validates credentials with BCrypt
3. Returns JWT token (24h expiry) + user profile
4. All `/api/**` endpoints (except `/api/auth/**`) require `Authorization: Bearer <token>` header

### Configuration

```properties
# JWT secret key (min 256 bits for HS256)
jwt.secret=hms-university-management-system-secret-key-2026-production-grade-256bit-minimum

# Token validity: 24 hours
jwt.expiration-ms=86400000
```

### Security

- Stateless session management (no server-side sessions)
- BCrypt password hashing with default strength (10 rounds)
- CORS configured for `http://localhost:3000`
- Swagger/OpenAPI endpoints are publicly accessible

## API Endpoints

### Auth

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/login` | Authenticate and get JWT token |
| GET | `/api/auth/me` | Get current user profile (requires token) |

### CRUD Resources

All CRUD endpoints require a valid JWT token.

| Resource | Base Path |
|----------|-----------|
| Halls | `/api/halls` |
| Rooms | `/api/rooms` |
| Students | `/api/students` |
| Staff | `/api/staff` |
| Wardens | `/api/wardens` |
| Mess Managers | `/api/mess-managers` |
| Mess Charges | `/api/mess-charges` |
| Complaints | `/api/complaints` |
| Grants | `/api/grants` |
| Hall Grants | `/api/hall-grants` |
| Expenditures | `/api/expenditures` |
| Payments | `/api/payments` |
| Staff Leaves | `/api/staff-leaves` |

### Business Logic

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/business/admit` | Admit a student |
| GET | `/api/business/student/{id}/dues` | Get student dues |
| GET | `/api/business/mess-payment/hall/{id}` | Mess payment sheet |
| GET | `/api/business/salary-sheet/hall/{id}` | Staff salary sheet |
| GET | `/api/business/occupancy/hall/{id}` | Hall occupancy |
| GET | `/api/business/occupancy` | All halls occupancy |
| PUT | `/api/business/complaints/{id}/atr` | Post ATR on complaint |
| GET | `/api/business/annual-statement/hall/{id}` | Annual financial statement |

## Project Structure

```
src/main/java/com/hms/
├── HmsApplication.java           # Spring Boot entry point
├── config/
│   ├── SecurityConfig.java       # Spring Security + JWT filter chain
│   ├── JwtUtil.java              # JWT token generation & validation
│   ├── JwtAuthenticationFilter.java  # Per-request JWT authentication
│   └── DataInitializer.java      # Auto-seeds users & permissions on startup
├── controller/                   # 15 REST controllers
├── dto/                          # Request/response DTOs
├── entity/                       # 15 JPA entities
├── enums/                        # UserRole, StaffType, RoomType, etc.
├── exception/                    # Global exception handler
├── repository/                   # Spring Data JPA repositories
└── service/
    ├── *.java                    # Service interfaces
    └── impl/*.java               # Service implementations
```

## Roles & Permissions

| Role | Permissions |
|------|------------|
| STUDENT | VIEW_OWN_DUES, VIEW_OWN_COMPLAINTS, CREATE_COMPLAINT, VIEW_OWN_PAYMENTS |
| WARDEN | VIEW_HALL_STUDENTS, VIEW_HALL_COMPLAINTS, POST_ATR, VIEW_HALL_OCCUPANCY, MANAGE_HALL_STAFF, VIEW_SALARY_SHEET, MANAGE_EXPENDITURES, VIEW_ANNUAL_STATEMENT, ADMIT_STUDENT |
| CONTROLLING_WARDEN | VIEW_ALL_HALLS, VIEW_ALL_COMPLAINTS, VIEW_OVERALL_OCCUPANCY |
| MESS_MANAGER | MANAGE_MESS_CHARGES, VIEW_PAYMENT_SHEET |
| CLERK | MANAGE_STAFF_LEAVES, VIEW_SALARY_SHEET |
| HMC_CHAIRMAN | MANAGE_GRANTS, MANAGE_HALLS, VIEW_ALL_EXPENDITURES, VIEW_ALL_STUDENTS, ALLOCATE_GRANTS |

## Swagger UI

Access interactive API documentation at:

```
http://localhost:8080/swagger-ui.html
```
