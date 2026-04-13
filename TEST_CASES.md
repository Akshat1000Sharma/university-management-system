# HMS - Manual Test Cases Document

## Test Environment

| Item | Detail |
|------|--------|
| Application | Hall Management System (HMS) |
| Backend | Spring Boot 4 on port 8080 |
| Frontend | Next.js 16 on port 3000 |
| Database | MySQL 8+ (hms_db) |
| Browser | Chrome (latest) |

---

## 1. Authentication Module

### TC-AUTH-001: Login with Valid Credentials

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-AUTH-001 |
| **Feature/Module** | Authentication |
| **Preconditions** | Application running, demo accounts seeded |
| **Steps** | 1. Navigate to http://localhost:3000 <br> 2. Enter email: student@hms.edu <br> 3. Enter password: student123 <br> 4. Click "Sign in" |
| **Expected Result** | User is redirected to /student dashboard, JWT token stored in localStorage |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-AUTH-002: Login with Invalid Credentials

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-AUTH-002 |
| **Feature/Module** | Authentication |
| **Preconditions** | Application running |
| **Steps** | 1. Navigate to login page <br> 2. Enter email: wrong@hms.edu <br> 3. Enter password: wrongpass <br> 4. Click "Sign in" |
| **Expected Result** | Error message displayed: "Invalid email or password", user stays on login page |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-AUTH-003: Login with Empty Fields

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-AUTH-003 |
| **Feature/Module** | Authentication |
| **Preconditions** | Application running |
| **Steps** | 1. Navigate to login page <br> 2. Leave email and password empty <br> 3. Click "Sign in" |
| **Expected Result** | Browser validation prevents form submission (required fields) |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-AUTH-004: Demo Account Quick Fill

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-AUTH-004 |
| **Feature/Module** | Authentication |
| **Preconditions** | Login page loaded |
| **Steps** | 1. Click any demo account button <br> 2. Verify email and password fields are populated |
| **Expected Result** | Email and password inputs are filled with demo credentials |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-AUTH-005: Role-Based Redirect After Login

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-AUTH-005 |
| **Feature/Module** | Authentication |
| **Preconditions** | Application running with demo accounts |
| **Steps** | 1. Login as each role: Student→/student, Warden→/warden, Controlling Warden→/controlling-warden, Mess Manager→/mess-manager, Clerk→/clerk, HMC→/hmc |
| **Expected Result** | Each role redirects to correct dashboard path |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-AUTH-006: Logout

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-AUTH-006 |
| **Feature/Module** | Authentication |
| **Preconditions** | User is logged in |
| **Steps** | 1. Click "Sign out" button <br> 2. Verify redirect to login page <br> 3. Try accessing /student directly |
| **Expected Result** | User redirected to login, localStorage cleared, protected routes redirect to login |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-AUTH-007: GET /api/auth/me Returns User Profile

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-AUTH-007 |
| **Feature/Module** | Authentication API |
| **Preconditions** | Valid JWT token |
| **Steps** | 1. Call GET /api/auth/me with Authorization: Bearer <token> |
| **Expected Result** | Returns 200 with user profile (role, name, email, hallId) |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-AUTH-008: GET /api/auth/me Without Token Returns 401

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-AUTH-008 |
| **Feature/Module** | Authentication API |
| **Preconditions** | No authentication |
| **Steps** | 1. Call GET /api/auth/me without Authorization header |
| **Expected Result** | Returns 401 Unauthorized |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 2. Student Module

### TC-STU-001: View Student Dues

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-STU-001 |
| **Feature/Module** | Student - Dues |
| **Preconditions** | Logged in as student, sample data loaded |
| **Steps** | 1. Navigate to /student/dues <br> 2. Select month and year <br> 3. View breakdown |
| **Expected Result** | Displays mess charges, room rent, amenity charge, and total due |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-STU-002: File a Complaint

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-STU-002 |
| **Feature/Module** | Student - Complaints |
| **Preconditions** | Logged in as student |
| **Steps** | 1. Navigate to /student/complaints <br> 2. Click "New Complaint" or equivalent <br> 3. Fill in type, description <br> 4. Submit |
| **Expected Result** | Complaint created, appears in list with PENDING status |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-STU-003: View Payments

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-STU-003 |
| **Feature/Module** | Student - Payments |
| **Preconditions** | Logged in as student, payments exist |
| **Steps** | 1. Navigate to /student/payments <br> 2. View payment history |
| **Expected Result** | Payment records displayed with amount and date |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-STU-004: Make a Payment

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-STU-004 |
| **Feature/Module** | Student - Payments |
| **Preconditions** | Logged in as student |
| **Steps** | 1. Navigate to /student/payments <br> 2. Enter payment amount <br> 3. Submit |
| **Expected Result** | Payment recorded, appears in payment list |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 3. Warden Module

