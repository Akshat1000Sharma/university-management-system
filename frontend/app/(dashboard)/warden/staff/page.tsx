"use client";

import { useEffect, useState } from "react";
import { Plus, Trash2 } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { STAFF_TYPES } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState,
  Modal, FormField, Input, Select, Btn, StatCard
} from "../../../components/ui";
import type { Staff } from "../../../lib/types";

export default function WardenStaffPage() {
  const { user } = useAuth();
  const [staff, setStaff] = useState<Staff[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({ name: "", staffType: "ATTENDANT", dailyPay: "" });

  const load = async () => {
    if (!user?.hallId) { setLoading(false); return; }
    try {
      const data = await api.staff.getByHall(user.hallId);
      setStaff(data);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load staff");
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
      await api.staff.create({
        name: form.name,
        staffType: form.staffType as Staff["staffType"],
        dailyPay: Number(form.dailyPay),
        hallId: user.hallId,
      });
      setShowModal(false);
      setForm({ name: "", staffType: "ATTENDANT", dailyPay: "" });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to create staff");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Remove this staff member?")) return;
    try {
      await api.staff.delete(id);
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to delete");
    }
  };

  if (loading) return <Spinner />;

  const byType = (type: string) => staff.filter((s) => s.staffType === type).length;

  return (
    <div>
      <PageHeader
        title="Staff Management"
        subtitle={user?.hallName}
        action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> Add Staff</Btn>}
      />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-3 gap-4 mb-6">
        <StatCard label="Total Staff" value={staff.length} color="blue" />
        <StatCard label="Attendants" value={byType("ATTENDANT")} color="emerald" />
        <StatCard label="Gardeners" value={byType("GARDENER")} color="amber" />
      </div>

      <Card>
        {staff.length === 0 ? (
          <EmptyState title="No staff yet" action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> Add Staff</Btn>} />
        ) : (
          <Table headers={["#", "Name", "Type", "Daily Pay", "Est. Monthly", "Actions"]}>
            {staff.map((s, i) => (
              <Tr key={s.id}>
                <Td>{i + 1}</Td>
                <Td className="font-medium">{s.name}</Td>
                <Td>
                  <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">
                    {s.staffType}
                  </span>
                </Td>
                <Td>₹{s.dailyPay}/day</Td>
                <Td className="text-slate-500">≈ ₹{(s.dailyPay * 26).toLocaleString("en-IN")}</Td>
                <Td>
                  <Btn size="sm" variant="danger" onClick={() => handleDelete(s.id)}>
                    <Trash2 className="w-3 h-3" />
                  </Btn>
                </Td>
              </Tr>
            ))}
          </Table>
        )}
      </Card>

      {showModal && (
        <Modal title="Add Staff Member" onClose={() => setShowModal(false)}>
          <form onSubmit={handleCreate} className="space-y-4">
            <FormField label="Full Name" required>
              <Input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required placeholder="Staff member name" />
            </FormField>
            <FormField label="Staff Type" required>
              <Select value={form.staffType} onChange={(e) => setForm({ ...form, staffType: e.target.value })}>
                {STAFF_TYPES.map((t) => <option key={t} value={t}>{t}</option>)}
              </Select>
            </FormField>
            <FormField label="Daily Pay (₹)" required>
              <Input type="number" value={form.dailyPay} onChange={(e) => setForm({ ...form, dailyPay: e.target.value })} min="1" required placeholder="e.g. 500" />
            </FormField>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>{submitting ? "Adding…" : "Add Staff"}</Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
