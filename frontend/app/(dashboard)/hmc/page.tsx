"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { Gift, Building2, DollarSign, Users, TrendingUp } from "lucide-react";
import { api } from "../../lib/api";
import { formatCurrency } from "../../lib/utils";
import { PageHeader, StatCard, Spinner, ErrorMsg } from "../../components/ui";
import type { Hall, Grant, Student } from "../../lib/types";

export default function HmcDashboard() {
  const [halls, setHalls] = useState<Hall[]>([]);
  const [grants, setGrants] = useState<Grant[]>([]);
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const [h, g, s] = await Promise.all([
          api.halls.getAll(),
          api.grants.getAll(),
          api.students.getAll(),
        ]);
        setHalls(h);
        setGrants(g);
        setStudents(s);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <Spinner />;

  const latestGrant = grants[0];
  const totalGrantAmount = grants.reduce((s, g) => s + g.totalAmount, 0);

  return (
    <div>
      <PageHeader title="HMC Chairman Dashboard" subtitle="Hall Management Committee — Oversight & Grant Distribution" />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <StatCard label="Total Halls" value={halls.length} icon={<Building2 className="w-5 h-5" />} color="blue" />
        <StatCard label="Total Students" value={students.length} icon={<Users className="w-5 h-5" />} color="indigo" />
        <StatCard label="Total Grants" value={grants.length} icon={<Gift className="w-5 h-5" />} color="amber" />
        <StatCard
          label="Total Grant Amount"
          value={formatCurrency(totalGrantAmount)}
          icon={<DollarSign className="w-5 h-5" />}
          color="emerald"
        />
      </div>

      {latestGrant && (
        <div className="bg-gradient-to-r from-indigo-50 to-purple-50 border border-indigo-100 rounded-xl p-5 mb-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold text-indigo-600 uppercase tracking-wide">Latest Grant</p>
              <p className="text-2xl font-bold text-slate-900 mt-1">{formatCurrency(latestGrant.totalAmount)}</p>
              <p className="text-sm text-slate-500 mt-0.5">{latestGrant.grantName ?? `Year ${latestGrant.year}`}</p>
            </div>
            <TrendingUp className="w-10 h-10 text-indigo-300" />
          </div>
        </div>
      )}

      <div className="grid grid-cols-2 md:grid-cols-2 gap-4 mt-2">
        {[
          { href: "/hmc/grants", label: "Grant Management", desc: "Create grants and distribute across halls", icon: <Gift className="w-6 h-6 text-amber-600" /> },
          { href: "/hmc/halls", label: "Halls & Rooms", desc: "Add/manage halls and their rooms", icon: <Building2 className="w-6 h-6 text-blue-600" /> },
          { href: "/hmc/expenditures", label: "All Expenditures", desc: "View expenditures across all halls", icon: <DollarSign className="w-6 h-6 text-rose-600" /> },
          { href: "/hmc/students", label: "All Students", desc: "View all students across the system", icon: <Users className="w-6 h-6 text-emerald-600" /> },
        ].map((item) => (
          <Link
            key={item.href}
            href={item.href}
            className="bg-white border border-slate-200 rounded-xl p-5 hover:shadow-sm hover:border-indigo-200 transition-all group"
          >
            <div className="mb-3">{item.icon}</div>
            <p className="font-semibold text-slate-900 group-hover:text-indigo-700">{item.label}</p>
            <p className="text-sm text-slate-500 mt-0.5">{item.desc}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}