### TC-WAR-001: Admit Student

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-WAR-001 |
| **Feature/Module** | Warden - Students |
| **Preconditions** | Logged in as warden, vacant rooms available |
| **Steps** | 1. Navigate to /warden/students <br> 2. Click "Admit" <br> 3. Fill name, email, phone, room <br> 4. Submit |
| **Expected Result** | Student created, room marked as occupied, dues breakdown shown |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-WAR-002: Post ATR on Complaint

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-WAR-002 |
| **Feature/Module** | Warden - Complaints |
| **Preconditions** | Logged in as warden, pending complaints exist |
| **Steps** | 1. Navigate to /warden/complaints <br> 2. Select a pending complaint <br> 3. Enter ATR text <br> 4. Submit |
| **Expected Result** | Complaint status changes to RESOLVED, ATR text saved |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-WAR-003: View Room Occupancy

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-WAR-003 |
| **Feature/Module** | Warden - Occupancy |
| **Preconditions** | Logged in as warden |
| **Steps** | 1. Navigate to /warden/occupancy |
| **Expected Result** | Shows total rooms, occupied, vacant, occupancy percentage |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-WAR-004: View Salary Sheet

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-WAR-004 |
| **Feature/Module** | Warden - Salary |
| **Preconditions** | Logged in as warden, staff and leave data exists |
| **Steps** | 1. Navigate to /warden/salary <br> 2. Select month/year |
| **Expected Result** | Lists all staff with daily pay, leave days, working days, calculated salary |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-WAR-005: Add Expenditure

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-WAR-005 |
| **Feature/Module** | Warden - Expenditures |
| **Preconditions** | Logged in as warden |
| **Steps** | 1. Navigate to /warden/expenditures <br> 2. Click "Add" <br> 3. Fill description, amount, category <br> 4. Submit |
| **Expected Result** | Expenditure saved and appears in list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-WAR-006: Delete Expenditure

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-WAR-006 |
| **Feature/Module** | Warden - Expenditures |
| **Preconditions** | Logged in as warden, expenditure exists |
| **Steps** | 1. Navigate to /warden/expenditures <br> 2. Click delete on an expenditure |
| **Expected Result** | Expenditure removed from list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-WAR-007: View Annual Statement

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-WAR-007 |
| **Feature/Module** | Warden - Accounts |
| **Preconditions** | Logged in as warden, grants/expenditures/salaries exist |
| **Steps** | 1. Navigate to /warden/accounts <br> 2. Select year |
| **Expected Result** | Shows grant allocated, total salaries, total expenditures, balance |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-WAR-008: Add Staff

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-WAR-008 |
| **Feature/Module** | Warden - Staff |
| **Preconditions** | Logged in as warden |
| **Steps** | 1. Navigate to /warden/staff <br> 2. Click "Add Staff" <br> 3. Fill name, type, daily pay <br> 4. Submit |
| **Expected Result** | Staff member created and appears in list |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 4. Mess Manager Module

### TC-MM-001: Add Mess Charge

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-MM-001 |
| **Feature/Module** | Mess Manager - Charges |
| **Preconditions** | Logged in as mess manager |
| **Steps** | 1. Navigate to /mess-manager/charges <br> 2. Fill student, month, year, amount <br> 3. Submit |
| **Expected Result** | Mess charge created, appears in list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-MM-002: Delete Mess Charge

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-MM-002 |
| **Feature/Module** | Mess Manager - Charges |
| **Preconditions** | Logged in as mess manager, charge exists |
| **Steps** | 1. Navigate to /mess-manager/charges <br> 2. Click delete on a charge |
| **Expected Result** | Charge removed from list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-MM-003: View Payment Sheet

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-MM-003 |
| **Feature/Module** | Mess Manager - Payment Sheet |
| **Preconditions** | Logged in as mess manager, charges exist |
| **Steps** | 1. Navigate to /mess-manager/payment-sheet <br> 2. Select month/year |
| **Expected Result** | Shows total mess charges and manager payment summary |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 5. Clerk Module

