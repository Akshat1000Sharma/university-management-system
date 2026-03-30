"use client";

import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import type { AuthUser, Role } from "./types";

interface AuthContextType {
  user: AuthUser | null;
  login: (user: AuthUser) => void;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType>({
  user: null,
  login: () => {},
  logout: () => {},
  isLoading: true,
});

const STORAGE_KEY = "hms_user";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      if (stored) {
        setUser(JSON.parse(stored));
      }
    } catch {
      // ignore
    } finally {
      setIsLoading(false);
    }
  }, []);

  const login = (u: AuthUser) => {
    setUser(u);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(u));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem(STORAGE_KEY);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);

export const ROLE_LABELS: Record<Role, string> = {
  STUDENT: "Student",
  WARDEN: "Hall Warden",
  CONTROLLING_WARDEN: "Controlling Warden",
  MESS_MANAGER: "Mess Manager",
  CLERK: "Hall Clerk",
  HMC_CHAIRMAN: "HMC Chairman",
};

export const ROLE_HOME: Record<Role, string> = {
  STUDENT: "/student",
  WARDEN: "/warden",
  CONTROLLING_WARDEN: "/controlling-warden",
  MESS_MANAGER: "/mess-manager",
  CLERK: "/clerk",
  HMC_CHAIRMAN: "/hmc",
};

interface DemoAccount {
  email: string;
  password: string;
  role: Role;
  name: string;
  hallId?: number;
  hallName?: string;
  studentId?: number;
  registrationNumber?: string;
}

export const DEMO_ACCOUNTS: DemoAccount[] = [
  { email: "student@hms.edu", password: "student123", role: "STUDENT", name: "Demo Student", hallId: 1, hallName: "North Hall", studentId: 1 },
  { email: "warden@hms.edu", password: "warden123", role: "WARDEN", name: "Dr. Kumar", hallId: 1, hallName: "North Hall" },
  { email: "cwarden@hms.edu", password: "cwarden123", role: "CONTROLLING_WARDEN", name: "Chief Warden" },
  { email: "mess@hms.edu", password: "mess123", role: "MESS_MANAGER", name: "Rajesh Kumar", hallId: 1, hallName: "North Hall" },
  { email: "clerk@hms.edu", password: "clerk123", role: "CLERK", name: "Hall Clerk", hallId: 1, hallName: "North Hall" },
  { email: "hmc@hms.edu", password: "hmc123", role: "HMC_CHAIRMAN", name: "HMC Chairman" },
];

export function validateLogin(email: string, password: string): AuthUser | null {
  const account = DEMO_ACCOUNTS.find(
    (a) => a.email.toLowerCase() === email.toLowerCase() && a.password === password
  );
  if (!account) return null;
  return {
    role: account.role,
    name: account.name,
    email: account.email,
    hallId: account.hallId,
    hallName: account.hallName,
    studentId: account.studentId,
    registrationNumber: account.registrationNumber,
  };
}
