"use client";

import { useEffect, useState } from "react";
import { CheckCircle } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatDate } from "../../../lib/utils";
import {
  PageHeader, Card, Badge, Table, Tr, Td, Spinner, ErrorMsg,
  EmptyState, Modal, FormField, Textarea, Btn
} from "../../../components/ui";
import type { Complaint } from "../../../lib/types";

export default function WardenComplaintsPage() {
  const { user } = useAuth();
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selected, setSelected] = useState<Complaint | null>(null);
  const [atr, setAtr] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [successId, setSuccessId] = useState<number | null>(null);

  const load = async () => {
    if (!user?.hallId) { setLoading(false); return; }
    try {
      const data = await api.complaints.getByHall(user.hallId);
      setComplaints(data);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [user]);

  const openAtr = (c: Complaint) => {
    setSelected(c);
    setAtr(c.atr ?? "");
  };

  const submitAtr = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selected) return;
    setSubmitting(true);
    try {
      await api.complaints.postAtr(selected.id, atr);
      setSuccessId(selected.id);
      setSelected(null);
      await load();
      setTimeout(() => setSuccessId(null), 3000);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to post ATR");
    } finally {
      setSubmitting(false);
    }
  };

  const updateStatus = async (c: Complaint, status: Complaint["complaintStatus"]) => {
    try {
      await api.complaints.update(c.id, { ...c, complaintStatus: status });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to update");
    }
  };

  if (loading) return <Spinner />;

  const pending = complaints.filter((c) => (c.complaintStatus ?? c.status) === "PENDING");
  const others = complaints.filter((c) => (c.complaintStatus ?? c.status) !== "PENDING");

  return (
    <div>
      <PageHeader title="Complaints & ATR" subtitle="Review complaints and post Action Taken Reports" />
      {error && <ErrorMsg message={error} />}

      {successId && (
        <div className="bg-emerald-50 border border-emerald-200 text-emerald-700 rounded-lg px-4 py-3 mb-4 flex items-center gap-2 text-sm">
          <CheckCircle className="w-4 h-4" />
          ATR posted successfully for complaint #{successId}
        </div>
      )}

      {complaints.length === 0 ? (
        <EmptyState title="No complaints in your hall" description="Complaints raised by students will appear here." />
      ) : (
        <>
          {pending.length > 0 && (
            <div className="mb-6">
              <h2 className="text-sm font-semibold text-amber-700 uppercase tracking-wide mb-3">
                Pending ({pending.length})
              </h2>
              <Card>
                <Table headers={["#", "Title/Description", "Type", "Student", "Date", "Actions"]}>
                  {pending.map((c) => (
                    <Tr key={c.id}>
                      <Td>{c.id}</Td>
                      <Td className="font-medium max-w-xs truncate">{c.title || c.description?.slice(0, 50)}</Td>
                      <Td>
                        <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">
                          {(c.complaintType || c.type || "").replace(/_/g, " ")}
                        </span>
                      </Td>
                      <Td className="text-slate-500 text-sm">Student #{c.studentId}</Td>
                      <Td className="text-slate-400 text-xs">{formatDate(c.complaintDate || "")}</Td>
                      <Td>
                        <div className="flex gap-1.5">
                          <Btn size="sm" onClick={() => updateStatus(c, "IN_PROGRESS")}>In Progress</Btn>
                          <Btn size="sm" variant="secondary" onClick={() => openAtr(c)}>Post ATR</Btn>
                        </div>
                      </Td>
                    </Tr>
                  ))}
                </Table>
              </Card>
            </div>
          )}

          <div>
            <h2 className="text-sm font-semibold text-slate-500 uppercase tracking-wide mb-3">
              All Complaints ({complaints.length})
            </h2>
            <Card>
              <Table headers={["#", "Description", "Type", "Status", "Date", "ATR", "Action"]}>
                {complaints.map((c) => (
                  <Tr key={c.id}>
                    <Td>{c.id}</Td>
                    <Td className="max-w-xs truncate text-sm">{c.title || c.description?.slice(0, 50)}</Td>
                    <Td>
                      <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">
                        {(c.complaintType || c.type || "").replace(/_/g, " ")}
                      </span>
                    </Td>
                    <Td><Badge status={c.complaintStatus ?? c.status ?? "PENDING"} /></Td>
                    <Td className="text-slate-400 text-xs">{formatDate(c.complaintDate || "")}</Td>
                    <Td className="text-xs text-slate-500 max-w-xs truncate">
                      {c.atr ? <span className="text-emerald-700">{c.atr}</span> : <span className="text-slate-300">—</span>}
                    </Td>
                    <Td>
                      <Btn size="sm" variant="secondary" onClick={() => openAtr(c)}>
                        {c.atr ? "Edit ATR" : "Post ATR"}
                      </Btn>
                    </Td>
                  </Tr>
                ))}
              </Table>
            </Card>
          </div>
        </>
      )}

      {selected && (
        <Modal title={`Post ATR — Complaint #${selected.id}`} onClose={() => setSelected(null)}>
          <div className="bg-slate-50 rounded-lg p-3 mb-4 text-sm">
            <p className="font-semibold text-slate-700">{selected.title || "Complaint"}</p>
            <p className="text-slate-500 mt-1">{selected.description}</p>
          </div>
          <form onSubmit={submitAtr} className="space-y-4">
            <FormField label="Action Taken Report" required>
              <Textarea
                value={atr}
                onChange={(e) => setAtr(e.target.value)}
                placeholder="Describe what action was taken to resolve this complaint..."
                rows={4}
                required
              />
            </FormField>
            <div className="flex justify-end gap-2">
              <Btn variant="secondary" onClick={() => setSelected(null)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>
                <CheckCircle className="w-4 h-4" />
                {submitting ? "Posting…" : "Post ATR"}
              </Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
