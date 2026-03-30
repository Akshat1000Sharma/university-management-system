"use client";

import Link from "next/link";
import { Calendar, DollarSign } from "lucide-react";
import { useAuth } from "../../lib/auth";
import { PageHeader } from "../../components/ui";

export default function ClerkDashboard() {
  const { user } = useAuth();

  return (
    <div>
      <PageHeader title="Hall Clerk Dashboard" subtitle={`${user?.hallName ?? "Your Hall"} — ${user?.name}`} />

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <Link href="/clerk/leaves" className="bg-white border border-slate-200 rounded-xl p-6 hover:border-teal-300 hover:shadow-sm transition-all group">
          <Calendar className="w-7 h-7 text-teal-600 mb-3" />
          <p className="font-semibold text-slate-900 text-lg group-hover:text-teal-700">Staff Leaves</p>
          <p className="text-sm text-slate-500 mt-1">Enter and manage staff leave records</p>
          <ul className="mt-3 text-xs text-slate-400 space-y-1">
            <li>• Record daily leaves for attendants and gardeners</li>
            <li>• Supports casual, earned, medical & special leave</li>
          </ul>
        </Link>
        <Link href="/clerk/salary" className="bg-white border border-slate-200 rounded-xl p-6 hover:border-indigo-300 hover:shadow-sm transition-all group">
          <DollarSign className="w-7 h-7 text-indigo-600 mb-3" />
          <p className="font-semibold text-slate-900 text-lg group-hover:text-indigo-700">Salary Sheet</p>
          <p className="text-sm text-slate-500 mt-1">View monthly salary with leave deductions</p>
          <ul className="mt-3 text-xs text-slate-400 space-y-1">
            <li>• Auto-calculates net pay after leave deductions</li>
            <li>• Printable salary sheet for each staff member</li>
          </ul>
        </Link>
      </div>
    </div>
  );
}