### TC-CLK-001: Record Staff Leave

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CLK-001 |
| **Feature/Module** | Clerk - Leaves |
| **Preconditions** | Logged in as clerk, staff exists |
| **Steps** | 1. Navigate to /clerk/leaves <br> 2. Select staff member and leave date <br> 3. Submit |
| **Expected Result** | Leave recorded, appears in list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-CLK-002: Delete Staff Leave

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CLK-002 |
| **Feature/Module** | Clerk - Leaves |
| **Preconditions** | Logged in as clerk, leave exists |
| **Steps** | 1. Navigate to /clerk/leaves <br> 2. Click delete on a leave record |
| **Expected Result** | Leave removed from list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-CLK-003: View Salary Sheet

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CLK-003 |
| **Feature/Module** | Clerk - Salary |
| **Preconditions** | Logged in as clerk |
| **Steps** | 1. Navigate to /clerk/salary <br> 2. Select month/year |
| **Expected Result** | Salary sheet displays staff with calculated salaries |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 6. HMC Chairman Module

### TC-HMC-001: Create Grant

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-HMC-001 |
| **Feature/Module** | HMC - Grants |
| **Preconditions** | Logged in as HMC |
| **Steps** | 1. Navigate to /hmc/grants <br> 2. Fill year and total amount <br> 3. Submit |
| **Expected Result** | Grant created and appears in list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-HMC-002: Allocate Grant to Hall

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-HMC-002 |
| **Feature/Module** | HMC - Grants |
| **Preconditions** | Logged in as HMC, grant exists |
| **Steps** | 1. Navigate to /hmc/grants <br> 2. Select grant and hall <br> 3. Enter allocation amount <br> 4. Submit |
| **Expected Result** | Hall-grant link created with allocated amount |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-HMC-003: Create Hall

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-HMC-003 |
| **Feature/Module** | HMC - Halls |
| **Preconditions** | Logged in as HMC |
| **Steps** | 1. Navigate to /hmc/halls <br> 2. Fill hall name, amenity charge <br> 3. Submit |
| **Expected Result** | Hall created and appears in list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-HMC-004: Create Room in Hall

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-HMC-004 |
| **Feature/Module** | HMC - Halls |
| **Preconditions** | Logged in as HMC, hall exists |
| **Steps** | 1. Navigate to /hmc/halls <br> 2. Select hall <br> 3. Fill room number, type, rent <br> 4. Submit |
| **Expected Result** | Room created under hall |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-HMC-005: Delete Hall

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-HMC-005 |
| **Feature/Module** | HMC - Halls |
| **Preconditions** | Logged in as HMC, hall exists |
| **Steps** | 1. Navigate to /hmc/halls <br> 2. Click delete on a hall |
| **Expected Result** | Hall removed from list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-HMC-006: View All Expenditures

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-HMC-006 |
| **Feature/Module** | HMC - Expenditures |
| **Preconditions** | Logged in as HMC, expenditures exist |
| **Steps** | 1. Navigate to /hmc/expenditures |
| **Expected Result** | Lists all expenditures across all halls |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 7. Controlling Warden Module

### TC-CW-001: View Overall Occupancy

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CW-001 |
| **Feature/Module** | Controlling Warden - Occupancy |
| **Preconditions** | Logged in as controlling warden |
| **Steps** | 1. Navigate to /controlling-warden/occupancy |
| **Expected Result** | Shows occupancy data for all halls |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-CW-002: View All Complaints

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CW-002 |
| **Feature/Module** | Controlling Warden - Complaints |
| **Preconditions** | Logged in as controlling warden |
| **Steps** | 1. Navigate to /controlling-warden/complaints |
| **Expected Result** | Lists all complaints across all halls |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-CW-003: View All Halls

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CW-003 |
| **Feature/Module** | Controlling Warden - Halls |
| **Preconditions** | Logged in as controlling warden |
| **Steps** | 1. Navigate to /controlling-warden/halls |
| **Expected Result** | Lists all halls with details |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 8. Navigation and UI

