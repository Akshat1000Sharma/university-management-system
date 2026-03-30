# HMS (Hall Management System) - Complete API Documentation

## Table of Contents
1. [API Overview](#api-overview)
2. [Server Setup](#server-setup)
3. [CRUD Operations](#crud-operations)
4. [Business Logic Endpoints](#business-logic-endpoints)
5. [Error Handling](#error-handling)
6. [Testing in Postman](#testing-in-postman)

---

## API Overview

**Base URL**: `http://localhost:8080/api`  
**Default Port**: 8080  
**Database**: MySQL (hms_db)

All endpoints return JSON responses. Authentication is disabled for development.

---

## Server Setup

### Start the Application
```bash
cd C:\Users\sande\Downloads\Downloads\HmsApplication\HmsApplication
.\gradlew.bat bootRun
```

**Expected Output**:
```
Tomcat initialized with port 8080 (http)
Started HmsApplication in X.XXX seconds (JVM running for X.XXX s)
```

### Verify Server is Running
```bash
curl http://localhost:8080/api/halls
```

---

## CRUD Operations

### 1. HALL MANAGEMENT

#### **Create Hall** (POST)
```
POST /api/halls
Content-Type: application/json

{
  "name": "New Hall",
  "isNew": true,
  "amenityCharge": 5000.0
}
```

**Response (201 Created)**:
```json
{
  "id": 5,
  "name": "New Hall",
  "isNew": true,
  "amenityCharge": 5000.0
}
```

#### **Get All Halls** (GET)
```
GET /api/halls
```

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "North Hall",
    "isNew": true,
    "amenityCharge": 5000.0
  },
  {
    "id": 2,
    "name": "South Hall",
    "isNew": false,
    "amenityCharge": 4000.0
  }
]
```

#### **Get Hall by ID** (GET)
```
GET /api/halls/1
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "North Hall",
  "isNew": true,
  "amenityCharge": 5000.0
}
```

#### **Update Hall** (PUT)
```
PUT /api/halls/1
Content-Type: application/json

{
  "name": "North Hall Updated",
  "isNew": true,
  "amenityCharge": 5500.0
}
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "North Hall Updated",
  "isNew": true,
  "amenityCharge": 5500.0
}
```

#### **Delete Hall** (DELETE)
```
DELETE /api/halls/1
```

**Response (200 OK)**:
```json
"Hall deleted"
```

---

### 2. ROOM MANAGEMENT

#### **Create Room** (POST)
```
POST /api/rooms
Content-Type: application/json

{
  "roomNumber": "101",
  "roomType": "SINGLE",
  "hallId": 1,
  "rentAmount": 5000.0
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "roomNumber": "101",
  "roomType": "SINGLE",
  "hallId": 1,
  "rentAmount": 5000.0
}
```

#### **Get All Rooms** (GET)
```
GET /api/rooms
```

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "roomNumber": "101",
    "roomType": "SINGLE",
    "hallId": 1,
    "rentAmount": 5000.0
  },
  {
    "id": 2,
    "roomNumber": "102",
    "roomType": "TWIN_SHARING",
    "hallId": 1,
    "rentAmount": 3000.0
  }
]
```

#### **Get Rooms by Hall** (GET)
```
GET /api/rooms/hall/1
```

#### **Update Room** (PUT)
```
PUT /api/rooms/1
Content-Type: application/json

{
  "roomNumber": "101A",
  "roomType": "SINGLE",
  "hallId": 1,
  "rentAmount": 5500.0
}
```

#### **Delete Room** (DELETE)
```
DELETE /api/rooms/1
```

---

### 3. STUDENT MANAGEMENT

#### **Create Student** (POST)
```
POST /api/students
Content-Type: application/json

{
  "name": "Amit Kumar",
  "email": "amit@example.com",
  "phone": "9876543210",
  "registrationNumber": "REG001",
  "hallId": 1,
  "roomId": 1,
  "admissionDate": "2026-01-15"
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "name": "Amit Kumar",
  "email": "amit@example.com",
  "phone": "9876543210",
  "registrationNumber": "REG001",
  "hallId": 1,
  "roomId": 1,
  "admissionDate": "2026-01-15"
}
```

#### **Get All Students** (GET)
```
GET /api/students
```

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Amit Kumar",
    "email": "amit@example.com",
    "phone": "9876543210",
    "registrationNumber": "REG001",
    "hallId": 1,
    "roomId": 1,
    "admissionDate": "2026-01-15"
  }
]
```

#### **Get Student by ID** (GET)
```
GET /api/students/1
```

#### **Get Students by Hall** (GET)
```
GET /api/students/hall/1
```

#### **Update Student** (PUT)
```
PUT /api/students/1
Content-Type: application/json

