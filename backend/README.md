# README (VS Code) — HMS Spring Boot (Gradle + MySQL)

This README tells you **exactly which folders/files to create** and **where to paste each code block** you received.

---

## 0) Requirements

- Java **17**
- MySQL running
- VS Code Extensions:
  - **Extension Pack for Java**
  - **Spring Boot Extension Pack** (recommended)

---

## 1) Create Project Folder Structure

Create a folder like:

```
hms/
```

Inside it, you will have:

```
hms
├─ build.gradle
├─ settings.gradle
├─ gradlew
├─ gradlew.bat
├─ gradle/wrapper/...
└─ src
   └─ main
      ├─ java
      │  └─ com
      │     └─ hms
      │        ├─ HmsApplication.java
      │        ├─ controller
      │        ├─ dto
      │        ├─ entity
      │        ├─ enums
      │        ├─ exception
      │        ├─ repository
      │        ├─ service
      │        └─ service/impl
      └─ resources
         └─ application.properties
```

---

## 2) Best Way to Create the Project (So Gradle Wrapper Exists)

### Option A (Recommended): Spring Initializr
1. Go to https://start.spring.io
2. Project: **Gradle - Groovy**
3. Language: Java
4. Spring Boot: **3.2.x**
5. Group: `com.hms`
6. Artifact: `hms`
7. Java: **17**
8. Dependencies:
   - Spring Web
   - Spring Data JPA
   - Validation
   - MySQL Driver
   - Lombok
9. Generate → unzip → open the folder in VS Code.
10. Replace your generated `build.gradle` and `application.properties` with the ones I gave you.

### Option B: If you already have the folder and no wrapper
Run in terminal inside `hms/`:
```bash
gradle wrapper
```

---

## 3) Create These Files and Paste Code

Below is a **file-by-file checklist**. Create each file exactly with same name and package path, then paste the code.

---

# A) Root Gradle Files

## ✅ `build.gradle`
**Path:** `hms/build.gradle`  
Paste the **build.gradle** code block you got.

## ✅ `settings.gradle`
**Path:** `hms/settings.gradle`

Create this file:

```gradle
rootProject.name = 'hms'
```

---

# B) Application Config

## ✅ `application.properties`
**Path:** `hms/src/main/resources/application.properties`  
Paste the properties code you got.

> Change DB username/password if needed:
```properties
spring.datasource.username=root
spring.datasource.password=root
```

Also ensure MySQL DB name is `hms_db`.

---

# C) Main Application Class

## ✅ `HmsApplication.java`
**Path:** `hms/src/main/java/com/hms/HmsApplication.java`  
Paste the main class code you got.

---

# D) Enums

Create folder:
`hms/src/main/java/com/hms/enums/`

Create these files:

1) **RoomType.java**  
`src/main/java/com/hms/enums/RoomType.java`

2) **ComplaintType.java**  
`src/main/java/com/hms/enums/ComplaintType.java`

3) **ComplaintStatus.java**  
`src/main/java/com/hms/enums/ComplaintStatus.java`

4) **StaffType.java**  
`src/main/java/com/hms/enums/StaffType.java`

5) **ExpenseCategory.java**  
`src/main/java/com/hms/enums/ExpenseCategory.java`

Paste each enum code into its matching file.

---

# E) Entities

Create folder:
`hms/src/main/java/com/hms/entity/`

Create these files (paste entity code into each):

1) `Hall.java`  
2) `Room.java`  
3) `Student.java`  
4) `Warden.java`  
5) `MessManager.java`  
6) `MessCharge.java`  
7) `Complaint.java`  
8) `Staff.java`  
9) `StaffLeave.java`  
10) `Grant.java`  
11) `HallGrant.java`  
12) `Expenditure.java`  
13) `Payment.java`

---

# F) Repositories

Create folder:
`hms/src/main/java/com/hms/repository/`

Create these repository files:

