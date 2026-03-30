-- HMS Database - Comprehensive Sample Data
-- Populates all 13 tables with rich data for every role/feature

-- Clear existing data (reverse dependency order)
DELETE FROM payments;
DELETE FROM mess_charges;
DELETE FROM staff_leaves;
DELETE FROM complaints;
DELETE FROM expenditures;
DELETE FROM hall_grants;
DELETE FROM grants;
DELETE FROM mess_managers;
DELETE FROM wardens;
DELETE FROM staff;
DELETE FROM students;
DELETE FROM rooms;
DELETE FROM halls;

-- Reset auto-increment counters
ALTER TABLE halls AUTO_INCREMENT = 1;
ALTER TABLE rooms AUTO_INCREMENT = 1;
ALTER TABLE students AUTO_INCREMENT = 1;
ALTER TABLE wardens AUTO_INCREMENT = 1;
ALTER TABLE mess_managers AUTO_INCREMENT = 1;
ALTER TABLE staff AUTO_INCREMENT = 1;
ALTER TABLE staff_leaves AUTO_INCREMENT = 1;
ALTER TABLE mess_charges AUTO_INCREMENT = 1;
ALTER TABLE complaints AUTO_INCREMENT = 1;
ALTER TABLE grants AUTO_INCREMENT = 1;
ALTER TABLE hall_grants AUTO_INCREMENT = 1;
ALTER TABLE expenditures AUTO_INCREMENT = 1;
ALTER TABLE payments AUTO_INCREMENT = 1;

-- ============================================================
-- 1. HALLS (4 halls)
-- ============================================================
INSERT INTO halls (name, is_new, amenity_charge) VALUES
('North Hall', true, 5000),
('South Hall', false, 4000),
('East Hall', true, 6000),
('West Hall', false, 3500);

-- ============================================================
-- 2. ROOMS (8 rooms per hall = 32 rooms total)
-- ============================================================
-- North Hall (hall_id=1) - 8 rooms
INSERT INTO rooms (room_number, room_type, rent, hall_id, is_occupied) VALUES
('N101', 'SINGLE', 15000, 1, true),
('N102', 'SINGLE', 15000, 1, true),
('N103', 'TWIN_SHARING', 12000, 1, true),
('N104', 'TWIN_SHARING', 12000, 1, true),
('N105', 'SINGLE', 15000, 1, true),
('N106', 'TWIN_SHARING', 12000, 1, false),
('N107', 'SINGLE', 15000, 1, false),
('N108', 'TWIN_SHARING', 12000, 1, false);

-- South Hall (hall_id=2) - 8 rooms
INSERT INTO rooms (room_number, room_type, rent, hall_id, is_occupied) VALUES
('S101', 'SINGLE', 14000, 2, true),
('S102', 'SINGLE', 14000, 2, true),
('S103', 'TWIN_SHARING', 11000, 2, true),
('S104', 'TWIN_SHARING', 11000, 2, true),
('S105', 'SINGLE', 14000, 2, true),
('S106', 'TWIN_SHARING', 11000, 2, false),
('S107', 'SINGLE', 14000, 2, false),
('S108', 'TWIN_SHARING', 11000, 2, false);

-- East Hall (hall_id=3) - 8 rooms
INSERT INTO rooms (room_number, room_type, rent, hall_id, is_occupied) VALUES
('E101', 'SINGLE', 16000, 3, true),
('E102', 'SINGLE', 16000, 3, true),
('E103', 'TWIN_SHARING', 13000, 3, true),
('E104', 'TWIN_SHARING', 13000, 3, true),
('E105', 'SINGLE', 16000, 3, false),
('E106', 'TWIN_SHARING', 13000, 3, false),
('E107', 'SINGLE', 16000, 3, false),
('E108', 'TWIN_SHARING', 13000, 3, false);

-- West Hall (hall_id=4) - 8 rooms
INSERT INTO rooms (room_number, room_type, rent, hall_id, is_occupied) VALUES
('W101', 'SINGLE', 13000, 4, true),
('W102', 'SINGLE', 13000, 4, true),
('W103', 'TWIN_SHARING', 10000, 4, true),
('W104', 'TWIN_SHARING', 10000, 4, true),
('W105', 'SINGLE', 13000, 4, false),
('W106', 'TWIN_SHARING', 10000, 4, false),
('W107', 'SINGLE', 13000, 4, false),
('W108', 'TWIN_SHARING', 10000, 4, false);