{
  "name": "Amit Kumar Singh",
  "email": "amit.singh@example.com",
  "phone": "9876543210",
  "registrationNumber": "REG001",
  "hallId": 1,
  "roomId": 1,
  "admissionDate": "2026-01-15"
}
```

#### **Delete Student** (DELETE)
```
DELETE /api/students/1
```

---

### 4. STAFF MANAGEMENT

#### **Create Staff** (POST)
```
POST /api/staff
Content-Type: application/json

{
  "name": "Rajesh Kumar",
  "staffType": "WARDEN",
  "dailyPay": 500.0,
  "hallId": 1
}
```

**Available Staff Types**: `WARDEN`, `STAFF_ACCOUNTANT`, `COMPOUNDER`, `COOK`, `CLEANER`

**Response (201 Created)**:
```json
{
  "id": 1,
  "name": "Rajesh Kumar",
  "staffType": "WARDEN",
  "dailyPay": 500.0,
  "hallId": 1
}
```

#### **Get All Staff** (GET)
```
GET /api/staff
```

#### **Get Staff by ID** (GET)
```
GET /api/staff/1
```

#### **Get Staff by Hall** (GET)
```
GET /api/staff/hall/1
```

#### **Update Staff** (PUT)
```
PUT /api/staff/1
Content-Type: application/json

{
  "name": "Rajesh Kumar Singh",
  "staffType": "WARDEN",
  "dailyPay": 550.0,
  "hallId": 1
}
```

#### **Delete Staff** (DELETE)
```
DELETE /api/staff/1
```

---

### 5. WARDEN MANAGEMENT

#### **Create Warden** (POST)
```
POST /api/wardens
Content-Type: application/json

{
  "name": "Dr. Sharma",
  "email": "sharma@university.edu",
  "phone": "9999888877",
  "hallId": 1
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "name": "Dr. Sharma",
  "email": "sharma@university.edu",
  "phone": "9999888877",
  "hallId": 1
}
```

#### **Get All Wardens** (GET)
```
GET /api/wardens
```

#### **Get Warden by Hall** (GET)
```
GET /api/wardens/hall/1
```

---

### 6. COMPLAINT MANAGEMENT

#### **Create Complaint** (POST)
```
POST /api/complaints
Content-Type: application/json

{
  "title": "Water Leakage in Room",
  "description": "Water is leaking from ceiling",
  "complaintType": "MAINTENANCE",
  "complaintStatus": "PENDING",
  "complaintDate": "2026-03-23",
  "studentId": 1,
  "hallId": 1
}
```

**Complaint Types**: `MAINTENANCE`, `FOOD_QUALITY`, `DISCIPLINE`, `OTHER`  
**Complaint Status**: `PENDING`, `IN_PROGRESS`, `RESOLVED`

**Response (201 Created)**:
```json
{
  "id": 1,
  "title": "Water Leakage in Room",
  "description": "Water is leaking from ceiling",
  "complaintType": "MAINTENANCE",
  "complaintStatus": "PENDING",
  "complaintDate": "2026-03-23",
  "studentId": 1,
  "hallId": 1
}
```

#### **Get All Complaints** (GET)
```
GET /api/complaints
```

#### **Get Complaint by ID** (GET)
```
GET /api/complaints/1
```

#### **Get Complaints by Hall** (GET)
```
GET /api/complaints/hall/1
```

#### **Get Complaints by Student** (GET)
```
GET /api/complaints/student/1
```

#### **Update Complaint** (PUT)
```
PUT /api/complaints/1
Content-Type: application/json

