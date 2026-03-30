"use client";

import { useEffect, useState } from "react";
import { Plus, Trash2 } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatDate } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState,
  Modal, FormField, Input, Select, Btn, StatCard
} from "../../../components/ui";
import type { Staff, StaffLeave } from "../../../lib/types";

const LEAVE_TYPES = ["CASUAL", "EARNED", "MEDICAL", "SPECIAL"];

export default function ClerkLeavesPage() {
  const { user } = useAuth();
  const [staff, setStaff] = useState<Staff[]>([]);
  const [leaves, setLeaves] = useState<StaffLeave[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    staffId: "", leaveType: "CASUAL", leaveDate: new Date().toISOString().split("T")[0],
  });
  const [selectedStaff, setSelectedStaff] = useState<number | "all">("all");

  const load = async () => {
    if (!user?.hallId) { setLoading(false); return; }
    try {
      const [s, l] = await Promise.all([
        api.staff.getByHall(user.hallId),
        api.staffLeaves.getAll(),
      ]);
      setStaff(s);
      const hallStaffIds = new Set(s.map((st) => st.id));
      setLeaves(l.filter((lv) => hallStaffIds.has(lv.staffId)));
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [user]);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user?.hallId) return;
    setSubmitting(true);
    try {
      await api.staffLeaves.create({
        staffId: Number(form.staffId),
        leaveType: form.leaveType,
        leaveDate: form.leaveDate,
        hallId: user.hallId,
      });
      setShowModal(false);
      setForm({ staffId: "", leaveType: "CASUAL", leaveDate: new Date().toISOString().split("T")[0] });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to add leave");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Delete this leave record?")) return;
    try { await api.staffLeaves.delete(id); await load(); }
    catch (e: unknown) { setError(e instanceof Error ? e.message : "Failed to delete"); }
  };

  if (loading) return <Spinner />;

  const filtered = selectedStaff === "all"
    ? leaves
    : leaves.filter((l) => l.staffId === selectedStaff);

  const leavesThisMonth = leaves.filter((l) => {
    const d = new Date(l.leaveDate);
    const now = new Date();
    return d.getMonth() === now.getMonth() && d.getFullYear() === now.getFullYear();
  });

  return (
    <div>
      <PageHeader
        title="Staff Leave Records"
        subtitle={user?.hallName}
        action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> Record Leave</Btn>}
      />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mb-6">
        <StatCard label="Total Staff" value={staff.length} color="blue" />
        <StatCard label="Total Leaves" value={leaves.length} color="amber" />
        <StatCard label="Leaves This Month" value={leavesThisMonth.length} color="rose" />
      </div>

      <Card className="p-4 mb-4 flex items-center gap-3">
        <label className="text-sm font-medium text-slate-600">Filter by staff:</label>
        <Select
          value={selectedStaff}
          onChange={(e) => setSelectedStaff(e.target.value === "all" ? "all" : Number(e.target.value))}
          className="w-52"
        >
          <option value="all">All Staff</option>
          {staff.map((s) => <option key={s.id} value={s.id}>{s.name}</option>)}
        </Select>
      </Card>

      <Card>
        {filtered.length === 0 ? (
          <EmptyState
            title="No leave records found"
            action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> Record Leave</Btn>}
          />
        ) : (
          <Table headers={["#", "Staff Member", "Type", "Leave Date", "Actions"]}>
            {filtered.map((l, i) => {
              const s = staff.find((st) => st.id === l.staffId);
              return (
                <Tr key={l.id}>
                  <Td>{i + 1}</Td>
                  <Td className="font-medium">{s ? s.name : `Staff #${l.staffId}`}</Td>
                  <Td>
                    <span className="text-xs bg-blue-50 text-blue-700 px-2 py-0.5 rounded-full">
                      {l.leaveType || "CASUAL"}
                    </span>
                  </Td>
                  <Td>{formatDate(l.leaveDate)}</Td>
                  <Td>
                    <Btn size="sm" variant="danger" onClick={() => handleDelete(l.id)}>
                      <Trash2 className="w-3 h-3" />
                    </Btn>
                  </Td>
                </Tr>
              );
            })}
          </Table>
        )}
      </Card>

      {showModal && (
        <Modal title="Record Staff Leave" onClose={() => setShowModal(false)}>
          <form onSubmit={handleCreate} className="space-y-4">
            <FormField label="Staff Member" required>
              <Select value={form.staffId} onChange={(e) => setForm({ ...form, staffId: e.target.value })} required>
                <option value="">Select staff member…</option>
                {staff.map((s) => <option key={s.id} value={s.id}>{s.name} ({s.staffType})</option>)}
              </Select>
            </FormField>
            <FormField label="Leave Type" required>
              <Select value={form.leaveType} onChange={(e) => setForm({ ...form, leaveType: e.target.value })}>
                {LEAVE_TYPES.map((t) => <option key={t} value={t}>{t}</option>)}
              </Select>
            </FormField>
            <FormField label="Leave Date" required>
              <Input type="date" value={form.leaveDate} onChange={(e) => setForm({ ...form, leaveDate: e.target.value })} required />
            </FormField>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>{submitting ? "Saving…" : "Record Leave"}</Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