-- ============================================================
-- 3. STUDENTS (5-6 per hall = 20 total)
--    Demo student account maps to studentId=1, hallId=1
-- ============================================================
-- North Hall students (hall_id=1, room_ids 1-5)
INSERT INTO students (name, address, phone, photo, hall_id, room_id) VALUES
('Raj Kumar', '123 Main St, Delhi', '9876543210', 'raj.jpg', 1, 1),
('Vikram Gupta', '654 Maple Dr, Chennai', '9876543214', 'vikram.jpg', 1, 2),
('Siddharth Mehra', '45 Nehru Nagar, Lucknow', '9876543220', 'siddharth.jpg', 1, 3),
('Arjun Reddy', '78 MG Road, Hyderabad', '9876543221', 'arjun.jpg', 1, 4),
('Karan Malhotra', '12 Civil Lines, Jaipur', '9876543222', 'karan.jpg', 1, 5);

-- South Hall students (hall_id=2, room_ids 9-13)
INSERT INTO students (name, address, phone, photo, hall_id, room_id) VALUES
('Priya Singh', '456 Oak Ave, Mumbai', '9876543211', 'priya.jpg', 2, 9),
('Amit Patel', '789 Pine Rd, Bangalore', '9876543212', 'amit.jpg', 2, 10),
('Deepika Rao', '34 Park Street, Kolkata', '9876543223', 'deepika.jpg', 2, 11),
('Rahul Verma', '67 Gandhi Nagar, Patna', '9876543224', 'rahul.jpg', 2, 12),
('Meera Krishnan', '89 Anna Salai, Chennai', '9876543225', 'meera.jpg', 2, 13);

-- East Hall students (hall_id=3, room_ids 17-20)
INSERT INTO students (name, address, phone, photo, hall_id, room_id) VALUES
('Anjali Rao', '987 Cedar Ln, Pune', '9876543215', 'anjali.jpg', 3, 17),
('Rohit Sharma', '23 Residency Rd, Bangalore', '9876543226', 'rohit.jpg', 3, 18),
('Kavita Nair', '56 Marine Dr, Kochi', '9876543227', 'kavita.jpg', 3, 19),
('Suresh Iyer', '78 Connaught Place, Delhi', '9876543228', 'suresh.jpg', 3, 20),
('Pooja Deshmukh', '90 FC Road, Pune', '9876543229', 'pooja.jpg', 3, NULL);

-- West Hall students (hall_id=4, room_ids 25-28)
INSERT INTO students (name, address, phone, photo, hall_id, room_id) VALUES
('Neha Sharma', '321 Elm St, Hyderabad', '9876543213', 'neha.jpg', 4, 25),
('Aditya Joshi', '45 Linking Rd, Mumbai', '9876543230', 'aditya.jpg', 4, 26),
('Tanvi Kulkarni', '67 Brigade Rd, Bangalore', '9876543231', 'tanvi.jpg', 4, 27),
('Nikhil Saxena', '89 Hazratganj, Lucknow', '9876543232', 'nikhil.jpg', 4, 28),
('Shreya Ghosh', '12 Salt Lake, Kolkata', '9876543233', 'shreya.jpg', 4, NULL);

-- ============================================================
-- 4. WARDENS (4 wardens, one per hall)
--    Demo warden account = hallId=1 (Dr. Kumar)
-- ============================================================
INSERT INTO wardens (name, contact, hall_id, is_controlling) VALUES
('Dr. Kumar', '9898765432', 1, true),
('Prof. Sharma', '9898765433', 2, true),
('Mr. Patel', '9898765434', 3, false),
('Mrs. Singh', '9898765435', 4, true);

-- ============================================================
-- 5. MESS MANAGERS (one per hall)
--    Demo mess manager = hallId=1 (Rajesh Kumar)
-- ============================================================
INSERT INTO mess_managers (name, hall_id) VALUES
('Rajesh Kumar', 1),
('Suresh Singh', 2),
('Mohan Verma', 3),
('Dinesh Gupta', 4);