{
  "title": "Water Leakage in Room",
  "description": "Water is leaking from ceiling",
  "complaintType": "MAINTENANCE",
  "complaintStatus": "IN_PROGRESS",
  "complaintDate": "2026-03-23",
  "studentId": 1,
  "hallId": 1
}
```

#### **Delete Complaint** (DELETE)
```
DELETE /api/complaints/1
```

---

### 7. MESS MANAGER MANAGEMENT

#### **Create Mess Manager** (POST)
```
POST /api/mess-managers
Content-Type: application/json

{
  "name": "Priya Singh",
  "email": "priya@example.com",
  "phone": "8765432109",
  "hallId": 1,
  "experience": 5
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "name": "Priya Singh",
  "email": "priya@example.com",
  "phone": "8765432109",
  "hallId": 1,
  "experience": 5
}
```

#### **Get All Mess Managers** (GET)
```
GET /api/mess-managers
```

---

### 8. MESS CHARGE MANAGEMENT

#### **Create Mess Charge** (POST)
```
POST /api/mess-charges
Content-Type: application/json

{
  "amount": 2000.0,
  "month": 3,
  "year": 2026,
  "hallId": 1,
  "studentId": 1
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "amount": 2000.0,
  "month": 3,
  "year": 2026,
  "hallId": 1,
  "studentId": 1
}
```

#### **Get All Mess Charges** (GET)
```
GET /api/mess-charges
```

#### **Get Mess Charges by Student** (GET)
```
GET /api/mess-charges/student/1
```

---

### 9. GRANT MANAGEMENT

#### **Create Grant** (POST)
```
POST /api/grants
Content-Type: application/json

{
  "grantName": "Merit Scholarship",
  "totalAmount": 50000.0,
  "description": "For meritorious students"
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "grantName": "Merit Scholarship",
  "totalAmount": 50000.0,
  "description": "For meritorious students"
}
```

#### **Get All Grants** (GET)
```
GET /api/grants
```

---

### 10. HALL GRANT MANAGEMENT

#### **Create Hall Grant** (POST)
```
POST /api/hall-grants
Content-Type: application/json

{
  "allocatedAmount": 20000.0,
  "hallId": 1,
  "grantId": 1
}
```

#### **Get Hall Grants by Hall** (GET)
```
GET /api/hall-grants/hall/1
```

---

### 11. EXPENDITURE MANAGEMENT

#### **Create Expenditure** (POST)
```
POST /api/expenditures
Content-Type: application/json

{
  "expenseCategory": "MAINTENANCE",
  "amount": 5000.0,
  "date": "2026-03-23",
  "description": "Roof repair",
  "hallId": 1
}
```

**Expense Categories**: `MAINTENANCE`, `FOOD`, `UTILITIES`, `STAFF`, `OTHER`

#### **Get Expenditures by Hall** (GET)
```
GET /api/expenditures/hall/1
```

---

### 12. PAYMENT MANAGEMENT

#### **Create Payment** (POST)
```
POST /api/payments
Content-Type: application/json

{
  "paymentAmount": 10000.0,
  "paymentDate": "2026-03-23",
  "paymentStatus": "COMPLETED",
  "studentId": 1,
  "hallId": 1
}
```

**Payment Status**: `PENDING`, `COMPLETED`, `FAILED`

#### **Get All Payments** (GET)
```
GET /api/payments
```

#### **Get Payments by Student** (GET)
```
GET /api/payments/student/1
```

---

### 13. STAFF LEAVE MANAGEMENT

#### **Create Staff Leave** (POST)
```
POST /api/staff-leaves
Content-Type: application/json

