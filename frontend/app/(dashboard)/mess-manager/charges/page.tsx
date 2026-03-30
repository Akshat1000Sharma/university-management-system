"use client";

import { useEffect, useState } from "react";
import { Plus, Save } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency, monthName, currentMonth, currentYear, MONTHS } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState,
  Modal, FormField, Input, Select, Btn, StatCard
} from "../../../components/ui";
import type { Student, MessCharge } from "../../../lib/types";

export default function MessChargesPage() {
  const { user } = useAuth();
  const [students, setStudents] = useState<Student[]>([]);
  const [charges, setCharges] = useState<MessCharge[]>([]);
  const [month, setMonth] = useState(currentMonth());
  const [year, setYear] = useState(currentYear());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({ studentId: "", amount: "" });

  const load = async () => {
    if (!user?.hallId) { setLoading(false); return; }
    try {
      const [stu, chr] = await Promise.all([
        api.students.getByHall(user.hallId),
        api.messCharges.getAll(),
      ]);
      setStudents(stu);
      setCharges(chr.filter((c) => c.hallId === user.hallId && c.month === month && c.year === year));
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [month, year, user]);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user?.hallId) return;
    setSubmitting(true);
    try {
      await api.messCharges.create({
        studentId: Number(form.studentId),
        amount: Number(form.amount),
        month, year,
        hallId: user.hallId,
      });
      setShowModal(false);
      setForm({ studentId: "", amount: "" });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to save");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Delete this charge?")) return;
    try { await api.messCharges.delete(id); await load(); }
    catch (e: unknown) { setError(e instanceof Error ? e.message : "Failed to delete"); }
  };

  if (loading) return <Spinner />;

  const total = charges.reduce((s, c) => s + c.amount, 0);
  const studentsWithCharge = new Set(charges.map((c) => c.studentId));
  const years = [currentYear() - 1, currentYear()];

  return (
    <div>
      <PageHeader
        title="Mess Charges"
        subtitle="Enter monthly mess charges for students"
        action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> Add Charge</Btn>}
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

      <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mb-6">
        <StatCard label="Charges Entered" value={charges.length} color="blue" />
        <StatCard label="Students Pending" value={students.length - studentsWithCharge.size} color="amber" />
        <StatCard label={`Total for ${monthName(month)}`} value={formatCurrency(total)} color="emerald" />
      </div>

      <Card>
        <div className="px-4 py-3 border-b border-slate-100 flex items-center justify-between">
          <h3 className="font-semibold text-slate-700">Charges for {monthName(month)} {year}</h3>
        </div>
        {charges.length === 0 ? (
          <EmptyState
            title={`No charges for ${monthName(month)} ${year}`}
            description="Start entering mess charges for students."
            action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> Add Charge</Btn>}
          />
        ) : (
          <Table headers={["#", "Student", "Amount", "Month/Year", "Actions"]}>
            {charges.map((c, i) => {
              const stu = students.find((s) => s.id === c.studentId);
              return (
                <Tr key={c.id}>
                  <Td>{i + 1}</Td>
                  <Td className="font-medium">{stu ? stu.name : `Student #${c.studentId}`}</Td>
                  <Td className="font-semibold text-emerald-600">{formatCurrency(c.amount)}</Td>
                  <Td className="text-slate-500">{monthName(c.month)} {c.year}</Td>
                  <Td>
                    <Btn size="sm" variant="danger" onClick={() => handleDelete(c.id)}>Delete</Btn>
                  </Td>
                </Tr>
              );
            })}
          </Table>
        )}
      </Card>

      {showModal && (
        <Modal title="Add Mess Charge" onClose={() => setShowModal(false)}>
          <form onSubmit={handleCreate} className="space-y-4">
            <FormField label="Student" required>
              <Select value={form.studentId} onChange={(e) => setForm({ ...form, studentId: e.target.value })} required>
                <option value="">Select student…</option>
                {students.filter((s) => !studentsWithCharge.has(s.id)).map((s) => (
                  <option key={s.id} value={s.id}>{s.name}{s.registrationNumber ? ` (${s.registrationNumber})` : ""}</option>
                ))}
              </Select>
            </FormField>
            <FormField label="Amount (₹)" required>
              <Input type="number" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} min="1" required placeholder="e.g. 3000" />
            </FormField>
            <p className="text-xs text-slate-400">Period: {monthName(month)} {year}</p>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>
                <Save className="w-4 h-4" />
                {submitting ? "Saving…" : "Save Charge"}
              </Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