-- ============================================================
-- 6. STAFF (4-5 per hall, various types = 18 total)
-- ============================================================
-- North Hall staff (hall_id=1)
INSERT INTO staff (name, staff_type, daily_pay, hall_id) VALUES
('Ramesh Kumar', 'ATTENDANT', 500, 1),
('Pradeep Singh', 'ATTENDANT', 500, 1),
('Mohan Lal', 'GARDENER', 400, 1),
('Sunil Yadav', 'ATTENDANT', 450, 1),
('Gopal Das', 'GARDENER', 400, 1);

-- South Hall staff (hall_id=2)
INSERT INTO staff (name, staff_type, daily_pay, hall_id) VALUES
('Sanjay Verma', 'ATTENDANT', 500, 2),
('Rajiv Gupta', 'ATTENDANT', 500, 2),
('Vikram Joshi', 'GARDENER', 400, 2),
('Manoj Tiwari', 'ATTENDANT', 450, 2);

-- East Hall staff (hall_id=3)
INSERT INTO staff (name, staff_type, daily_pay, hall_id) VALUES
('Arun Kumar', 'ATTENDANT', 500, 3),
('Ashok Singh', 'GARDENER', 400, 3),
('Ravi Shankar', 'ATTENDANT', 480, 3),
('Deepak Mishra', 'GARDENER', 420, 3);

-- West Hall staff (hall_id=4)
INSERT INTO staff (name, staff_type, daily_pay, hall_id) VALUES
('Sunil Reddy', 'ATTENDANT', 500, 4),
('Naveen Kumar', 'GARDENER', 400, 4),
('Prakash Sahu', 'ATTENDANT', 460, 4),
('Kishore Nath', 'ATTENDANT', 470, 4),
('Balu Prasad', 'GARDENER', 410, 4);

-- ============================================================
-- 7. STAFF LEAVES (spread across Jan-Mar 2026 for salary calc)
-- ============================================================
-- January 2026 leaves
INSERT INTO staff_leaves (staff_id, leave_date) VALUES
(1, '2026-01-05'),
(1, '2026-01-12'),
(2, '2026-01-08'),
(3, '2026-01-15'),
(3, '2026-01-22'),
(4, '2026-01-10'),
(6, '2026-01-07'),
(6, '2026-01-14'),
(7, '2026-01-20'),
(10, '2026-01-18'),
(11, '2026-01-25'),
(14, '2026-01-06'),
(14, '2026-01-13'),
(15, '2026-01-28');

-- February 2026 leaves
INSERT INTO staff_leaves (staff_id, leave_date) VALUES
(1, '2026-02-03'),
(1, '2026-02-17'),
(2, '2026-02-10'),
(3, '2026-02-05'),
(4, '2026-02-12'),
(4, '2026-02-24'),
(5, '2026-02-09'),
(6, '2026-02-16'),
(7, '2026-02-23'),
(8, '2026-02-11'),
(9, '2026-02-18'),
(10, '2026-02-04'),
(10, '2026-02-25'),
(11, '2026-02-13'),
(12, '2026-02-20'),
(13, '2026-02-06'),
(14, '2026-02-15'),
(15, '2026-02-22'),
(16, '2026-02-08'),
(17, '2026-02-19'),
(18, '2026-02-26');

-- March 2026 leaves
INSERT INTO staff_leaves (staff_id, leave_date) VALUES
(1, '2026-03-01'),
(1, '2026-03-05'),
(1, '2026-03-19'),
(2, '2026-03-10'),
(2, '2026-03-24'),
(3, '2026-03-15'),
(4, '2026-03-20'),
(4, '2026-03-27'),
(5, '2026-03-11'),
(5, '2026-03-22'),
(6, '2026-03-08'),
(6, '2026-03-18'),
(7, '2026-03-12'),
(7, '2026-03-26'),
(8, '2026-03-04'),
(8, '2026-03-17'),
(9, '2026-03-09'),
(10, '2026-03-03'),
(10, '2026-03-13'),
(10, '2026-03-25'),
(11, '2026-03-07'),
(12, '2026-03-16'),
(12, '2026-03-28'),
(13, '2026-03-06'),
(13, '2026-03-21'),
(14, '2026-03-02'),
(14, '2026-03-14'),
(15, '2026-03-10'),
(15, '2026-03-23'),
(16, '2026-03-05'),
(16, '2026-03-19'),
(17, '2026-03-11'),
(17, '2026-03-25'),
(18, '2026-03-18');

