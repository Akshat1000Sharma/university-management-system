import type {
  Hall, Room, Student, Staff, Warden, Complaint, MessManager,
  MessCharge, Grant, HallGrant, Expenditure, Payment, StaffLeave,
  StudentDues, MessPaymentSheet, SalaryRecord, HallOccupancy, AnnualStatement
} from "./types";

const BASE = "/api";

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `HTTP ${res.status}`);
  }
  const text = await res.text();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  return (text ? JSON.parse(text) : null) as any;
}

// Halls
export const api = {
  halls: {
    getAll: () => request<Hall[]>("/halls"),
    getById: (id: number) => request<Hall>(`/halls/${id}`),
    create: (data: Omit<Hall, "id">) => request<Hall>("/halls", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<Hall, "id">) => request<Hall>(`/halls/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/halls/${id}`, { method: "DELETE" }),
  },

  rooms: {
    getAll: () => request<Room[]>("/rooms"),
    getByHall: (hallId: number) => request<Room[]>(`/rooms/hall/${hallId}`),
    create: (data: Omit<Room, "id">) => request<Room>("/rooms", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<Room, "id">) => request<Room>(`/rooms/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/rooms/${id}`, { method: "DELETE" }),
  },

  students: {
    getAll: () => request<Student[]>("/students"),
    getById: (id: number) => request<Student>(`/students/${id}`),
    getByHall: (hallId: number) => request<Student[]>(`/students/hall/${hallId}`),
    create: (data: Omit<Student, "id">) => request<Student>("/students", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<Student, "id">) => request<Student>(`/students/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/students/${id}`, { method: "DELETE" }),
  },

  staff: {
    getAll: () => request<Staff[]>("/staff"),
    getById: (id: number) => request<Staff>(`/staff/${id}`),
    getByHall: (hallId: number) => request<Staff[]>(`/staff/hall/${hallId}`),
    create: (data: Omit<Staff, "id">) => request<Staff>("/staff", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<Staff, "id">) => request<Staff>(`/staff/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/staff/${id}`, { method: "DELETE" }),
  },

  wardens: {
    getAll: () => request<Warden[]>("/wardens"),
    getByHall: (hallId: number) => request<Warden>(`/wardens/hall/${hallId}`),
    create: (data: Omit<Warden, "id">) => request<Warden>("/wardens", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<Warden, "id">) => request<Warden>(`/wardens/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/wardens/${id}`, { method: "DELETE" }),
  },

  complaints: {
    getAll: () => request<Complaint[]>("/complaints"),
    getById: (id: number) => request<Complaint>(`/complaints/${id}`),
    getByHall: (hallId: number) => request<Complaint[]>(`/complaints/hall/${hallId}`),
    getByStudent: (studentId: number) => request<Complaint[]>(`/complaints/student/${studentId}`),
    create: (data: Omit<Complaint, "id">) => request<Complaint>("/complaints", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Partial<Complaint>) => request<Complaint>(`/complaints/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/complaints/${id}`, { method: "DELETE" }),
    postAtr: (id: number, atr: string) => request<string>(`/business/complaint/${id}/atr`, { method: "PUT", body: JSON.stringify({ atr }) }),
  },

  messManagers: {
    getAll: () => request<MessManager[]>("/mess-managers"),
    create: (data: Omit<MessManager, "id">) => request<MessManager>("/mess-managers", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<MessManager, "id">) => request<MessManager>(`/mess-managers/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/mess-managers/${id}`, { method: "DELETE" }),
  },

  messCharges: {
    getAll: () => request<MessCharge[]>("/mess-charges"),
    getByStudent: (studentId: number) => request<MessCharge[]>(`/mess-charges/student/${studentId}`),
    create: (data: Omit<MessCharge, "id">) => request<MessCharge>("/mess-charges", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<MessCharge, "id">) => request<MessCharge>(`/mess-charges/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/mess-charges/${id}`, { method: "DELETE" }),
  },

  grants: {
    getAll: () => request<Grant[]>("/grants"),
    create: (data: Omit<Grant, "id">) => request<Grant>("/grants", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<Grant, "id">) => request<Grant>(`/grants/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/grants/${id}`, { method: "DELETE" }),
  },

  hallGrants: {
    getByHall: (hallId: number) => request<HallGrant[]>(`/hall-grants/hall/${hallId}`),
    getAll: () => request<HallGrant[]>("/hall-grants"),
    create: (data: Omit<HallGrant, "id">) => request<HallGrant>("/hall-grants", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<HallGrant, "id">) => request<HallGrant>(`/hall-grants/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  },

  expenditures: {
    getAll: () => request<Expenditure[]>("/expenditures"),
    getByHall: (hallId: number) => request<Expenditure[]>(`/expenditures/hall/${hallId}`),
    create: (data: Omit<Expenditure, "id">) => request<Expenditure>("/expenditures", { method: "POST", body: JSON.stringify(data) }),
    update: (id: number, data: Omit<Expenditure, "id">) => request<Expenditure>(`/expenditures/${id}`, { method: "PUT", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/expenditures/${id}`, { method: "DELETE" }),
  },

  payments: {
    getAll: () => request<Payment[]>("/payments"),
    getByStudent: (studentId: number) => request<Payment[]>(`/payments/student/${studentId}`),
    create: (data: Omit<Payment, "id">) => request<Payment>("/payments", { method: "POST", body: JSON.stringify(data) }),
  },

  staffLeaves: {
    getByStaff: (staffId: number) => request<StaffLeave[]>(`/staff-leaves/staff/${staffId}`),
    getAll: () => request<StaffLeave[]>("/staff-leaves"),
    create: (data: Omit<StaffLeave, "id">) => request<StaffLeave>("/staff-leaves", { method: "POST", body: JSON.stringify(data) }),
    delete: (id: number) => request<string>(`/staff-leaves/${id}`, { method: "DELETE" }),
  },

  business: {
    admitStudent: (data: {
      name: string; email: string; phone: string;
      registrationNumber: string; hallId: number; admissionDate: string;
    }) => request<Student>("/business/admit-student", { method: "POST", body: JSON.stringify(data) }),

    getStudentDues: (studentId: number, month: number, year: number) =>
      request<StudentDues>(`/business/student/${studentId}/dues?month=${month}&year=${year}`),

    getMessPayment: (hallId: number, month: number, year: number) =>
      request<MessPaymentSheet>(`/business/hall/${hallId}/mess-payment?month=${month}&year=${year}`),

    getSalarySheet: (hallId: number, month: number, year: number) =>
      request<SalaryRecord[]>(`/business/hall/${hallId}/salary-sheet?month=${month}&year=${year}`),

    getHallOccupancy: (hallId: number) =>
      request<HallOccupancy>(`/business/hall/${hallId}/occupancy`),

    getOverallOccupancy: () =>
      request<HallOccupancy[]>("/business/overall-occupancy"),

    getAnnualStatement: (hallId: number, year: number) =>
      request<AnnualStatement>(`/business/hall/${hallId}/annual-statement?year=${year}`),
  },
};
