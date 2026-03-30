"use client";

import { useEffect, useState } from "react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency, monthName, currentMonth, currentYear, MONTHS } from "../../../lib/utils";
import { PageHeader, Card, Spinner, ErrorMsg, Select } from "../../../components/ui";
import type { StudentDues } from "../../../lib/types";
import { CreditCard, Utensils, Home, Star } from "lucide-react";

export default function StudentDuesPage() {
  const { user } = useAuth();
  const [dues, setDues] = useState<StudentDues | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [month, setMonth] = useState(currentMonth());
  const [year, setYear] = useState(currentYear());

  const fetchDues = async () => {
    if (!user?.studentId) return;
    setLoading(true);
    setError("");
    try {
      const d = await api.business.getStudentDues(user.studentId, month, year);
      setDues(d);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to fetch dues");
      setDues(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchDues(); }, [month, year]);

  const years = [currentYear() - 1, currentYear(), currentYear() + 1];

  return (
    <div>
      <PageHeader title="My Dues" subtitle="Monthly breakdown of charges" />

      <Card className="p-4 mb-6 flex flex-wrap gap-3 items-center">
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium text-slate-600">Month:</label>
          <Select value={month} onChange={(e) => setMonth(Number(e.target.value))} className="w-36">
            {MONTHS.map((m, i) => (
              <option key={m} value={i + 1}>{m}</option>
            ))}
          </Select>
        </div>
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium text-slate-600">Year:</label>
          <Select value={year} onChange={(e) => setYear(Number(e.target.value))} className="w-24">
            {years.map((y) => <option key={y} value={y}>{y}</option>)}
          </Select>
        </div>
      </Card>

      {error && <ErrorMsg message={error} />}
      {loading && <Spinner />}

      {dues && !loading && (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
            <Card className="p-5">
              <div className="flex items-center gap-3 mb-3">
                <div className="bg-amber-50 p-2 rounded-lg">
                  <Utensils className="w-5 h-5 text-amber-600" />
                </div>
                <p className="font-medium text-slate-700">Mess Charges</p>
              </div>
              <p className="text-2xl font-bold text-slate-900">{formatCurrency(dues.messCharge)}</p>
              <p className="text-xs text-slate-400 mt-1">For {monthName(dues.month)} {dues.year}</p>
            </Card>

            <Card className="p-5">
              <div className="flex items-center gap-3 mb-3">
                <div className="bg-blue-50 p-2 rounded-lg">
                  <Home className="w-5 h-5 text-blue-600" />
                </div>
                <p className="font-medium text-slate-700">Room Rent</p>
              </div>
              <p className="text-2xl font-bold text-slate-900">{formatCurrency(dues.roomRent)}</p>
              <p className="text-xs text-slate-400 mt-1">Fixed monthly rent</p>
            </Card>

            <Card className="p-5">
              <div className="flex items-center gap-3 mb-3">
                <div className="bg-purple-50 p-2 rounded-lg">
                  <Star className="w-5 h-5 text-purple-600" />
                </div>
                <p className="font-medium text-slate-700">Amenity Charges</p>
              </div>
              <p className="text-2xl font-bold text-slate-900">{formatCurrency(dues.amenityCharge)}</p>
              <p className="text-xs text-slate-400 mt-1">Reading room, TV, play room</p>
            </Card>
          </div>

          <Card className="p-6">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="bg-rose-50 p-2.5 rounded-xl">
                  <CreditCard className="w-6 h-6 text-rose-600" />
                </div>
                <div>
                  <p className="text-sm text-slate-500">Total Due for {monthName(dues.month)} {dues.year}</p>
                  <p className="text-3xl font-bold text-slate-900">{formatCurrency(dues.totalDue)}</p>
                </div>
              </div>
            </div>

            <div className="mt-5 border-t border-slate-100 pt-4 space-y-2">
              {[
                ["Mess Charges", dues.messCharge],
                ["Room Rent", dues.roomRent],
                ["Amenity Charges", dues.amenityCharge],
              ].map(([label, val]) => (
                <div key={String(label)} className="flex justify-between text-sm">
                  <span className="text-slate-600">{label}</span>
                  <span className="font-medium text-slate-900">{formatCurrency(Number(val))}</span>
                </div>
              ))}
              <div className="flex justify-between text-sm font-bold border-t border-slate-200 pt-2 mt-2">
                <span className="text-slate-900">Total</span>
                <span className="text-rose-600">{formatCurrency(dues.totalDue)}</span>
              </div>
            </div>

            <p className="text-xs text-slate-400 mt-4">
              Please pay your dues at the hall office or use the Payments section.
            </p>
          </Card>
        </>
      )}

      {!dues && !loading && !error && (
        <Card className="p-8 text-center">
          <p className="text-slate-500">No dues data found for this period.</p>
        </Card>
      )}
    </div>
  );
}
