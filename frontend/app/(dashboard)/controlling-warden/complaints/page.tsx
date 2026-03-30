"use client";

import { useEffect, useState } from "react";
import { api } from "../../../lib/api";
import { formatDate } from "../../../lib/utils";
import { PageHeader, Card, Badge, Table, Tr, Td, Spinner, ErrorMsg, EmptyState, Select, StatCard } from "../../../components/ui";
import type { Complaint, Hall } from "../../../lib/types";

export default function AllComplaintsPage() {
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [halls, setHalls] = useState<Hall[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [filterHall, setFilterHall] = useState<number | "all">("all");
  const [filterStatus, setFilterStatus] = useState("all");

  useEffect(() => {
    (async () => {
      try {
        const [c, h] = await Promise.all([api.complaints.getAll(), api.halls.getAll()]);
        setComplaints(c);
        setHalls(h);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <Spinner />;

  const filtered = complaints.filter((c) => {
    const hallOk = filterHall === "all" || c.hallId === filterHall;
    const statusOk = filterStatus === "all" || (c.complaintStatus ?? c.status) === filterStatus;
    return hallOk && statusOk;
  });

  const pending = complaints.filter((c) => (c.complaintStatus ?? c.status) === "PENDING").length;
  const resolved = complaints.filter((c) => (c.complaintStatus ?? c.status) === "RESOLVED").length;

  return (
    <div>
      <PageHeader title="All Complaints" subtitle="System-wide complaints across all halls" />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-3 gap-4 mb-6">
        <StatCard label="Total" value={complaints.length} color="blue" />
        <StatCard label="Pending" value={pending} color="amber" />
        <StatCard label="Resolved" value={resolved} color="emerald" />
      </div>

      <Card className="p-4 mb-4 flex flex-wrap gap-3 items-center">
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium text-slate-600">Hall:</label>
          <Select value={filterHall} onChange={(e) => setFilterHall(e.target.value === "all" ? "all" : Number(e.target.value))} className="w-40">
            <option value="all">All Halls</option>
            {halls.map((h) => <option key={h.id} value={h.id}>{h.name}</option>)}
          </Select>
        </div>
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium text-slate-600">Status:</label>
          <Select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)} className="w-36">
            <option value="all">All</option>
            <option value="PENDING">Pending</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="RESOLVED">Resolved</option>
          </Select>
        </div>
      </Card>

      <Card>
        {filtered.length === 0 ? (
          <EmptyState title="No complaints found" description="No complaints match your filters." />
        ) : (
          <Table headers={["#", "Description", "Type", "Hall", "Student", "Status", "ATR", "Date"]}>
            {filtered.map((c, i) => {
              const hall = halls.find((h) => h.id === c.hallId);
              return (
                <Tr key={c.id}>
                  <Td>{c.id}</Td>
                  <Td className="max-w-xs truncate text-sm">{c.title || c.description?.slice(0, 50)}</Td>
                  <Td>
                    <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">
                      {(c.complaintType || c.type || "").replace(/_/g, " ")}
                    </span>
                  </Td>
                  <Td className="text-slate-600 text-sm">{hall?.name ?? `#${c.hallId}`}</Td>
                  <Td className="text-slate-500 text-sm">#{c.studentId}</Td>
                  <Td><Badge status={c.complaintStatus ?? c.status ?? "PENDING"} /></Td>
                  <Td className="text-xs text-slate-500 max-w-xs truncate">
                    {c.atr ? <span className="text-emerald-700">{c.atr.slice(0, 60)}</span> : <span className="text-slate-300">—</span>}
                  </Td>
                  <Td className="text-slate-400 text-xs">{formatDate(c.complaintDate || "")}</Td>
                </Tr>
              );
            })}
          </Table>
        )}
      </Card>
    </div>
  );
}
