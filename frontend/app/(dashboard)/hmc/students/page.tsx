"use client";

import { useEffect, useState } from "react";
import { api } from "../../../lib/api";
import { formatDate } from "../../../lib/utils";
import { PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState, Select, StatCard } from "../../../components/ui";
import type { Student, Hall, Room } from "../../../lib/types";

export default function HmcStudentsPage() {
  const [students, setStudents] = useState<Student[]>([]);
  const [halls, setHalls] = useState<Hall[]>([]);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [filterHall, setFilterHall] = useState<number | "all">("all");

  useEffect(() => {
    (async () => {
      try {
        const [s, h, r] = await Promise.all([api.students.getAll(), api.halls.getAll(), api.rooms.getAll()]);
        setStudents(s);
        setHalls(h);
        setRooms(r);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <Spinner />;

  const filtered = filterHall === "all" ? students : students.filter((s) => s.hallId === filterHall);

  return (
    <div>
      <PageHeader title="All Students" subtitle="All admitted students across the system" />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <StatCard label="Total Students" value={students.length} color="blue" />
        {halls.slice(0, 3).map((h) => (
          <StatCard key={h.id} label={h.name} value={students.filter((s) => s.hallId === h.id).length} color="indigo" />
        ))}
      </div>

      <Card className="p-4 mb-4 flex items-center gap-3">
        <label className="text-sm font-medium text-slate-600">Hall:</label>
        <Select value={filterHall} onChange={(e) => setFilterHall(e.target.value === "all" ? "all" : Number(e.target.value))} className="w-44">
          <option value="all">All Halls ({students.length})</option>
          {halls.map((h) => <option key={h.id} value={h.id}>{h.name} ({students.filter((s) => s.hallId === h.id).length})</option>)}
        </Select>
      </Card>

      <Card>
        {filtered.length === 0 ? (
          <EmptyState title="No students found" description="No students match the selected filter." />
        ) : (
          <Table headers={["#", "Name", "Reg. No.", "Phone", "Hall", "Room", "Admitted"]}>
            {filtered.map((s, i) => {
              const hall = halls.find((h) => h.id === s.hallId);
              const room = rooms.find((r) => r.id === s.roomId);
              return (
                <Tr key={s.id}>
                  <Td>{i + 1}</Td>
                  <Td className="font-medium">{s.name}</Td>
                  <Td className="font-mono text-sm text-slate-600">{s.registrationNumber}</Td>
                  <Td className="text-slate-500 text-sm">{s.phone}</Td>
                  <Td className="text-sm">{hall?.name ?? `#${s.hallId}`}</Td>
                  <Td>
                    {room ? (
                      <span className="text-xs bg-emerald-50 text-emerald-700 px-2 py-0.5 rounded-full">Room {room.roomNumber}</span>
                    ) : (
                      <span className="text-xs text-slate-400">Not assigned</span>
                    )}
                  </Td>
                  <Td className="text-slate-400 text-xs">{formatDate(s.admissionDate ?? "")}</Td>
                </Tr>
              );
            })}
          </Table>
        )}
      </Card>
    </div>
  );
}
