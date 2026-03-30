"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { CreditCard, MessageSquare, Wallet, Building2, User, Phone, Hash } from "lucide-react";
import { useAuth } from "../../lib/auth";
import { api } from "../../lib/api";
import { formatCurrency, currentMonth, currentYear } from "../../lib/utils";
import { PageHeader, StatCard, Card, Spinner, ErrorMsg } from "../../components/ui";
import type { Student, StudentDues } from "../../lib/types";

export default function StudentDashboard() {
  const { user } = useAuth();
  const [student, setStudent] = useState<Student | null>(null);
  const [dues, setDues] = useState<StudentDues | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user?.studentId) { setLoading(false); return; }
    (async () => {
      try {
        const [s, d] = await Promise.all([
          api.students.getById(user.studentId!),
          api.business.getStudentDues(user.studentId!, currentMonth(), currentYear()),
        ]);
        setStudent(s);
        setDues(d);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load data");
      } finally {
        setLoading(false);
      }
    })();
  }, [user]);

  if (loading) return <Spinner />;

  return (
    <div>
      <PageHeader title="My Dashboard" subtitle={`Welcome back, ${user?.name}`} />

      {error && <ErrorMsg message={error} />}

      {student && (
        <Card className="p-5 mb-6">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-indigo-100 flex items-center justify-center">
              <User className="w-6 h-6 text-indigo-600" />
            </div>
            <div className="flex-1 grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
              <div>
                <p className="text-slate-400 text-xs">Name</p>
                <p className="font-semibold text-slate-900">{student.name}</p>
              </div>
              <div>
                <p className="text-slate-400 text-xs">Registration No.</p>
                <p className="font-semibold text-slate-900">{student.registrationNumber}</p>
              </div>
              <div>
                <p className="text-slate-400 text-xs">Hall</p>
                <p className="font-semibold text-slate-900">{user?.hallName ?? `Hall #${student.hallId}`}</p>
              </div>
              <div>
                <p className="text-slate-400 text-xs">Room</p>
                <p className="font-semibold text-slate-900">{student.roomId ? `Room #${student.roomId}` : "Not assigned"}</p>
              </div>
            </div>
          </div>
        </Card>
      )}

      {dues && (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
          <StatCard label="Mess Charges" value={formatCurrency(dues.messCharge)} color="amber" />
          <StatCard label="Room Rent" value={formatCurrency(dues.roomRent)} color="blue" />
          <StatCard label="Amenity Charges" value={formatCurrency(dues.amenityCharge)} color="purple" />
          <StatCard label="Total Due" value={formatCurrency(dues.totalDue)} color="rose" />
        </div>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <Link href="/student/dues" className="bg-white border border-slate-200 rounded-xl p-5 hover:border-indigo-300 hover:shadow-sm transition-all group">
          <CreditCard className="w-6 h-6 text-indigo-600 mb-3" />
          <p className="font-semibold text-slate-900 group-hover:text-indigo-700">View My Dues</p>
          <p className="text-sm text-slate-500 mt-1">Check your current dues breakdown</p>
        </Link>
        <Link href="/student/complaints" className="bg-white border border-slate-200 rounded-xl p-5 hover:border-emerald-300 hover:shadow-sm transition-all group">
          <MessageSquare className="w-6 h-6 text-emerald-600 mb-3" />
          <p className="font-semibold text-slate-900 group-hover:text-emerald-700">Complaints</p>
          <p className="text-sm text-slate-500 mt-1">Raise and track complaints</p>
        </Link>
        <Link href="/student/payments" className="bg-white border border-slate-200 rounded-xl p-5 hover:border-amber-300 hover:shadow-sm transition-all group">
          <Wallet className="w-6 h-6 text-amber-600 mb-3" />
          <p className="font-semibold text-slate-900 group-hover:text-amber-700">Payments</p>
          <p className="text-sm text-slate-500 mt-1">View payment history & pay dues</p>
        </Link>
      </div>
    </div>
  );
}