-- ============================================================
-- 8. MESS CHARGES (Jan, Feb, Mar 2026 for every student)
-- ============================================================
-- January 2026
INSERT INTO mess_charges (student_id, hall_id, month, year, amount) VALUES
(1, 1, 1, 2026, 3000),
(2, 1, 1, 2026, 3000),
(3, 1, 1, 2026, 3000),
(4, 1, 1, 2026, 3000),
(5, 1, 1, 2026, 3000),
(6, 2, 1, 2026, 2800),
(7, 2, 1, 2026, 2800),
(8, 2, 1, 2026, 2800),
(9, 2, 1, 2026, 2800),
(10, 2, 1, 2026, 2800),
(11, 3, 1, 2026, 3200),
(12, 3, 1, 2026, 3200),
(13, 3, 1, 2026, 3200),
(14, 3, 1, 2026, 3200),
(15, 3, 1, 2026, 3200),
(16, 4, 1, 2026, 2500),
(17, 4, 1, 2026, 2500),
(18, 4, 1, 2026, 2500),
(19, 4, 1, 2026, 2500),
(20, 4, 1, 2026, 2500);

-- February 2026
INSERT INTO mess_charges (student_id, hall_id, month, year, amount) VALUES
(1, 1, 2, 2026, 3000),
(2, 1, 2, 2026, 3000),
(3, 1, 2, 2026, 3000),
(4, 1, 2, 2026, 3000),
(5, 1, 2, 2026, 3000),
(6, 2, 2, 2026, 2800),
(7, 2, 2, 2026, 2800),
(8, 2, 2, 2026, 2800),
(9, 2, 2, 2026, 2800),
(10, 2, 2, 2026, 2800),
(11, 3, 2, 2026, 3200),
(12, 3, 2, 2026, 3200),
(13, 3, 2, 2026, 3200),
(14, 3, 2, 2026, 3200),
(15, 3, 2, 2026, 3200),
(16, 4, 2, 2026, 2500),
(17, 4, 2, 2026, 2500),
(18, 4, 2, 2026, 2500),
(19, 4, 2, 2026, 2500),
(20, 4, 2, 2026, 2500);

-- March 2026
INSERT INTO mess_charges (student_id, hall_id, month, year, amount) VALUES
(1, 1, 3, 2026, 3000),
(2, 1, 3, 2026, 3000),
(3, 1, 3, 2026, 3000),
(4, 1, 3, 2026, 3000),
(5, 1, 3, 2026, 3000),
(6, 2, 3, 2026, 2800),
(7, 2, 3, 2026, 2800),
(8, 2, 3, 2026, 2800),
(9, 2, 3, 2026, 2800),
(10, 2, 3, 2026, 2800),
(11, 3, 3, 2026, 3200),
(12, 3, 3, 2026, 3200),
(13, 3, 3, 2026, 3200),
(14, 3, 3, 2026, 3200),
(15, 3, 3, 2026, 3200),
(16, 4, 3, 2026, 2500),
(17, 4, 3, 2026, 2500),
(18, 4, 3, 2026, 2500),
(19, 4, 3, 2026, 2500),
(20, 4, 3, 2026, 2500);

-- ============================================================
-- 9. COMPLAINTS (mix of types, statuses, across all halls)
-- ============================================================
-- North Hall complaints
INSERT INTO complaints (student_id, hall_id, type, description, status, atr) VALUES
(1, 1, 'FUSED_LIGHT', 'Tube light in room N101 is not working since 2 days', 'RESOLVED', 'Replaced tube light on 2026-03-21'),
(1, 1, 'WATER_TAP', 'Bathroom tap is dripping constantly', 'IN_PROGRESS', 'Plumber scheduled for tomorrow'),
(2, 1, 'ROOM_REPAIR', 'Window glass broken due to storm', 'PENDING', NULL),
(3, 1, 'ATTENDANT_BEHAVIOR', 'Attendant not cleaning corridor regularly', 'IN_PROGRESS', 'Warning issued to attendant'),
(4, 1, 'MESS_STAFF_BEHAVIOR', 'Food quality has degraded significantly this month', 'PENDING', NULL),
(5, 1, 'WATER_FILTER', 'Water filter on 1st floor needs servicing', 'RESOLVED', 'Filter cartridge replaced on 2026-03-18'),
(2, 1, 'FUSED_LIGHT', 'Emergency light in corridor not functioning', 'PENDING', NULL);

