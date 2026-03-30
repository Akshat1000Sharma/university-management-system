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
            <StatCard label="Total Income" value={formatCurrency(statement.totalIncome)} color="emerald" />
            <StatCard label="Total Expenditure" value={formatCurrency(statement.totalExpenditure)} color="rose" />
            <StatCard label="Total Grant" value={formatCurrency(statement.totalGrant)} color="blue" />
            <StatCard label="Net Balance" value={formatCurrency(statement.netBalance)} color={statement.netBalance >= 0 ? "emerald" : "rose"} />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {statement.incomeBreakdown && (
              <Card className="p-5">
                <p className="font-semibold text-slate-700 mb-4 flex items-center gap-2">
                  <BarChart3 className="w-4 h-4 text-emerald-600" />
                  Income Breakdown
                </p>
                <div className="space-y-3">
                  {Object.entries(statement.incomeBreakdown).map(([key, val]) => (
                    <div key={key} className="flex justify-between text-sm">
                      <span className="text-slate-500 capitalize">{key.replace(/([A-Z])/g, " $1")}</span>
                      <span className="font-semibold text-slate-900">{formatCurrency(val)}</span>
                    </div>
                  ))}
                  <div className="border-t border-slate-100 pt-2 flex justify-between text-sm font-bold">
                    <span>Total Income</span>
                    <span className="text-emerald-600">{formatCurrency(statement.totalIncome)}</span>
                  </div>
                </div>
              </Card>
            )}

            {statement.expenditureBreakdown && (
              <Card className="p-5">
                <p className="font-semibold text-slate-700 mb-4 flex items-center gap-2">
                  <BarChart3 className="w-4 h-4 text-rose-600" />
                  Expenditure Breakdown
                </p>
                <div className="space-y-3">
                  {Object.entries(statement.expenditureBreakdown).map(([key, val]) => (
                    <div key={key} className="flex justify-between text-sm">
                      <span className="text-slate-500 capitalize">{key.replace(/([A-Z])/g, " $1")}</span>
                      <span className="font-semibold text-slate-900">{formatCurrency(val)}</span>
                    </div>
                  ))}
                  <div className="border-t border-slate-100 pt-2 flex justify-between text-sm font-bold">
                    <span>Total Expenditure</span>
                    <span className="text-rose-600">{formatCurrency(statement.totalExpenditure)}</span>
                  </div>
                </div>
              </Card>
            )}
          </div>

          <Card className="p-5 mt-6">
            <div className="flex items-center justify-between">
              <p className="font-semibold text-slate-700">Annual Net Balance ({year})</p>
              <p className={`text-2xl font-bold ${statement.netBalance >= 0 ? "text-emerald-600" : "text-red-600"}`}>
                {formatCurrency(statement.netBalance)}
              </p>
            </div>
            <p className="text-xs text-slate-400 mt-2">
              Formula: Total Income + Total Grant − Total Expenditure
            </p>
          </Card>
        </>
      )}
    </div>
  );
}
