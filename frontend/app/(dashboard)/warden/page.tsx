"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { Users, MessageSquare, Home, BarChart3, FileText, DollarSign } from "lucide-react";
import { useAuth } from "../../lib/auth";
import { api } from "../../lib/api";
import { formatCurrency, currentMonth, currentYear } from "../../lib/utils";
import { PageHeader, StatCard, Card, Spinner, ErrorMsg } from "../../components/ui";
import type { HallOccupancy, Complaint, Student } from "../../lib/types";

export default function WardenDashboard() {
  const { user } = useAuth();
  const [occupancy, setOccupancy] = useState<HallOccupancy | null>(null);
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user?.hallId) { setLoading(false); return; }
    (async () => {
      try {
        const [occ, comp, stu] = await Promise.all([
          api.business.getHallOccupancy(user.hallId!),
          api.complaints.getByHall(user.hallId!),
          api.students.getByHall(user.hallId!),
        ]);
        setOccupancy(occ);
        setComplaints(comp);
        setStudents(stu);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load dashboard");
      } finally {
        setLoading(false);
      }
    })();
  }, [user]);

  if (loading) return <Spinner />;

  const pending = complaints.filter((c) => (c.complaintStatus ?? c.status) === "PENDING").length;

  return (
    <div>
      <PageHeader title="Warden Dashboard" subtitle={`${user?.hallName ?? "Your Hall"} — ${currentMonth()}/${currentYear()}`} />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <StatCard label="Total Students" value={students.length} icon={<Users className="w-5 h-5" />} color="blue" />
        <StatCard label="Occupancy" value={occupancy ? `${occupancy.occupancyPercentage.toFixed(0)}%` : "—"} icon={<Home className="w-5 h-5" />} color="emerald" />
        <StatCard label="Total Complaints" value={complaints.length} icon={<MessageSquare className="w-5 h-5" />} color="amber" />
        <StatCard label="Pending Complaints" value={pending} icon={<MessageSquare className="w-5 h-5" />} color="rose" />
      </div>

      {occupancy && (
        <Card className="p-5 mb-6">
          <p className="font-semibold text-slate-700 mb-3">Room Occupancy</p>
          <div className="flex items-center gap-6 text-sm">
            <div><span className="text-slate-500">Total Rooms: </span><span className="font-bold">{occupancy.totalRooms}</span></div>
            <div><span className="text-slate-500">Occupied: </span><span className="font-bold text-emerald-600">{occupancy.occupiedRooms}</span></div>
            <div><span className="text-slate-500">Empty: </span><span className="font-bold text-amber-600">{occupancy.emptyRooms}</span></div>
          </div>
          <div className="mt-3 bg-slate-100 rounded-full h-3">
            <div
              className="h-3 rounded-full bg-emerald-500 transition-all"
              style={{ width: `${occupancy.occupancyPercentage}%` }}
            />
          </div>
          <p className="text-xs text-slate-400 mt-1">{occupancy.occupancyPercentage.toFixed(1)}% occupied</p>
        </Card>
      )}

      <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
        {[
          { href: "/warden/students", label: "Manage Students", desc: "Admit and view students", icon: <Users className="w-6 h-6 text-blue-600" /> },
          { href: "/warden/complaints", label: "Complaints & ATR", desc: "View and respond to complaints", icon: <MessageSquare className="w-6 h-6 text-amber-600" /> },
          { href: "/warden/occupancy", label: "Room Occupancy", desc: "Hall occupancy details", icon: <Home className="w-6 h-6 text-emerald-600" /> },
          { href: "/warden/staff", label: "Staff Management", desc: "Manage hall staff", icon: <Users className="w-6 h-6 text-purple-600" /> },
          { href: "/warden/salary", label: "Salary Sheet", desc: "Generate staff salaries", icon: <DollarSign className="w-6 h-6 text-indigo-600" /> },
          { href: "/warden/accounts", label: "Annual Statement", desc: "Financial overview", icon: <BarChart3 className="w-6 h-6 text-rose-600" /> },
        ].map((item) => (
          <Link
            key={item.href}
            href={item.href}
            className="bg-white border border-slate-200 rounded-xl p-5 hover:shadow-sm hover:border-indigo-200 transition-all group"
          >
            <div className="mb-3">{item.icon}</div>
            <p className="font-semibold text-slate-900 group-hover:text-indigo-700">{item.label}</p>
            <p className="text-xs text-slate-500 mt-0.5">{item.desc}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}