-- South Hall complaints
INSERT INTO complaints (student_id, hall_id, type, description, status, atr) VALUES
(6, 2, 'WATER_TAP', 'Hot water tap in bathroom leaking heavily', 'RESOLVED', 'Tap replaced on 2026-03-15'),
(7, 2, 'ROOM_REPAIR', 'Wall paint peeling off near window', 'IN_PROGRESS', 'Painter will visit this week'),
(8, 2, 'FUSED_LIGHT', 'Both lights in room S103 fused', 'PENDING', NULL),
(9, 2, 'WATER_FILTER', 'Water tastes metallic from the filter', 'IN_PROGRESS', 'Water sample sent for testing'),
(10, 2, 'ATTENDANT_BEHAVIOR', 'Washroom not cleaned for 3 days', 'RESOLVED', 'Stern warning given, cleaning resumed');

-- East Hall complaints
INSERT INTO complaints (student_id, hall_id, type, description, status, atr) VALUES
(11, 3, 'ROOM_REPAIR', 'Ceiling fan making loud noise', 'PENDING', NULL),
(12, 3, 'WATER_TAP', 'Basin tap handle broken', 'RESOLVED', 'New tap installed on 2026-02-28'),
(13, 3, 'MESS_STAFF_BEHAVIOR', 'Mess staff serving stale food', 'IN_PROGRESS', 'Mess committee meeting scheduled'),
(14, 3, 'FUSED_LIGHT', 'Study room lights flickering', 'PENDING', NULL);

-- West Hall complaints
INSERT INTO complaints (student_id, hall_id, type, description, status, atr) VALUES
(16, 4, 'WATER_FILTER', 'Water filter replacement overdue by 2 months', 'RESOLVED', 'Replaced filter on 2026-03-20'),
(17, 4, 'ROOM_REPAIR', 'Door lock is jammed and cannot be locked', 'IN_PROGRESS', 'Locksmith called'),
(18, 4, 'ATTENDANT_BEHAVIOR', 'Attendant rude to students', 'PENDING', NULL),
(19, 4, 'FUSED_LIGHT', 'Reading lamp socket sparking', 'RESOLVED', 'Electrician fixed wiring on 2026-03-22'),
(16, 4, 'WATER_TAP', 'Shower tap not working at all', 'PENDING', NULL);

-- ============================================================
-- 10. GRANTS (3 years)
-- ============================================================
INSERT INTO grants (year, total_amount) VALUES
(2024, 400000),
(2025, 450000),
(2026, 500000);

-- ============================================================
-- 11. HALL GRANTS (allocations for each grant-year to halls)
-- ============================================================
-- 2024 grants (grant_id=1)
INSERT INTO hall_grants (grant_id, hall_id, allocated_amount, spent_amount) VALUES
(1, 1, 120000, 115000),
(1, 2, 110000, 105000),
(1, 3, 100000, 92000),
(1, 4, 70000, 68000);

-- 2025 grants (grant_id=2)
INSERT INTO hall_grants (grant_id, hall_id, allocated_amount, spent_amount) VALUES
(2, 1, 140000, 125000),
(2, 2, 130000, 115000),
(2, 3, 110000, 98000),
(2, 4, 70000, 62000);

-- 2026 grants (grant_id=3)
INSERT INTO hall_grants (grant_id, hall_id, allocated_amount, spent_amount) VALUES
(3, 1, 150000, 95000),
(3, 2, 140000, 88000),
(3, 3, 130000, 75000),
(3, 4, 80000, 42000);