{
  "leaveType": "CASUAL",
  "leaveDate": "2026-03-25",
  "staffId": 1,
  "hallId": 1
}
```

**Leave Types**: `CASUAL`, `EARNED`, `MEDICAL`, `SPECIAL`

#### **Get Staff Leaves by Staff** (GET)
```
GET /api/staff-leaves/staff/1
```

---

## Business Logic Endpoints

### 1. ADMIT STUDENT

**Purpose**: Admit a new student and assign a room automatically

```
POST /api/business/admit-student
Content-Type: application/json

{
  "name": "New Student",
  "email": "student@example.com",
  "phone": "9876543210",
  "registrationNumber": "REG999",
  "hallId": 1,
  "admissionDate": "2026-03-23"
}
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "New Student",
  "email": "student@example.com",
  "phone": "9876543210",
  "registrationNumber": "REG999",
  "hallId": 1,
  "roomId": 5,
  "admissionDate": "2026-03-23",
  "totalDue": 0.0
}
```

---

### 2. GET STUDENT DUES

**Purpose**: Calculate total dues (mess charges + room rent + amenity fee)

```
GET /api/business/student/1/dues?month=3&year=2026
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Amit Kumar",
  "hallId": 1,
  "roomId": 1,
  "messCharge": 2000.0,
  "roomRent": 5000.0,
  "amenityCharge": 5000.0,
  "totalDue": 12000.0,
  "month": 3,
  "year": 2026
}
```

---

### 3. GET MESS MANAGER PAYMENT

**Purpose**: Generate monthly payment sheet for mess manager

```
GET /api/business/hall/1/mess-payment?month=3&year=2026
```

**Response (200 OK)**:
```json
{
  "hallId": 1,
  "hallName": "North Hall",
  "messManagerId": 1,
  "messManagerName": "Priya Singh",
  "month": 3,
  "year": 2026,
  "totalStudents": 4,
  "monthlyCharge": 2000.0,
  "totalAmount": 8000.0,
  "paymentDate": "2026-03-23"
}
```

---

### 4. GET SALARY SHEET

**Purpose**: Generate staff salary with leave deductions

```
GET /api/business/hall/1/salary-sheet?month=3&year=2026
```

**Response (200 OK)**:
```json
[
  {
    "staffId": 1,
    "staffName": "Rajesh Kumar",
    "staffType": "WARDEN",
    "dailyPay": 500.0,
    "workingDays": 28,
    "leavesTaken": 2,
    "baseSalary": 14000.0,
    "deduction": 1000.0,
    "netSalary": 13000.0,
    "month": 3,
    "year": 2026
  }
]
```

---

### 5. GET HALL OCCUPANCY

**Purpose**: Get occupancy percentage of a single hall

```
GET /api/business/hall/1/occupancy
```

**Response (200 OK)**:
```json
{
  "hallId": 1,
  "hallName": "North Hall",
  "totalRooms": 20,
  "occupiedRooms": 18,
  "emptyRooms": 2,
  "occupancyPercentage": 90.0
}
```

---

### 6. GET OVERALL OCCUPANCY

**Purpose**: Get occupancy percentage of all halls

```
GET /api/business/overall-occupancy
```

**Response (200 OK)**:
```json
[
  {
    "hallId": 1,
    "hallName": "North Hall",
    "totalRooms": 20,
    "occupiedRooms": 18,
    "emptyRooms": 2,
    "occupancyPercentage": 90.0
  },
  {
    "hallId": 2,
    "hallName": "South Hall",
    "totalRooms": 25,
    "occupiedRooms": 20,
    "emptyRooms": 5,
    "occupancyPercentage": 80.0
  }
]
```

---

### 7. POST ACTION TAKEN REPORT (ATR)

**Purpose**: Update complaint status with action taken report

```
PUT /api/business/complaint/1/atr
Content-Type: application/json

