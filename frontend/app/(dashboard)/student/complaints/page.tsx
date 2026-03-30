"use client";

import { useEffect, useState } from "react";
import { Plus, AlertCircle } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatDate, COMPLAINT_TYPES } from "../../../lib/utils";
import {
  PageHeader, Card, Badge, Table, Tr, Td, Spinner, ErrorMsg,
  EmptyState, Modal, FormField, Input, Select, Textarea, Btn
} from "../../../components/ui";
import type { Complaint } from "../../../lib/types";

const COMPLAINT_TYPE_OPTIONS = [
  { value: "MAINTENANCE", label: "Maintenance / Repair" },
  { value: "FOOD_QUALITY", label: "Food Quality" },
  { value: "DISCIPLINE", label: "Discipline" },
  { value: "OTHER", label: "Other" },
];

export default function StudentComplaintsPage() {
  const { user } = useAuth();
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    title: "",
    description: "",
    complaintType: "MAINTENANCE",
  });

  const load = async () => {
    if (!user?.studentId) { setLoading(false); return; }
    try {
      const data = await api.complaints.getByStudent(user.studentId);
      setComplaints(data);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load complaints");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [user]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user?.studentId || !user?.hallId) return;
    setSubmitting(true);
    try {
      await api.complaints.create({
        ...form,
        complaintType: form.complaintType as Complaint["complaintType"],
        complaintStatus: "PENDING",
        complaintDate: new Date().toISOString().split("T")[0],
        studentId: user.studentId,
        hallId: user.hallId,
      });
      setShowModal(false);
      setForm({ title: "", description: "", complaintType: "MAINTENANCE" });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to submit complaint");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <Spinner />;

  return (
    <div>
      <PageHeader
        title="My Complaints"
        subtitle="Raise and track maintenance or other complaints"
        action={
          <Btn onClick={() => setShowModal(true)}>
            <Plus className="w-4 h-4" />
            New Complaint
          </Btn>
        }
      />

      {error && <ErrorMsg message={error} />}

      <Card>
        {complaints.length === 0 ? (
          <EmptyState
            title="No complaints yet"
            description="Raise a complaint if you face any issues in the hall."
            action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> New Complaint</Btn>}
          />
        ) : (
          <Table headers={["#", "Title", "Type", "Status", "Date", "ATR"]}>
            {complaints.map((c, i) => (
              <Tr key={c.id}>
                <Td>{i + 1}</Td>
                <Td className="font-medium">{c.title || c.description?.slice(0, 40)}</Td>
                <Td>
                  <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">
                    {(c.complaintType || c.type || "").replace(/_/g, " ")}
                  </span>
                </Td>
                <Td><Badge status={c.complaintStatus || c.status || "PENDING"} /></Td>
                <Td className="text-slate-500 text-xs">{formatDate(c.complaintDate || "")}</Td>
                <Td className="text-xs text-slate-500 max-w-xs truncate">
                  {c.atr ? (
                    <span className="text-emerald-700">{c.atr}</span>
                  ) : (
                    <span className="text-slate-300">—</span>
                  )}
                </Td>
              </Tr>
            ))}
          </Table>
        )}
      </Card>

      {showModal && (
        <Modal title="Raise a Complaint" onClose={() => setShowModal(false)}>
          <form onSubmit={handleSubmit} className="space-y-4">
            <FormField label="Title" required>
              <Input
                value={form.title}
                onChange={(e) => setForm({ ...form, title: e.target.value })}
                placeholder="Brief title for your complaint"
                required
              />
            </FormField>
            <FormField label="Complaint Type" required>
              <Select
                value={form.complaintType}
                onChange={(e) => setForm({ ...form, complaintType: e.target.value })}
              >
                {COMPLAINT_TYPE_OPTIONS.map((o) => (
                  <option key={o.value} value={o.value}>{o.label}</option>
                ))}
              </Select>
            </FormField>
            <FormField label="Description" required>
              <Textarea
                value={form.description}
                onChange={(e) => setForm({ ...form, description: e.target.value })}
                placeholder="Describe your complaint in detail..."
                required
              />
            </FormField>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>
                {submitting ? "Submitting…" : "Submit Complaint"}
              </Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