-- ============================================================
-- 12. EXPENDITURES (multiple categories, multiple months, all halls)
-- ============================================================
-- North Hall expenditures
INSERT INTO expenditures (hall_id, description, amount, date, category) VALUES
(1, 'Bathroom tiles replacement - ground floor', 25000, '2026-01-10', 'REPAIR'),
(1, 'Newspapers - Jan subscription', 2000, '2026-01-05', 'NEWSPAPER'),
(1, 'Magazine subscriptions - Jan', 1500, '2026-01-08', 'MAGAZINE'),
(1, 'Corridor painting', 18000, '2026-02-05', 'REPAIR'),
(1, 'Newspapers - Feb subscription', 2000, '2026-02-03', 'NEWSPAPER'),
(1, 'Magazine subscriptions - Feb', 1500, '2026-02-06', 'MAGAZINE'),
(1, 'Water pipe repair', 8000, '2026-02-20', 'REPAIR'),
(1, 'Bathroom tiles replacement - 1st floor', 22000, '2026-03-15', 'REPAIR'),
(1, 'Newspapers - Mar subscription', 2000, '2026-03-02', 'NEWSPAPER'),
(1, 'Magazine subscriptions - Mar', 1500, '2026-03-05', 'MAGAZINE'),
(1, 'Garden landscaping', 5000, '2026-03-20', 'OTHER'),
(1, 'Emergency electrical repair', 6500, '2026-03-25', 'REPAIR');

-- South Hall expenditures
INSERT INTO expenditures (hall_id, description, amount, date, category) VALUES
(2, 'Electrical wiring overhaul - Block A', 18000, '2026-01-12', 'REPAIR'),
(2, 'Newspapers - Jan subscription', 1800, '2026-01-04', 'NEWSPAPER'),
(2, 'Plumbing repair - bathrooms', 12000, '2026-02-10', 'REPAIR'),
(2, 'Newspapers - Feb subscription', 1800, '2026-02-02', 'NEWSPAPER'),
(2, 'Magazine subscriptions - Feb', 1200, '2026-02-07', 'MAGAZINE'),
(2, 'Cleaning supplies bulk purchase', 3000, '2026-03-08', 'OTHER'),
(2, 'Roof leak repair', 15000, '2026-03-14', 'REPAIR'),
(2, 'Newspapers - Mar subscription', 1800, '2026-03-03', 'NEWSPAPER'),
(2, 'Pest control treatment', 5000, '2026-03-22', 'OTHER');

-- East Hall expenditures
INSERT INTO expenditures (hall_id, description, amount, date, category) VALUES
(3, 'Wall painting - all rooms', 35000, '2026-01-20', 'REPAIR'),
(3, 'Newspapers - Jan subscription', 2200, '2026-01-06', 'NEWSPAPER'),
(3, 'Magazine subscriptions - Jan', 1800, '2026-01-09', 'MAGAZINE'),
(3, 'Furniture repair - common room', 8000, '2026-02-12', 'REPAIR'),
(3, 'Window replacement - 3rd floor', 12000, '2026-02-22', 'REPAIR'),
(3, 'Newspapers - Feb subscription', 2200, '2026-02-04', 'NEWSPAPER'),
(3, 'Drainage system repair', 10000, '2026-03-05', 'REPAIR'),
(3, 'Newspapers - Mar subscription', 2200, '2026-03-01', 'NEWSPAPER'),
(3, 'Magazine subscriptions - Mar', 1800, '2026-03-06', 'MAGAZINE'),
(3, 'Fire extinguisher refills', 3000, '2026-03-18', 'OTHER');

-- West Hall expenditures
INSERT INTO expenditures (hall_id, description, amount, date, category) VALUES
(4, 'Garden maintenance and planting', 5000, '2026-01-08', 'REPAIR'),
(4, 'Newspapers - Jan subscription', 1500, '2026-01-03', 'NEWSPAPER'),
(4, 'Floor polishing - corridors', 7000, '2026-02-08', 'REPAIR'),
(4, 'Newspapers - Feb subscription', 1500, '2026-02-01', 'NEWSPAPER'),
(4, 'Magazine subscriptions - Feb', 1000, '2026-02-05', 'MAGAZINE'),
(4, 'Staircase railing repair', 4500, '2026-03-03', 'REPAIR'),
(4, 'Newspapers - Mar subscription', 1500, '2026-03-02', 'NEWSPAPER'),
(4, 'Water tank cleaning', 3000, '2026-03-15', 'OTHER'),
(4, 'Electrical panel upgrade', 9000, '2026-03-20', 'REPAIR');

