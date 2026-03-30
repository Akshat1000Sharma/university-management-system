"use client";

import { useEffect, useState } from "react";
import { api } from "../../../lib/api";
import { formatCurrency, formatDate } from "../../../lib/utils";
import { PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState, Select, StatCard } from "../../../components/ui";
import type { Expenditure, Hall } from "../../../lib/types";

export default function HmcExpendituresPage() {
  const [expenditures, setExpenditures] = useState<Expenditure[]>([]);
  const [halls, setHalls] = useState<Hall[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [filterHall, setFilterHall] = useState<number | "all">("all");

  useEffect(() => {
    (async () => {
      try {
        const [e, h] = await Promise.all([api.expenditures.getAll(), api.halls.getAll()]);
        setExpenditures(e);
        setHalls(h);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <Spinner />;

  const filtered = filterHall === "all" ? expenditures : expenditures.filter((e) => e.hallId === filterHall);
  const total = filtered.reduce((s, e) => s + e.amount, 0);

  const byHall = halls.map((h) => ({
    hall: h,
    total: expenditures.filter((e) => e.hallId === h.id).reduce((s, e) => s + e.amount, 0),
  }));

  return (
    <div>
      <PageHeader title="All Expenditures" subtitle="System-wide expenditure overview" />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <StatCard label="Total Records" value={expenditures.length} color="blue" />
        <StatCard label="Total Expenditure" value={formatCurrency(expenditures.reduce((s, e) => s + e.amount, 0))} color="rose" />
        {byHall.slice(0, 2).map(({ hall, total: t }) => (
          <StatCard key={hall.id} label={hall.name} value={formatCurrency(t)} color="amber" />
        ))}
      </div>

      <Card className="p-4 mb-4 flex items-center gap-3">
        <label className="text-sm font-medium text-slate-600">Hall:</label>
        <Select value={filterHall} onChange={(e) => setFilterHall(e.target.value === "all" ? "all" : Number(e.target.value))} className="w-44">
          <option value="all">All Halls</option>
          {halls.map((h) => <option key={h.id} value={h.id}>{h.name}</option>)}
        </Select>
        {filterHall !== "all" && (
          <span className="text-sm text-slate-500">Total: <strong>{formatCurrency(total)}</strong></span>
        )}
      </Card>

      <Card>
        {filtered.length === 0 ? (
          <EmptyState title="No expenditures found" description="No expenditure records match the selected filter." />
        ) : (
          <Table headers={["#", "Hall", "Description", "Category", "Amount", "Date"]}>
            {filtered.map((e, i) => {
              const hall = halls.find((h) => h.id === e.hallId);
              return (
                <Tr key={e.id}>
                  <Td>{i + 1}</Td>
                  <Td className="font-medium text-sm">{hall?.name ?? `#${e.hallId}`}</Td>
                  <Td className="max-w-xs truncate text-sm">{e.description}</Td>
                  <Td>
                    <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">
                      {e.expenseCategory ?? e.category}
                    </span>
                  </Td>
                  <Td className="font-semibold text-rose-600">{formatCurrency(e.amount)}</Td>
                  <Td className="text-slate-400 text-xs">{formatDate(e.date)}</Td>
                </Tr>
              );
            })}
            <tr className="bg-slate-50 border-t border-slate-200">
              <td colSpan={4} className="px-4 py-3 text-right font-bold text-slate-700 text-sm">Total:</td>
              <td className="px-4 py-3 font-bold text-rose-600">{formatCurrency(total)}</td>
              <td />
            </tr>
          </Table>
        )}
      </Card>
    </div>
  );
}