{
  "atr": "Roof has been repaired. Water leakage issue resolved."
}
```

**Response (200 OK)**:
```json
"Action Taken Report posted successfully for complaint 1"
```

---

### 8. GET ANNUAL STATEMENT

**Purpose**: Generate annual financial statement for a hall

```
GET /api/business/hall/1/annual-statement?year=2026
```

**Response (200 OK)**:
```json
{
  "hallId": 1,
  "hallName": "North Hall",
  "year": 2026,
  "totalIncome": 250000.0,
  "totalExpenditure": 85000.0,
  "totalGrant": 50000.0,
  "netBalance": 215000.0,
  "incomeBreakdown": {
    "studentFeesCollected": 180000.0,
    "otherIncome": 20000.0
  },
  "expenditureBreakdown": {
    "staffSalaries": 50000.0,
    "maintenance": 20000.0,
    "utilities": 15000.0
  }
}
```

---

## Error Handling

### 404 - Resource Not Found
```json
{
  "status": 404,
  "message": "Resource not found",
  "timestamp": "2026-03-23T10:30:00"
}
```

### 400 - Bad Request
```json
{
  "status": 400,
  "message": "Invalid request data",
  "timestamp": "2026-03-23T10:30:00"
}
```

### 500 - Internal Server Error
```json
{
  "status": 500,
  "message": "Internal server error",
  "timestamp": "2026-03-23T10:30:00"
}
```

---

## Testing in Postman

### Step 1: Install Postman
Download from: https://www.postman.com/downloads/

### Step 2: Create New Collection
1. Click **Collections** → **Create New Collection**
2. Name: "HMS API"
3. Click **Create**

### Step 3: Add Requests

#### Example: Create Hall
1. Click **Add Request**
2. **Name**: "Create Hall"
3. **Method**: POST
4. **URL**: `http://localhost:8080/api/halls`
5. **Headers**:
   - Key: `Content-Type`
   - Value: `application/json`
6. **Body** (raw JSON):
```json
{
  "name": "East Hall",
  "isNew": true,
  "amenityCharge": 5000.0
}
```
7. Click **Send**

### Step 4: View Response
- Response body shows created hall with ID
- Status code: 201 (Created)

### Step 5: Chain Requests
1. Create a variable: Go to **Collection** → **Variables** → Add `base_url = http://localhost:8080/api`
2. Use in requests: `{{base_url}}/halls`
3. Save previous response: In **Tests** tab, add:
```javascript
var jsonData = pm.response.json();
pm.environment.set("hallId", jsonData.id);
```
4. Use in next request: `{{base_url}}/halls/{{hallId}}`

### Step 6: Test Scenarios

#### Test 1: Full Student Flow
1. POST `/halls` - Create hall
2. POST `/rooms` - Create room (use hallId from response)
3. POST `/students` - Create student (use hallId, roomId)
4. GET `/business/student/{studentId}/dues` - Get student dues
5. POST `/complaints` - File complaint
6. PUT `/complaints/{id}/atr` - Post action taken

#### Test 2: Financial Reports
1. GET `/business/hall/{hallId}/occupancy` - Hall occupancy
2. GET `/business/overall-occupancy` - All halls occupancy
3. GET `/business/hall/{hallId}/mess-payment` - Mess payment
4. GET `/business/hall/{hallId}/salary-sheet` - Salary sheet
5. GET `/business/hall/{hallId}/annual-statement` - Annual statement

### Step 7: Export Collection
1. Right-click collection → **Export**
2. Format: `Collection v2.1`
3. Save as `HMS_API.postman_collection.json`
4. Share with team members

---

## Quick PowerShell Testing

### Test 1: Create and List Halls
```powershell
# Create a hall
$hallData = @{
    name = "West Hall"
    isNew = $true
    amenityCharge = 5000
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/halls `
  -Headers @{"Content-Type"="application/json"} `
  -Body $hallData

# Get all halls
curl http://localhost:8080/api/halls
```