-- ============================================================
-- 13. PAYMENTS (multiple months for most students)
-- ============================================================
-- January 2026 payments
INSERT INTO payments (student_id, amount, date) VALUES
(1, 23000, '2026-01-10'),
(2, 23000, '2026-01-12'),
(3, 20000, '2026-01-11'),
(4, 20000, '2026-01-14'),
(5, 23000, '2026-01-13'),
(6, 20800, '2026-01-10'),
(7, 20800, '2026-01-11'),
(8, 17800, '2026-01-13'),
(9, 17800, '2026-01-15'),
(10, 17800, '2026-01-12'),
(11, 25200, '2026-01-10'),
(12, 25200, '2026-01-14'),
(13, 22200, '2026-01-12'),
(14, 22200, '2026-01-16'),
(16, 19000, '2026-01-10'),
(17, 19000, '2026-01-11'),
(18, 16000, '2026-01-14'),
(19, 16000, '2026-01-13');

-- February 2026 payments
INSERT INTO payments (student_id, amount, date) VALUES
(1, 23000, '2026-02-10'),
(2, 23000, '2026-02-12'),
(3, 20000, '2026-02-11'),
(4, 20000, '2026-02-15'),
(5, 23000, '2026-02-14'),
(6, 20800, '2026-02-10'),
(7, 20800, '2026-02-13'),
(8, 17800, '2026-02-12'),
(9, 17800, '2026-02-14'),
(10, 17800, '2026-02-11'),
(11, 25200, '2026-02-10'),
(12, 25200, '2026-02-15'),
(13, 22200, '2026-02-11'),
(14, 22200, '2026-02-14'),
(16, 19000, '2026-02-10'),
(17, 19000, '2026-02-12'),
(18, 16000, '2026-02-13'),
(19, 16000, '2026-02-15');

-- March 2026 payments
INSERT INTO payments (student_id, amount, date) VALUES
(1, 23000, '2026-03-10'),
(2, 23000, '2026-03-12'),
(3, 20000, '2026-03-11'),
(4, 20000, '2026-03-14'),
(5, 23000, '2026-03-13'),
(6, 20800, '2026-03-10'),
(7, 20800, '2026-03-11'),
(8, 17800, '2026-03-13'),
(9, 17800, '2026-03-15'),
(10, 17800, '2026-03-12'),
(11, 25200, '2026-03-10'),
(12, 25200, '2026-03-14'),
(13, 22200, '2026-03-12'),
(16, 19000, '2026-03-10'),
(17, 19000, '2026-03-11'),
(18, 16000, '2026-03-14');

-- ============================================================
-- Verification Queries
-- ============================================================
SELECT '--- HALLS ---' AS Info;
SELECT * FROM halls;

SELECT '--- ROOMS ---' AS Info;
SELECT COUNT(*) AS total_rooms FROM rooms;
SELECT hall_id, COUNT(*) AS rooms, SUM(is_occupied) AS occupied FROM rooms GROUP BY hall_id;

SELECT '--- STUDENTS ---' AS Info;
SELECT COUNT(*) AS total_students FROM students;
SELECT hall_id, COUNT(*) AS students FROM students GROUP BY hall_id;

SELECT '--- WARDENS ---' AS Info;
SELECT * FROM wardens;

SELECT '--- MESS MANAGERS ---' AS Info;
SELECT * FROM mess_managers;

SELECT '--- STAFF ---' AS Info;
SELECT hall_id, COUNT(*) AS staff_count FROM staff GROUP BY hall_id;

SELECT '--- STAFF LEAVES ---' AS Info;
SELECT COUNT(*) AS total_leaves FROM staff_leaves;

SELECT '--- MESS CHARGES ---' AS Info;
SELECT month, year, COUNT(*) AS charges FROM mess_charges GROUP BY month, year;

SELECT '--- COMPLAINTS ---' AS Info;
SELECT status, COUNT(*) AS count FROM complaints GROUP BY status;

SELECT '--- GRANTS ---' AS Info;
SELECT * FROM grants;

SELECT '--- HALL GRANTS ---' AS Info;
SELECT * FROM hall_grants;

SELECT '--- EXPENDITURES ---' AS Info;
SELECT hall_id, COUNT(*) AS count, SUM(amount) AS total FROM expenditures GROUP BY hall_id;

SELECT '--- PAYMENTS ---' AS Info;
SELECT MONTH(date) AS month, COUNT(*) AS count, SUM(amount) AS total FROM payments GROUP BY MONTH(date);
