"use client";

import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import type { AuthUser, Role } from "./types";
import { clearStoredToken } from "./api";

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
    clearStoredToken();
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
  label: string;
}

export const DEMO_ACCOUNTS: DemoAccount[] = [
  { email: "student@hms.edu", password: "student123", role: "STUDENT", label: "Student" },
  { email: "student5@hms.edu", password: "student123", role: "STUDENT", label: "Student (North demo)" },
  { email: "student6@hms.edu", password: "student123", role: "STUDENT", label: "Student (North demo 2)" },
  { email: "student7@hms.edu", password: "student123", role: "STUDENT", label: "Student (North demo 3)" },
  { email: "warden@hms.edu", password: "warden123", role: "WARDEN", label: "Hall Warden" },
  { email: "cwarden@hms.edu", password: "cwarden123", role: "CONTROLLING_WARDEN", label: "Controlling Warden" },
  { email: "mess@hms.edu", password: "mess123", role: "MESS_MANAGER", label: "Mess Manager" },
  { email: "clerk@hms.edu", password: "clerk123", role: "CLERK", label: "Hall Clerk" },
  { email: "hmc@hms.edu", password: "hmc123", role: "HMC_CHAIRMAN", label: "HMC Chairman" },
];