### Test 2: Student Admission and Dues
```powershell
# Admit student
$studentData = @{
    name = "Priya Sharma"
    email = "priya@example.com"
    phone = "9876543210"
    registrationNumber = "REG025"
    hallId = 1
    admissionDate = "2026-03-23"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/business/admit-student `
  -Headers @{"Content-Type"="application/json"} `
  -Body $studentData

# Get student dues (assuming studentId=1)
curl "http://localhost:8080/api/business/student/1/dues?month=3&year=2026"
```

### Test 3: Generate Reports
```powershell
# Get hall occupancy
curl "http://localhost:8080/api/business/hall/1/occupancy"

# Get all halls occupancy
curl "http://localhost:8080/api/business/overall-occupancy"

# Get mess payment sheet
curl "http://localhost:8080/api/business/hall/1/mess-payment?month=3&year=2026"

# Get staff salary sheet
curl "http://localhost:8080/api/business/hall/1/salary-sheet?month=3&year=2026"

# Get annual statement
curl "http://localhost:8080/api/business/hall/1/annual-statement?year=2026"
```

---

## Database Verification

### Connect to MySQL
```bash
mysql -h localhost -u root -p#Neeraj1402@ hms_db
```

### Check Tables
```sql
SHOW TABLES;
```

### View Sample Data
```sql
-- View all halls
SELECT * FROM hall;

-- View all students
SELECT * FROM student;

-- View all staff
SELECT * FROM staff;

-- View all complaints
SELECT * FROM complaint;

-- View student with dues calculation
SELECT s.id, s.name, r.rent_amount, m.amount as mess_charge, h.amenity_charge,
       (r.rent_amount + m.amount + h.amenity_charge) as total_due
FROM student s
LEFT JOIN room r ON s.room_id = r.id
LEFT JOIN mess_charge m ON s.id = m.student_id
LEFT JOIN hall h ON s.hall_id = h.id;
```

---

## Summary Table

| Entity | Create | Read | Update | Delete | Relationships |
|--------|--------|------|--------|--------|---------------|
| **Hall** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | ← Rooms, Students, Wardens, Staff |
| **Room** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | ← Students, hall → Hall |
| **Student** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Hall, Room, Complaints, Mess Charges, Payments |
| **Staff** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Hall, Staff Leaves |
| **Warden** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Hall |
| **Complaint** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Student, Hall |
| **Mess Manager** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Hall |
| **Mess Charge** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Student, Hall |
| **Grant** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | ← Hall Grants |
| **Hall Grant** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Hall, Grant |
| **Expenditure** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Hall |
| **Payment** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Student, Hall |
| **Staff Leave** | ✅ POST | ✅ GET | ✅ PUT | ✅ DELETE | → Staff, Hall |

---

## Common Issues & Solutions

### Issue: Port 8080 Already in Use
```powershell
taskkill /F /IM java.exe
```

### Issue: Database Connection Error
```bash
# Verify MySQL is running
mysql -h localhost -u root -p#Neeraj1402@

# Check database exists
SHOW DATABASES;
USE hms_db;
SHOW TABLES;
```

### Issue: API Returns 404
- Verify server is running: `curl http://localhost:8080/api/halls`
- Check correct endpoint path
- Verify resource ID exists in database

### Issue: POST Request Fails
- Verify `Content-Type: application/json` header
- Check JSON format is valid
- Ensure all required fields are included

---

## Next Steps

1. ✅ Start HMS server
2. ✅ Test CRUD endpoints in Postman
3. ✅ Test business logic endpoints
4. ✅ Generate sample reports
5. ✅ Verify database data
6. ✅ Share API documentation with team

---

**Last Updated**: March 23, 2026  
**HMS Version**: 1.0  
**Status**: Production Ready ✅
