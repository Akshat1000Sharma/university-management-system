

Here is your `PROBLEM_STATEMENT.md` file — save it in your project root:

```markdown
# Students' Hall Management System (HMS)

## Problem Statement

A student's hostel has requested that we develop the following software to automate various bookkeeping activities associated with its day-to-day operations.

---

### 1. Student Admission & Room Allotment

- After a student is admitted, he/she presents a note from the admissions unit, along with his/her **name**, **permanent address**, **contact telephone number**, and a **photograph**.
- He/she is then allotted a **hall** and a specific **room number**.
- A **letter confirming the allotted room** is issued to the student.

---

### 2. Mess Charges

- Students incur **mess charges each month**.
- The **mess manager** enters the total charges for each student on the mess account into the software.

---

### 3. Room Rent

- Each room has a **fixed room rent**.
- The newly constructed halls have **higher rent** than some of the older halls.
- **Twin-sharing rooms** have **lower rent**.

---

### 4. Amenity Charges

- Each hall provides certain **amenities** to students, such as:
  - Reading rooms
  - Playrooms
  - TV room
- A **fixed amount** is levied on each student for these amenities.

---

### 5. Mess Manager Monthly Payment

- The total amount collected from each student in a hall toward **mess charges** is handed over to the **mess manager each month**.
- The computer prints a **sheet showing the total amount due** to each mess manager.
- **Printed checks** are issued to each manager.
- **Signatures** are obtained from them on the sheet.

---

### 6. Student Dues Computation

- Whenever a student comes to pay his dues, his **total due** is computed as:

```
Total Due = Mess Charges + Amenity Charges + Room Rent
```

---

### 7. Complaint System

- Students should be able to raise **various types of complaints** using a **web browser** in their rooms or in the lab.
- **Repair requests:**
  - Fused lights
  - Non-functional water taps
  - Non-functional water filters
  - Room repair
- **Behavior complaints:**
  - Behavior of attendants
  - Behavior of mess staff
- **Round-the-clock operation** of the software is required.

---

### 8. HMC Annual Grant

- The **HMC** receives an **annual grant** from the Institute for:
  - Staff salaries
  - Upkeep of halls and gardens
- The **HMC chairman** should be provided with support for the **distribution of the grant** among the different halls.
- The **wardens** of the different halls should be able to enter their **expenditure details** against the allocations.

---

### 9. Room Occupancy

- The **controlling warden** should be able to view the **overall room occupancy**.
- The **warden of each hall** should be able to view the **occupancy of his hall**.

---

### 10. Complaints & ATR (Action Taken Report)

- The warden of each hall should be able to:
  - **View the complaints** raised by students.
  - **Post his Action Taken Report (ATR)** for each complaint.

---

### 11. Staff Management & Salary

- The halls employ **attendants** and **gardeners**.
- These temporary employees receive a **fixed pay on a per-day basis**.
- The **hall clerk** enters any **leave taken** by an attendant or a gardener from the terminal located at the hall office.
- At the end of each month:
  - A **consolidated list of salary payable** to each employee of the hall is printed.
  - **Checks for each employee** are printed.

```
Monthly Salary = (Total Days in Month - Leave Days) × Daily Pay
```

---

### 12. Petty Expenses

- The HMC incurs **petty expenses**, such as:
  - Repair work
  - Newspaper subscriptions
  - Magazine subscriptions
- It should be possible to **enter these expenses**.

---

### 13. Staff Recruitment & Deletion

- Whenever **new staff is recruited**, his details including his **daily pay** are entered.
- Whenever a **staff member leaves**, it should be possible to **delete his records**.

---

### 14. Statement of Accounts

- The **warden** should be able to **view the statement of accounts** at any time.
- The warden takes a **printout of the annual consolidated statement of accounts**.
- He **signs and submits** it to the **Institute administration** for:
  - Approval
  - Audit verification

---

## Summary of Users / Roles

| Role                 | Key Responsibilities                                          |
|----------------------|---------------------------------------------------------------|
| **Student**          | View dues, raise complaints                                   |
| **Mess Manager**     | Enter monthly mess charges per student                        |
| **Hall Clerk**       | Enter staff leave records                                     |
| **Warden**           | View occupancy, view complaints, post ATR, view accounts      |
| **Controlling Warden** | View overall room occupancy across all halls               |
| **HMC Chairman**     | Distribute annual grant among halls                           |

---

## Summary of Key Computations

| Computation            | Formula                                                  |
|------------------------|----------------------------------------------------------|
| **Student Total Due**  | `Mess Charges + Room Rent + Amenity Charges`             |
| **Staff Monthly Salary** | `(Days in Month - Leave Days) × Daily Pay`             |
| **Mess Manager Payment** | `SUM of all student mess charges in that hall for month`|
| **Room Occupancy %**   | `(Occupied Rooms / Total Rooms) × 100`                   |
| **Annual Balance**     | `Grant Allocated - Salaries Paid - Expenditures`         |

---
```

Save this as `PROBLEM_STATEMENT.md` in your project root folder `hms/`.