"use client";

import { useEffect, useState } from "react";
import { useRouter, usePathname } from "next/navigation";
import Link from "next/link";
import {
  Building2, LayoutDashboard, MessageSquare, CreditCard,
  Users, BarChart3, FileText, Settings, LogOut, ChevronDown,
  Utensils, Calendar, DollarSign, Gift, Home, Menu, X, Wallet
} from "lucide-react";
import { useAuth, ROLE_LABELS } from "../lib/auth";
import type { Role } from "../lib/types";

type NavItem = {
  label: string;
  href: string;
  icon: React.ReactNode;
};

const NAV_ITEMS: Record<Role, NavItem[]> = {
  STUDENT: [
    { label: "Dashboard", href: "/student", icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "My Dues", href: "/student/dues", icon: <CreditCard className="w-4 h-4" /> },
    { label: "Complaints", href: "/student/complaints", icon: <MessageSquare className="w-4 h-4" /> },
    { label: "Payments", href: "/student/payments", icon: <Wallet className="w-4 h-4" /> },
  ],
  WARDEN: [
    { label: "Dashboard", href: "/warden", icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "Students", href: "/warden/students", icon: <Users className="w-4 h-4" /> },
    { label: "Complaints & ATR", href: "/warden/complaints", icon: <MessageSquare className="w-4 h-4" /> },
    { label: "Room Occupancy", href: "/warden/occupancy", icon: <Home className="w-4 h-4" /> },
    { label: "Staff Management", href: "/warden/staff", icon: <Users className="w-4 h-4" /> },
    { label: "Salary Sheet", href: "/warden/salary", icon: <DollarSign className="w-4 h-4" /> },
    { label: "Expenditures", href: "/warden/expenditures", icon: <FileText className="w-4 h-4" /> },
    { label: "Annual Statement", href: "/warden/accounts", icon: <BarChart3 className="w-4 h-4" /> },
  ],
  CONTROLLING_WARDEN: [
    { label: "Dashboard", href: "/controlling-warden", icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "Overall Occupancy", href: "/controlling-warden/occupancy", icon: <BarChart3 className="w-4 h-4" /> },
    { label: "All Complaints", href: "/controlling-warden/complaints", icon: <MessageSquare className="w-4 h-4" /> },
    { label: "All Halls", href: "/controlling-warden/halls", icon: <Building2 className="w-4 h-4" /> },
  ],
  MESS_MANAGER: [
    { label: "Dashboard", href: "/mess-manager", icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "Mess Charges", href: "/mess-manager/charges", icon: <Utensils className="w-4 h-4" /> },
    { label: "Payment Sheet", href: "/mess-manager/payment-sheet", icon: <DollarSign className="w-4 h-4" /> },
  ],
  CLERK: [
    { label: "Dashboard", href: "/clerk", icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "Staff Leaves", href: "/clerk/leaves", icon: <Calendar className="w-4 h-4" /> },
    { label: "Salary Sheet", href: "/clerk/salary", icon: <DollarSign className="w-4 h-4" /> },
  ],
  HMC_CHAIRMAN: [
    { label: "Dashboard", href: "/hmc", icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "Grants", href: "/hmc/grants", icon: <Gift className="w-4 h-4" /> },
    { label: "Halls & Rooms", href: "/hmc/halls", icon: <Building2 className="w-4 h-4" /> },
    { label: "Expenditures", href: "/hmc/expenditures", icon: <DollarSign className="w-4 h-4" /> },
    { label: "All Students", href: "/hmc/students", icon: <Users className="w-4 h-4" /> },
  ],
};

