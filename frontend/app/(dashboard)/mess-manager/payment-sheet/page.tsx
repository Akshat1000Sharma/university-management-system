"use client";

import { useEffect, useState } from "react";
import { DollarSign, Printer } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency, formatDate, monthName, currentMonth, currentYear, MONTHS } from "../../../lib/utils";
import { PageHeader, Card, Spinner, ErrorMsg, Select, Btn } from "../../../components/ui";
import type { MessPaymentSheet } from "../../../lib/types";

export default function MessPaymentSheetPage() {
  const { user } = useAuth();
  const [sheet, setSheet] = useState<MessPaymentSheet | null>(null);
  const [month, setMonth] = useState(currentMonth());
  const [year, setYear] = useState(currentYear());
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [chargeCount, setChargeCount] = useState<number | null>(null);

  const load = async () => {
    if (!user?.hallId) return;
    setLoading(true); setError("");
    try {
      const [data, charges] = await Promise.all([
        api.business.getMessPayment(user.hallId, month, year),
        api.messCharges.getByHallMonthYear(user.hallId, month, year),
      ]);
      setSheet(data);
      setChargeCount(charges.length);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "No payment data for this period");
      setSheet(null);
      setChargeCount(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [month, year, user]);

  const years = [currentYear() - 1, currentYear()];
  const chargePerStudent =
    sheet?.monthlyCharge != null && Number.isFinite(sheet.monthlyCharge)
      ? sheet.monthlyCharge
      : sheet && chargeCount != null && chargeCount > 0
        ? sheet.totalAmount / chargeCount
        : undefined;

  return (
    <div>
      <PageHeader
        title="Monthly Payment Sheet"
        subtitle="Total mess charges owed to you by the hall"
        action={
          sheet ? (
            <Btn variant="secondary" onClick={() => window.print()}>
              <Printer className="w-4 h-4" /> Print Sheet
            </Btn>
          ) : undefined
        }
      />

      <Card className="p-4 mb-6 flex flex-wrap gap-3 items-center">
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium text-slate-600">Month:</label>
          <Select value={month} onChange={(e) => setMonth(Number(e.target.value))} className="w-36">
            {MONTHS.map((m, i) => <option key={m} value={i + 1}>{m}</option>)}
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

      {sheet && !loading && (
        <Card className="p-6">
          <div className="border border-slate-200 rounded-xl overflow-hidden">
            {/* Header */}
            <div className="bg-indigo-600 text-white px-6 py-4">
              <p className="text-sm opacity-80 font-medium">MESS MANAGER PAYMENT SHEET</p>
              <p className="text-xl font-bold">{sheet.hallName}</p>
              <p className="text-sm opacity-80 mt-0.5">{monthName(sheet.month)} {sheet.year}</p>
            </div>

            <div className="p-6 space-y-5">
              <div className="grid grid-cols-2 gap-6">
                <div>
                  <p className="text-xs text-slate-400 uppercase tracking-wide">Mess Manager</p>
                  <p className="font-semibold text-slate-900 mt-1">{sheet.messManagerName}</p>
                  <p className="text-sm text-slate-500">Hall #{sheet.hallId}</p>
                </div>
                <div>
                  <p className="text-xs text-slate-400 uppercase tracking-wide">Payment Date</p>
                  <p className="font-semibold text-slate-900 mt-1">{sheet.paymentDate ? formatDate(sheet.paymentDate) : "—"}</p>
                </div>
              </div>

              <div className="border-t border-slate-100 pt-4 space-y-3">
                {sheet.totalStudents !== undefined && (
                  <div className="flex justify-between text-sm">
                    <span className="text-slate-600">Total Students</span>
                    <span className="font-semibold">{sheet.totalStudents}</span>
                  </div>
                )}
                {chargePerStudent !== undefined && (
                  <div className="flex justify-between text-sm">
                    <span className="text-slate-600">Charge per Student</span>
                    <span className="font-semibold">{formatCurrency(chargePerStudent)}</span>
                  </div>
                )}
                <div className="flex justify-between text-sm font-bold text-lg border-t border-slate-200 pt-3">
                  <span className="text-slate-900">Total Amount Due</span>
                  <span className="text-emerald-600">{formatCurrency(sheet.totalAmount)}</span>
                </div>
              </div>

              <div className="bg-emerald-50 border border-emerald-200 rounded-lg p-4 flex items-center gap-3">
                <DollarSign className="w-8 h-8 text-emerald-600 shrink-0" />
                <div>
                  <p className="text-xs text-emerald-700 font-medium">Payable Amount</p>
                  <p className="text-2xl font-bold text-emerald-700">{formatCurrency(sheet.totalAmount)}</p>
                <p className="text-xs text-emerald-600 mt-0.5">
                  Cheque to be issued to {sheet.messManagerName} for {monthName(sheet.month)} {sheet.year}
                </p>
                </div>
              </div>

              <div className="mt-8 pt-6 border-t border-slate-100 grid grid-cols-2 gap-8">
                <div>
                  <div className="border-t border-slate-400 pt-2 mt-8">
                    <p className="text-xs text-slate-500">Signature of Mess Manager</p>
                    <p className="text-sm font-medium text-slate-700 mt-0.5">{sheet.messManagerName}</p>
                  </div>
                </div>
                <div>
                  <div className="border-t border-slate-400 pt-2 mt-8">
                    <p className="text-xs text-slate-500">Signature of Warden</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </Card>
      )}
    </div>
  );
}
