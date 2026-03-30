export type Role =
  | "STUDENT"
  | "WARDEN"
  | "CONTROLLING_WARDEN"
  | "MESS_MANAGER"
  | "CLERK"
  | "HMC_CHAIRMAN";

export interface AuthUser {
  role: Role;
  name: string;
  email: string;
  hallId?: number;
  hallName?: string;
  studentId?: number;
  registrationNumber?: string;
}

export interface LoginResponse {
  token: string;
  role: Role;
  name: string;
  email: string;
  hallId?: number;
  hallName?: string;
  studentId?: number;
  registrationNumber?: string;
}

export interface Hall {
  id: number;
  name: string;
  isNew: boolean;
  amenityCharge: number;
}

export interface Room {
  id: number;
  roomNumber: string;
  roomType: "SINGLE" | "TWIN_SHARING";
  hallId: number;
  rent: number;
  occupied?: boolean;
}

export interface Student {
  id: number;
  name: string;
  email?: string;
  phone: string;
  registrationNumber: string;
  hallId: number;
  roomId?: number;
  admissionDate?: string;
  address?: string;
}

export interface Staff {
  id: number;
  name: string;
  staffType: "ATTENDANT" | "GARDENER" | "WARDEN" | "STAFF_ACCOUNTANT" | "COMPOUNDER" | "COOK" | "CLEANER";
  dailyPay: number;
  hallId: number;
}

export interface Warden {
  id: number;
  name: string;
  email?: string;
  phone: string;
  hallId: number;
  isControlling?: boolean;
}

export interface Complaint {
  id: number;
  title?: string;
  description: string;
  complaintType: "MAINTENANCE" | "FOOD_QUALITY" | "DISCIPLINE" | "OTHER" | "FUSED_LIGHT" | "WATER_TAP" | "WATER_FILTER" | "ROOM_REPAIR" | "ATTENDANT_BEHAVIOR" | "MESS_STAFF_BEHAVIOR";
  complaintStatus: "PENDING" | "IN_PROGRESS" | "RESOLVED";
  complaintDate?: string;
  atr?: string;
  studentId: number;
  hallId: number;
  type?: string;
  status?: string;
}

export interface MessManager {
  id: number;
  name: string;
  email?: string;
  phone?: string;
  hallId: number;
  experience?: number;
}

export interface MessCharge {
  id: number;
  amount: number;
  month: number;
  year: number;
  hallId: number;
  studentId: number;
}

export interface Grant {
  id: number;
  grantName?: string;
  totalAmount: number;
  description?: string;
  year?: number;
}

export interface HallGrant {
  id: number;
  allocatedAmount: number;
  spentAmount?: number;
  hallId: number;
  grantId: number;
}

export interface Expenditure {
  id: number;
  expenseCategory?: string;
  category?: string;
  amount: number;
  date: string;
  description: string;
  hallId: number;
}

export interface Payment {
  id: number;
  paymentAmount?: number;
  amount?: number;
  paymentDate?: string;
  date?: string;
  paymentStatus?: "PENDING" | "COMPLETED" | "FAILED";
  studentId: number;
  hallId?: number;
}

export interface StaffLeave {
  id: number;
  leaveType?: string;
  leaveDate: string;
  staffId: number;
  hallId?: number;
}

export interface StudentDues {
  studentId: number;
  studentName: string;
  messCharges: number;
  roomRent: number;
  amenityCharge: number;
  totalDue: number;
}

export interface MessPaymentSheet {
  hallId: number;
  hallName: string;
  messManagerId: number;
  messManagerName: string;
  month: number;
  year: number;
  totalStudents?: number;
  monthlyCharge?: number;
  totalAmount: number;
  paymentDate?: string;
}

export interface SalaryRecord {
  staffId: number;
  staffName: string;
  staffType: string;
  dailyPay: number;
  totalDays: number;
  leaveDays: number;
  workingDays: number;
  salary: number;
}

export interface HallOccupancy {
  hallId: number;
  hallName: string;
  totalRooms: number;
  occupiedRooms: number;
  vacantRooms: number;
  occupancyPercent: number;
}

export interface AnnualStatement {
  hallId: number;
  hallName: string;
  year: number;
  grantAllocated: number;
  totalSalariesPaid: number;
  totalExpenditures: number;
  totalBalance: number;
  salaryDetails?: SalaryRecord[];
}
