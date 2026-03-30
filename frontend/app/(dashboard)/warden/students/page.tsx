"use client";

import { useEffect, useState } from "react";
import { Plus, Trash2, UserPlus } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatDate } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState,
  Modal, FormField, Input, Select, Btn, StatCard
} from "../../../components/ui";
import type { Student, Room } from "../../../lib/types";

export default function WardenStudentsPage() {
  const { user } = useAuth();
  const [students, setStudents] = useState<Student[]>([]);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showAdmit, setShowAdmit] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    name: "", email: "", phone: "", registrationNumber: "", admissionDate: new Date().toISOString().split("T")[0],
  });

  const load = async () => {
    if (!user?.hallId) { setLoading(false); return; }
    try {
      const [s, r] = await Promise.all([
        api.students.getByHall(user.hallId),
        api.rooms.getByHall(user.hallId),
      ]);
      setStudents(s);
      setRooms(r);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [user]);

  const handleAdmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user?.hallId) return;
    setSubmitting(true);
    try {
      await api.business.admitStudent({ ...form, hallId: user.hallId });
      setShowAdmit(false);
      setForm({ name: "", email: "", phone: "", registrationNumber: "", admissionDate: new Date().toISOString().split("T")[0] });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to admit student");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Are you sure you want to remove this student?")) return;
    try {
      await api.students.delete(id);
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to delete");
    }
  };

  const vacantRooms = rooms.filter((r) => !students.some((s) => s.roomId === r.id));
  if (loading) return <Spinner />;

  return (
    <div>
      <PageHeader
        title="Student Management"
        subtitle={user?.hallName}
        action={
          <Btn onClick={() => setShowAdmit(true)}>
            <UserPlus className="w-4 h-4" /> Admit Student
          </Btn>
        }
      />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mb-6">
        <StatCard label="Total Students" value={students.length} color="blue" />
        <StatCard label="Vacant Rooms" value={vacantRooms.length} color="amber" />
        <StatCard label="Total Rooms" value={rooms.length} color="indigo" />
      </div>

      <Card>
        {students.length === 0 ? (
          <EmptyState
            title="No students yet"
            description="Admit the first student to this hall."
            action={<Btn onClick={() => setShowAdmit(true)}><UserPlus className="w-4 h-4" /> Admit Student</Btn>}
          />
        ) : (
          <Table headers={["#", "Name", "Reg. No.", "Phone", "Room", "Admitted", "Actions"]}>
            {students.map((s, i) => {
              const room = rooms.find((r) => r.id === s.roomId);
              return (
                <Tr key={s.id}>
                  <Td>{i + 1}</Td>
                  <Td className="font-medium">{s.name}</Td>
                  <Td className="text-slate-500 text-sm font-mono">{s.registrationNumber}</Td>
                  <Td className="text-slate-500 text-sm">{s.phone}</Td>
                  <Td>
                    {room ? (
                      <span className="text-xs bg-emerald-50 text-emerald-700 px-2 py-0.5 rounded-full">
                        Room {room.roomNumber}
                      </span>
                    ) : (
                      <span className="text-xs text-slate-400">Not assigned</span>
                    )}
                  </Td>
                  <Td className="text-slate-400 text-xs">{formatDate(s.admissionDate ?? "")}</Td>
                  <Td>
                    <Btn size="sm" variant="danger" onClick={() => handleDelete(s.id)}>
                      <Trash2 className="w-3 h-3" />
                    </Btn>
                  </Td>
                </Tr>
              );
            })}
          </Table>
        )}
      </Card>

      {showAdmit && (
        <Modal title="Admit New Student" onClose={() => setShowAdmit(false)}>
          <form onSubmit={handleAdmit} className="space-y-4">
            <FormField label="Full Name" required>
              <Input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} placeholder="Student full name" required />
            </FormField>
            <FormField label="Email">
              <Input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="student@example.com" />
            </FormField>
            <FormField label="Phone Number" required>
              <Input value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} placeholder="10-digit phone number" required />
            </FormField>
            <FormField label="Registration Number" required>
              <Input value={form.registrationNumber} onChange={(e) => setForm({ ...form, registrationNumber: e.target.value })} placeholder="e.g. REG001" required />
            </FormField>
            <FormField label="Admission Date" required>
              <Input type="date" value={form.admissionDate} onChange={(e) => setForm({ ...form, admissionDate: e.target.value })} required />
            </FormField>
            <p className="text-xs text-slate-400">A room will be automatically assigned from available rooms in {user?.hallName}.</p>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowAdmit(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>{submitting ? "Admitting…" : "Admit Student"}</Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