### TC-NAV-001: Sidebar Navigation Works for All Roles

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-NAV-001 |
| **Feature/Module** | Navigation |
| **Preconditions** | Logged in as any role |
| **Steps** | 1. Click each sidebar navigation link <br> 2. Verify URL changes and page loads |
| **Expected Result** | All navigation links navigate to correct pages without errors |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-NAV-002: Mobile Responsive Sidebar

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-NAV-002 |
| **Feature/Module** | Navigation - Responsive |
| **Preconditions** | Logged in, browser viewport < 768px |
| **Steps** | 1. Resize browser to mobile width <br> 2. Verify hamburger menu appears <br> 3. Click hamburger to open sidebar <br> 4. Click a nav link |
| **Expected Result** | Sidebar opens/closes on mobile, navigation works |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 9. Validation and Error Handling

### TC-VAL-001: API Returns 404 for Non-Existent Resource

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-VAL-001 |
| **Feature/Module** | API Error Handling |
| **Preconditions** | Valid JWT token |
| **Steps** | 1. Call GET /api/students/999999 |
| **Expected Result** | Returns 404 with error message: "Student not found" |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-VAL-002: API Returns 401 for Expired/Invalid Token

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-VAL-002 |
| **Feature/Module** | API Security |
| **Preconditions** | Invalid or expired JWT token |
| **Steps** | 1. Call GET /api/students with invalid Authorization header |
| **Expected Result** | Returns 401 Unauthorized |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-VAL-003: Login Validation - Invalid Email Format

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-VAL-003 |
| **Feature/Module** | Authentication - Validation |
| **Preconditions** | Application running |
| **Steps** | 1. POST /api/auth/login with email: "notanemail" |
| **Expected Result** | Returns 400 with validation error for email format |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-VAL-004: Admit Student to Occupied Room

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-VAL-004 |
| **Feature/Module** | Business Logic - Validation |
| **Preconditions** | Room already occupied |
| **Steps** | 1. POST /api/business/admit with occupied roomId |
| **Expected Result** | Returns 500 with message: "Room is already occupied" |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## 10. CRUD Operations (API-Level)

### TC-CRUD-001: Create and Retrieve Hall

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CRUD-001 |
| **Feature/Module** | Halls CRUD |
| **Preconditions** | Valid JWT token |
| **Steps** | 1. POST /api/halls with {name, amenityCharge} <br> 2. GET /api/halls/{id} |
| **Expected Result** | Hall created (200), retrievable with same data |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-CRUD-002: Update Hall

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CRUD-002 |
| **Feature/Module** | Halls CRUD |
| **Preconditions** | Hall exists |
| **Steps** | 1. PUT /api/halls/{id} with updated name <br> 2. GET /api/halls/{id} |
| **Expected Result** | Hall updated, GET returns new data |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-CRUD-003: Delete Hall

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CRUD-003 |
| **Feature/Module** | Halls CRUD |
| **Preconditions** | Hall exists |
| **Steps** | 1. DELETE /api/halls/{id} <br> 2. GET /api/halls/{id} |
| **Expected Result** | Delete returns 200, subsequent GET returns 404 |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-CRUD-004: Create and List Complaints

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CRUD-004 |
| **Feature/Module** | Complaints CRUD |
| **Preconditions** | Valid JWT token, hall and student exist |
| **Steps** | 1. POST /api/complaints with {studentId, hallId, type, description} <br> 2. GET /api/complaints |
| **Expected Result** | Complaint created, appears in list |
| **Actual Result** | — |
| **Status** | Not Executed |

### TC-CRUD-005: Filter Complaints by Hall

| Field | Value |
|-------|-------|
| **Test Case ID** | TC-CRUD-005 |
| **Feature/Module** | Complaints - Filtering |
| **Preconditions** | Complaints exist across multiple halls |
| **Steps** | 1. GET /api/complaints/hall/{hallId} |
| **Expected Result** | Only complaints for the specified hall are returned |
| **Actual Result** | — |
| **Status** | Not Executed |

---

## Summary

| Module | Total Test Cases |
|--------|-----------------|
| Authentication | 8 |
| Student | 4 |
| Warden | 8 |
| Mess Manager | 3 |
| Clerk | 3 |
| HMC Chairman | 6 |
| Controlling Warden | 3 |
| Navigation & UI | 2 |
| Validation & Error Handling | 4 |
| CRUD Operations | 5 |
| **Total** | **46** |
