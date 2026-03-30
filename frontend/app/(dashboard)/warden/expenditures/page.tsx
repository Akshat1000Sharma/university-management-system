"use client";

import { useEffect, useState } from "react";
import { Plus } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency, formatDate, EXPENSE_CATEGORIES } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState,
  Modal, FormField, Input, Select, Textarea, Btn, StatCard
} from "../../../components/ui";
import type { Expenditure } from "../../../lib/types";

export default function WardenExpendituresPage() {
  const { user } = useAuth();
  const [expenditures, setExpenditures] = useState<Expenditure[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    description: "", amount: "", date: new Date().toISOString().split("T")[0],
    expenseCategory: "MAINTENANCE",
  });

  const load = async () => {
    if (!user?.hallId) { setLoading(false); return; }
    try {
      const data = await api.expenditures.getByHall(user.hallId);
      setExpenditures(data);
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
      await api.expenditures.create({
        ...form,
        amount: Number(form.amount),
        hallId: user.hallId,
      });
      setShowModal(false);
      setForm({ description: "", amount: "", date: new Date().toISOString().split("T")[0], expenseCategory: "MAINTENANCE" });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to add");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Delete this expenditure?")) return;
    try { await api.expenditures.delete(id); await load(); }
    catch (e: unknown) { setError(e instanceof Error ? e.message : "Failed to delete"); }
  };

  if (loading) return <Spinner />;

  const total = expenditures.reduce((s, e) => s + e.amount, 0);
  const byCategory = expenditures.reduce<Record<string, number>>((acc, e) => {
    const cat = e.expenseCategory ?? e.category ?? "OTHER";
    acc[cat] = (acc[cat] || 0) + e.amount;
    return acc;
  }, {});

  return (
    <div>
      <PageHeader
        title="Expenditures"
        subtitle={user?.hallName}
        action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> Add Expense</Btn>}
      />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <StatCard label="Total Expenditure" value={formatCurrency(total)} color="rose" />
        {Object.entries(byCategory).slice(0, 3).map(([cat, amt]) => (
          <StatCard key={cat} label={cat} value={formatCurrency(amt)} color="amber" />
        ))}
      </div>

      <Card>
        {expenditures.length === 0 ? (
          <EmptyState title="No expenditures recorded" action={<Btn onClick={() => setShowModal(true)}><Plus className="w-4 h-4" /> Add Expense</Btn>} />
        ) : (
          <Table headers={["#", "Description", "Category", "Amount", "Date", "Actions"]}>
            {expenditures.map((e, i) => (
              <Tr key={e.id}>
                <Td>{i + 1}</Td>
                <Td className="font-medium max-w-xs truncate">{e.description}</Td>
                <Td>
                  <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">
                    {e.expenseCategory ?? e.category}
                  </span>
                </Td>
                <Td className="font-semibold text-rose-600">{formatCurrency(e.amount)}</Td>
                <Td className="text-slate-400 text-xs">{formatDate(e.date)}</Td>
                <Td>
                  <Btn size="sm" variant="danger" onClick={() => handleDelete(e.id)}>Delete</Btn>
                </Td>
              </Tr>
            ))}
          </Table>
        )}
      </Card>

      {showModal && (
        <Modal title="Add Expenditure" onClose={() => setShowModal(false)}>
          <form onSubmit={handleCreate} className="space-y-4">
            <FormField label="Description" required>
              <Textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} placeholder="What was the expense for?" required />
            </FormField>
            <FormField label="Category" required>
              <Select value={form.expenseCategory} onChange={(e) => setForm({ ...form, expenseCategory: e.target.value })}>
                {EXPENSE_CATEGORIES.map((c) => <option key={c} value={c}>{c}</option>)}
              </Select>
            </FormField>
            <FormField label="Amount (₹)" required>
              <Input type="number" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} min="1" required />
            </FormField>
            <FormField label="Date" required>
              <Input type="date" value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} required />
            </FormField>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>{submitting ? "Saving…" : "Save Expenditure"}</Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
