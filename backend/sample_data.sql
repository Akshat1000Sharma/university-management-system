-- HMS Database Sample Data
-- Insert sample data for testing the Hall Management System

-- 1. Insert Halls
INSERT INTO halls (name, is_new, amenity_charge) VALUES
('North Hall', true, 5000),
('South Hall', false, 4000),
('East Hall', true, 6000),
('West Hall', false, 3500);

-- 2. Insert Rooms
INSERT INTO rooms (room_number, room_type, rent, hall_id, is_occupied) VALUES
('101', 'SINGLE', 15000, 1, false),
('102', 'TWIN_SHARING', 12000, 1, false),
('103', 'SINGLE', 15000, 1, true),
('104', 'TWIN_SHARING', 12000, 1, false),
('201', 'SINGLE', 14000, 2, true),
('202', 'TWIN_SHARING', 11000, 2, false),
('203', 'SINGLE', 14000, 2, true),
('301', 'SINGLE', 16000, 3, false),
('302', 'TWIN_SHARING', 13000, 3, false),
('401', 'SINGLE', 13000, 4, true),
('402', 'TWIN_SHARING', 10000, 4, false);

-- 3. Insert Students
INSERT INTO students (name, address, phone, photo, hall_id, room_id) VALUES
('Raj Kumar', '123 Main St, Delhi', '9876543210', 'raj.jpg', 1, 3),
('Priya Singh', '456 Oak Ave, Mumbai', '9876543211', 'priya.jpg', 2, 5),
('Amit Patel', '789 Pine Rd, Bangalore', '9876543212', 'amit.jpg', 2, 7),
('Neha Sharma', '321 Elm St, Hyderabad', '9876543213', 'neha.jpg', 4, 10),
('Vikram Gupta', '654 Maple Dr, Chennai', '9876543214', 'vikram.jpg', 1, NULL),
('Anjali Rao', '987 Cedar Ln, Pune', '9876543215', 'anjali.jpg', 3, NULL);

-- 4. Insert Wardens
INSERT INTO wardens (name, contact, hall_id, is_controlling) VALUES
('Dr. Kumar', '9898765432', 1, true),
('Prof. Sharma', '9898765433', 2, true),
('Mr. Patel', '9898765434', 3, false),
('Mrs. Singh', '9898765435', 4, true);

-- 5. Insert Mess Managers
INSERT INTO mess_managers (name, hall_id) VALUES
('Rajesh Kumar', 1),
('Suresh Singh', 2),
('Mohan Verma', 3),
('Dinesh Gupta', 4);

-- 6. Insert Mess Charges
INSERT INTO mess_charges (student_id, hall_id, month, year, amount) VALUES
(1, 1, 3, 2026, 3000),
(1, 1, 2, 2026, 3000),
(2, 2, 3, 2026, 2800),
(2, 2, 2, 2026, 2800),
(3, 2, 3, 2026, 2800),
(4, 4, 3, 2026, 2500),
(4, 4, 2, 2026, 2500);

-- 7. Insert Staff
INSERT INTO staff (name, staff_type, daily_pay, hall_id) VALUES
('Ramesh Kumar', 'ATTENDANT', 500, 1),
('Pradeep Singh', 'ATTENDANT', 500, 1),
('Mohan Lal', 'GARDENER', 400, 1),
('Sanjay Verma', 'ATTENDANT', 500, 2),
('Rajiv Gupta', 'ATTENDANT', 500, 2),
('Vikram Joshi', 'GARDENER', 400, 2),
('Arun Kumar', 'ATTENDANT', 500, 3),
('Ashok Singh', 'GARDENER', 400, 3),
('Sunil Reddy', 'ATTENDANT', 500, 4),
('Naveen Kumar', 'GARDENER', 400, 4);

-- 8. Insert Staff Leaves
INSERT INTO staff_leaves (staff_id, leave_date) VALUES
(1, '2026-03-01'),
(1, '2026-03-05'),
(2, '2026-03-10'),
(3, '2026-03-15'),
(4, '2026-03-20'),
(5, '2026-03-22'),
(6, '2026-03-08'),
(7, '2026-03-12'),
(8, '2026-03-18'),
(9, '2026-03-25');

-- 9. Insert Complaints
INSERT INTO complaints (student_id, hall_id, type, description, status, atr) VALUES
(1, 1, 'FUSED_LIGHT', 'Bulb in room 103 is fused', 'RESOLVED', 'Replaced bulb on 2026-03-21'),
(2, 2, 'WATER_TAP', 'Tap in bathroom is leaking', 'IN_PROGRESS', 'Plumber called'),
(3, 2, 'ROOM_REPAIR', 'Wall paint peeling off', 'PENDING', NULL),
(4, 4, 'WATER_FILTER', 'Water filter needs replacement', 'RESOLVED', 'Replaced filter on 2026-03-20'),
(1, 1, 'ATTENDANT_BEHAVIOR', 'Attendant not maintaining cleanliness', 'PENDING', NULL),
(5, 1, 'MESS_STAFF_BEHAVIOR', 'Food quality is poor', 'IN_PROGRESS', 'Discussed with manager');

-- 10. Insert Grants
INSERT INTO grants (year, total_amount) VALUES
(2026, 500000),
(2025, 450000);

-- 11. Insert Hall Grants
INSERT INTO hall_grants (grant_id, hall_id, allocated_amount, spent_amount) VALUES
(1, 1, 150000, 95000),
(1, 2, 140000, 88000),
(1, 3, 130000, 75000),
(1, 4, 80000, 42000),
(2, 1, 140000, 125000),
(2, 2, 130000, 115000);

-- 12. Insert Expenditures
INSERT INTO expenditures (hall_id, description, amount, date, category) VALUES
(1, 'Bathroom tiles replacement', 25000, '2026-03-15', 'REPAIR'),
(1, 'Newspapers for library', 2000, '2026-03-20', 'NEWSPAPER'),
(1, 'Magazine subscriptions', 1500, '2026-03-18', 'MAGAZINE'),
(2, 'Electrical wiring repair', 18000, '2026-03-10', 'REPAIR'),
(2, 'Cleaning supplies', 3000, '2026-03-22', 'OTHER'),
(3, 'Paint for walls', 12000, '2026-03-05', 'REPAIR'),
(3, 'Furniture maintenance', 8000, '2026-03-12', 'OTHER'),
(4, 'Garden maintenance', 5000, '2026-03-08', 'REPAIR');

-- 13. Insert Payments
INSERT INTO payments (student_id, amount, date) VALUES
(1, 18000, '2026-03-15'),
(1, 18000, '2026-02-15'),
(2, 17800, '2026-03-10'),
(2, 17800, '2026-02-10'),
(3, 17800, '2026-03-12'),
(4, 15500, '2026-03-14'),
(4, 15500, '2026-02-14'),
(5, 20000, '2026-03-18'),
(6, 19000, '2026-03-20');

-- Verification Queries
SELECT '--- HALLS ---' AS Info;
SELECT * FROM halls;

SELECT '--- STUDENTS ---' AS Info;
SELECT * FROM students;

SELECT '--- ROOMS ---' AS Info;
SELECT * FROM rooms;

SELECT '--- STAFF ---' AS Info;
SELECT * FROM staff;

SELECT '--- COMPLAINTS ---' AS Info;
SELECT * FROM complaints;

SELECT '--- MESS CHARGES ---' AS Info;
SELECT * FROM mess_charges;

SELECT '--- PAYMENTS ---' AS Info;
SELECT * FROM payments;