const ROLE_COLORS: Record<Role, string> = {
  STUDENT: "bg-blue-600",
  WARDEN: "bg-emerald-600",
  CONTROLLING_WARDEN: "bg-purple-600",
  MESS_MANAGER: "bg-orange-600",
  CLERK: "bg-teal-600",
  HMC_CHAIRMAN: "bg-rose-600",
};

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  const { user, logout, isLoading } = useAuth();
  const router = useRouter();
  const pathname = usePathname();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  useEffect(() => {
    if (!isLoading && !user) {
      router.replace("/");
    }
  }, [user, isLoading, router]);

  if (isLoading || !user) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  const navItems = NAV_ITEMS[user.role];
  const roleColor = ROLE_COLORS[user.role];

  const handleLogout = () => {
    logout();
    router.push("/");
  };

  const Sidebar = () => (
    <aside className="flex flex-col h-full bg-white border-r border-slate-200">
      {/* Logo */}
      <div className="flex items-center gap-2.5 px-5 py-4 border-b border-slate-100">
        <div className={`${roleColor} rounded-lg p-1.5`}>
          <Building2 className="w-5 h-5 text-white" />
        </div>
        <div>
          <p className="text-sm font-bold text-slate-900 leading-none">HMS</p>
          <p className="text-xs text-slate-400 mt-0.5">Hall Management</p>
        </div>
      </div>

      {/* User info */}
      <div className="px-5 py-3 border-b border-slate-100">
        <div className={`${roleColor} text-white rounded-lg px-3 py-2`}>
          <p className="text-xs font-semibold opacity-80">{ROLE_LABELS[user.role]}</p>
          <p className="text-sm font-bold truncate">{user.name}</p>
          {user.hallName && (
            <p className="text-xs opacity-70 truncate">{user.hallName}</p>
          )}
        </div>
      </div>

      {/* Nav items */}
      <nav className="flex-1 px-3 py-3 space-y-0.5 overflow-y-auto">
        {navItems.map((item) => {
          const isActive = pathname === item.href;
          return (
            <Link
              key={item.href}
              href={item.href}
              onClick={() => setSidebarOpen(false)}
              className={`flex items-center gap-2.5 px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                isActive
                  ? "bg-indigo-50 text-indigo-700"
                  : "text-slate-600 hover:bg-slate-50 hover:text-slate-900"
              }`}
            >
              <span className={isActive ? "text-indigo-600" : "text-slate-400"}>
                {item.icon}
              </span>
              {item.label}
            </Link>
          );
        })}
      </nav>

      {/* Logout */}
      <div className="px-3 py-3 border-t border-slate-100">
        <button
          onClick={handleLogout}
          className="flex items-center gap-2.5 w-full px-3 py-2 rounded-lg text-sm font-medium text-slate-600 hover:bg-red-50 hover:text-red-700 transition-colors"
        >
          <LogOut className="w-4 h-4 text-slate-400" />
          Sign out
        </button>
      </div>
    </aside>
  );

  return (
    <div className="flex h-screen bg-slate-50">
      {/* Desktop sidebar */}
      <div className="hidden lg:flex lg:w-60 lg:flex-shrink-0 flex-col">
        <Sidebar />
      </div>

      {/* Mobile sidebar overlay */}
      {sidebarOpen && (
        <div className="fixed inset-0 z-40 lg:hidden">
          <div
            className="absolute inset-0 bg-black/40"
            onClick={() => setSidebarOpen(false)}
          />
          <div className="absolute left-0 top-0 bottom-0 w-60 z-50">
            <Sidebar />
          </div>
        </div>
      )}

      {/* Main content */}
      <div className="flex-1 flex flex-col min-w-0 overflow-hidden">
        {/* Mobile header */}
        <header className="lg:hidden flex items-center justify-between px-4 py-3 bg-white border-b border-slate-200">
          <button
            onClick={() => setSidebarOpen(true)}
            className="p-1.5 rounded-lg hover:bg-slate-100"
          >
            <Menu className="w-5 h-5" />
          </button>
          <div className="flex items-center gap-2">
            <Building2 className="w-5 h-5 text-indigo-600" />
            <span className="font-bold text-slate-900">HMS</span>
          </div>
          <div className="w-8" />
        </header>

        {/* Page content */}
        <main className="flex-1 overflow-y-auto p-6">
          {children}
        </main>
      </div>
    </div>
  );
}
