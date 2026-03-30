"use client";

import { useEffect, useState } from "react";
import { BarChart3 } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency, currentYear } from "../../../lib/utils";
import { PageHeader, Card, StatCard, Spinner, ErrorMsg, Select } from "../../../components/ui";
import type { AnnualStatement } from "../../../lib/types";

export default function WardenAccountsPage() {
  const { user } = useAuth();
  const [statement, setStatement] = useState<AnnualStatement | null>(null);
  const [year, setYear] = useState(currentYear());
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const load = async () => {
    if (!user?.hallId) return;
    setLoading(true);
    setError("");
    try {
      const data = await api.business.getAnnualStatement(user.hallId, year);
      setStatement(data);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load statement");
      setStatement(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [year, user]);

  const years = [currentYear() - 2, currentYear() - 1, currentYear()];

  return (
    <div>
      <PageHeader title="Annual Statement of Accounts" subtitle={user?.hallName} />

      <Card className="p-4 mb-6 flex items-center gap-3">
        <label className="text-sm font-medium text-slate-600">Year:</label>
        <Select value={year} onChange={(e) => setYear(Number(e.target.value))} className="w-24">
          {years.map((y) => <option key={y} value={y}>{y}</option>)}
        </Select>
      </Card>

      {error && <ErrorMsg message={error} />}
      {loading && <Spinner />}

      {statement && !loading && (
        <>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <StatCard label="Grant Allocated" value={formatCurrency(statement.grantAllocated)} color="blue" />
            <StatCard label="Total Salaries Paid" value={formatCurrency(statement.totalSalariesPaid)} color="amber" />
            <StatCard label="Other Expenditures" value={formatCurrency(statement.totalExpenditures)} color="rose" />
            <StatCard label="Net Balance" value={formatCurrency(statement.totalBalance)} color={statement.totalBalance >= 0 ? "emerald" : "rose"} />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Card className="p-5">
              <p className="font-semibold text-slate-700 mb-4 flex items-center gap-2">
                <BarChart3 className="w-4 h-4 text-rose-600" />
                Expenditure Breakdown
              </p>
              <div className="space-y-3">
                <div className="flex justify-between text-sm">
                  <span className="text-slate-500">Staff Salaries</span>
                  <span className="font-semibold text-slate-900">{formatCurrency(statement.totalSalariesPaid)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-slate-500">Other Expenditures</span>
                  <span className="font-semibold text-slate-900">{formatCurrency(statement.totalExpenditures)}</span>
                </div>
                <div className="border-t border-slate-100 pt-2 flex justify-between text-sm font-bold">
                  <span>Total Expenditure</span>
                  <span className="text-rose-600">{formatCurrency(statement.totalSalariesPaid + statement.totalExpenditures)}</span>
                </div>
              </div>
            </Card>

            <Card className="p-5">
              <p className="font-semibold text-slate-700 mb-4 flex items-center gap-2">
                <BarChart3 className="w-4 h-4 text-blue-600" />
                Financial Summary
              </p>
              <div className="space-y-3">
                <div className="flex justify-between text-sm">
                  <span className="text-slate-500">Grant Allocated</span>
                  <span className="font-semibold text-emerald-700">+{formatCurrency(statement.grantAllocated)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-slate-500">Total Expenditure</span>
                  <span className="font-semibold text-rose-600">-{formatCurrency(statement.totalSalariesPaid + statement.totalExpenditures)}</span>
                </div>
                <div className="border-t border-slate-100 pt-2 flex justify-between text-sm font-bold">
                  <span>Net Balance</span>
                  <span className={statement.totalBalance >= 0 ? "text-emerald-600" : "text-red-600"}>
                    {formatCurrency(statement.totalBalance)}
                  </span>
                </div>
              </div>
            </Card>
          </div>

          <Card className="p-5 mt-6">
            <div className="flex items-center justify-between">
              <p className="font-semibold text-slate-700">Annual Net Balance ({year})</p>
              <p className={`text-2xl font-bold ${statement.totalBalance >= 0 ? "text-emerald-600" : "text-red-600"}`}>
                {formatCurrency(statement.totalBalance)}
              </p>
            </div>
            <p className="text-xs text-slate-400 mt-2">
              Formula: Grant Allocated − Staff Salaries − Other Expenditures
            </p>
          </Card>
        </>
      )}
    </div>
  );
}