1) `HallRepository.java`  
2) `RoomRepository.java`  
3) `StudentRepository.java`  
4) `WardenRepository.java`  
5) `MessManagerRepository.java`  
6) `MessChargeRepository.java`  
7) `ComplaintRepository.java`  
8) `StaffRepository.java`  
9) `StaffLeaveRepository.java`  ✅ (use the UPDATED version that has `findByStaffIdAndLeaveDateBetween`)
10) `GrantRepository.java`  
11) `HallGrantRepository.java`  
12) `ExpenditureRepository.java`  
13) `PaymentRepository.java`

---

# G) Exceptions

Create folder:
`hms/src/main/java/com/hms/exception/`

Create:

1) `ResourceNotFoundException.java`  
2) `GlobalExceptionHandler.java`

Paste exception code.

---

# H) Services (Interfaces)

Create folder:
`hms/src/main/java/com/hms/service/`

Create these interface files:

1) `HallService.java`  
2) `RoomService.java`  
3) `StudentService.java`  
4) `WardenService.java`  
5) `MessManagerService.java`  
6) `MessChargeService.java`  
7) `ComplaintService.java`  
8) `StaffService.java`  
9) `StaffLeaveService.java`  
10) `GrantService.java`  
11) `HallGrantService.java`  
12) `ExpenditureService.java`  
13) `PaymentService.java`  
14) `BusinessService.java` ✅ (Part 2)

---

# I) Service Implementations

Create folder:
`hms/src/main/java/com/hms/service/impl/`

Create these class files:

1) `HallServiceImpl.java`  
2) `RoomServiceImpl.java`  
3) `StudentServiceImpl.java`  
4) `WardenServiceImpl.java`  
5) `MessManagerServiceImpl.java`  
6) `MessChargeServiceImpl.java`  
7) `ComplaintServiceImpl.java`  
8) `StaffServiceImpl.java`  
9) `StaffLeaveServiceImpl.java`  
10) `GrantServiceImpl.java`  
11) `HallGrantServiceImpl.java`  
12) `ExpenditureServiceImpl.java`  
13) `PaymentServiceImpl.java`  
14) `BusinessServiceImpl.java` ✅ (Part 2)

Paste code into each.

---

# J) DTOs (Part 2)

Create folder:
`hms/src/main/java/com/hms/dto/`

Create these files:

1) `StudentDueResponse.java`  
2) `MessManagerPaymentResponse.java`  
3) `StaffSalaryResponse.java`  
4) `OccupancyResponse.java`  
5) `AnnualStatementResponse.java`  
6) `AdmitStudentRequest.java`

Paste DTO code.

---

# K) Controllers

Create folder:
`hms/src/main/java/com/hms/controller/`

Create these controller files:

**CRUD controllers:**
1) `HallController.java`  
2) `RoomController.java`  
3) `StudentController.java`  
4) `WardenController.java`  
5) `MessManagerController.java`  
6) `MessChargeController.java`  
7) `ComplaintController.java`  
8) `StaffController.java`  
9) `StaffLeaveController.java`  
10) `GrantController.java`  
11) `HallGrantController.java`  
12) `ExpenditureController.java`  
13) `PaymentController.java`

**Business logic controller (Part 2):**
14) `BusinessController.java`

Paste code into each.

---

## 4) Run the App

Open terminal in the project root (`hms/`) and run:

```bash
./gradlew bootRun
```

Windows:
```bat
gradlew.bat bootRun
```

Server runs on:
```
http://localhost:8080
```

---

## 5) MySQL Tables Auto-Creation

Because of:
```properties
spring.jpa.hibernate.ddl-auto=update
```

Tables will auto-create in schema:
```
hms_db
```

---

## 6) Quick Test URLs

Example CRUD:
- `GET http://localhost:8080/api/halls`

Business endpoints:
- Admit student:
  - `POST http://localhost:8080/api/business/admit`
- Student dues:
  - `GET http://localhost:8080/api/business/student/1/dues?month=3&year=2026`

---

## 7) Common Problems

### Lombok not working
In VS Code:
- Install Lombok support (usually included in Java extension pack)
- Ensure annotation processing enabled (VS Code Java settings)

### DB connection error
- Make sure MySQL is running
- Change `username/password` in `application.properties`

---

If you want, paste your current folder tree screenshot (or list) and I’ll tell you if anything is missing or placed in the wrong path.