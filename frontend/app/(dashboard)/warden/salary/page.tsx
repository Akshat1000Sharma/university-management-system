"use client";

import { useEffect, useState } from "react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency, monthName, currentMonth, currentYear, MONTHS } from "../../../lib/utils";
import { PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState, Select, StatCard } from "../../../components/ui";
import type { SalaryRecord } from "../../../lib/types";

export default function WardenSalaryPage() {
  const { user } = useAuth();
  const [records, setRecords] = useState<SalaryRecord[]>([]);
  const [month, setMonth] = useState(currentMonth());
  const [year, setYear] = useState(currentYear());
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const load = async () => {
    if (!user?.hallId) return;
    setLoading(true); setError("");
    try {
      const data = await api.business.getSalarySheet(user.hallId, month, year);
      setRecords(data);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load salary sheet");
      setRecords([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [month, year, user]);

  const total = records.reduce((s, r) => s + r.salary, 0);
  const years = [currentYear() - 1, currentYear()];

  return (
    <div>
      <PageHeader title="Staff Salary Sheet" subtitle={user?.hallName} />

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
      {loading ? <Spinner /> : (
        <>
          {records.length > 0 && (
            <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mb-6">
              <StatCard label="Total Staff" value={records.length} color="blue" />
              <StatCard label="Total Salary Payable" value={formatCurrency(total)} color="emerald" />
              <StatCard label="Total Leaves Taken" value={records.reduce((s, r) => s + r.leaveDays, 0)} color="amber" />
            </div>
          )}

          <Card>
            <div className="px-4 py-3 border-b border-slate-100">
              <h3 className="font-semibold text-slate-700">
                Salary Sheet — {monthName(month)} {year}
              </h3>
            </div>
            {records.length === 0 ? (
              <EmptyState title="No salary data" description="No staff records found for this period." />
            ) : (
              <Table headers={["#", "Name", "Type", "Daily Pay", "Total Days", "Working Days", "Leaves", "Salary"]}>
                {records.map((r, i) => (
                  <Tr key={r.staffId}>
                    <Td>{i + 1}</Td>
                    <Td className="font-medium">{r.staffName}</Td>
                    <Td><span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">{r.staffType}</span></Td>
                    <Td>₹{r.dailyPay}/day</Td>
                    <Td className="text-center">{r.totalDays}</Td>
                    <Td className="text-center">{r.workingDays}</Td>
                    <Td className="text-center text-amber-600">{r.leaveDays}</Td>
                    <Td className="font-bold text-emerald-600">{formatCurrency(r.salary)}</Td>
                  </Tr>
                ))}
                <tr className="bg-slate-50 border-t-2 border-slate-200">
                  <td colSpan={7} className="px-4 py-3 text-right font-bold text-slate-700">Total Payable:</td>
                  <td className="px-4 py-3 font-bold text-emerald-600 text-lg">{formatCurrency(total)}</td>
                </tr>
              </Table>
            )}
          </Card>
        </>
      )}
    </div>
  );
}
