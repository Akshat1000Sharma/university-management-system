export function formatCurrency(amount: number): string {
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  }).format(amount);
}

export function formatDate(dateStr: string): string {
  if (!dateStr) return "-";
  return new Date(dateStr).toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}

export const MONTHS = [
  "January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December",
];

export function monthName(n: number): string {
  return MONTHS[n - 1] ?? String(n);
}

export function currentMonth() {
  return new Date().getMonth() + 1;
}

export function currentYear() {
  return new Date().getFullYear();
}

export function daysInMonth(month: number, year: number): number {
  return new Date(year, month, 0).getDate();
}

export const COMPLAINT_TYPES = [
  { value: "MAINTENANCE", label: "Maintenance" },
  { value: "FOOD_QUALITY", label: "Food Quality" },
  { value: "DISCIPLINE", label: "Discipline" },
  { value: "OTHER", label: "Other" },
];

export const STATUS_COLORS: Record<string, string> = {
  PENDING: "bg-amber-100 text-amber-800",
  IN_PROGRESS: "bg-blue-100 text-blue-800",
  RESOLVED: "bg-green-100 text-green-800",
  COMPLETED: "bg-green-100 text-green-800",
  FAILED: "bg-red-100 text-red-800",
};

export const STAFF_TYPES = [
  "ATTENDANT",
  "GARDENER",
  "WARDEN",
  "STAFF_ACCOUNTANT",
  "COMPOUNDER",
  "COOK",
  "CLEANER",
];

export const EXPENSE_CATEGORIES = [
  "MAINTENANCE",
  "FOOD",
  "UTILITIES",
  "STAFF",
  "OTHER",
  "REPAIR",
  "NEWSPAPER",
  "MAGAZINE",
];
