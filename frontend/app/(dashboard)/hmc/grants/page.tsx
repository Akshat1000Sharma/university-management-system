"use client";

import { useEffect, useState } from "react";
import { Plus, ArrowRight } from "lucide-react";
import { api } from "../../../lib/api";
import { formatCurrency, currentYear } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState,
  Modal, FormField, Input, Select, Textarea, Btn, StatCard
} from "../../../components/ui";
import type { Grant, Hall, HallGrant } from "../../../lib/types";

export default function HmcGrantsPage() {
  const [grants, setGrants] = useState<Grant[]>([]);
  const [halls, setHalls] = useState<Hall[]>([]);
  const [hallGrants, setHallGrants] = useState<HallGrant[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [showGrantModal, setShowGrantModal] = useState(false);
  const [showAllocModal, setShowAllocModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const [grantForm, setGrantForm] = useState({ grantName: "", totalAmount: "", description: "", year: String(currentYear()) });
  const [allocForm, setAllocForm] = useState({ grantId: "", hallId: "", allocatedAmount: "" });

  const load = async () => {
    try {
      const [g, h, hg] = await Promise.all([
        api.grants.getAll(),
        api.halls.getAll(),
        api.hallGrants.getAll(),
      ]);
      setGrants(g);
      setHalls(h);
      setHallGrants(hg);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleCreateGrant = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await api.grants.create({
        grantName: grantForm.grantName,
        totalAmount: Number(grantForm.totalAmount),
        description: grantForm.description,
        year: Number(grantForm.year),
      });
      setShowGrantModal(false);
      setGrantForm({ grantName: "", totalAmount: "", description: "", year: String(currentYear()) });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to create grant");
    } finally {
      setSubmitting(false);
    }
  };

  const handleAllocate = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await api.hallGrants.create({
        grantId: Number(allocForm.grantId),
        hallId: Number(allocForm.hallId),
        allocatedAmount: Number(allocForm.allocatedAmount),
      });
      setShowAllocModal(false);
      setAllocForm({ grantId: "", hallId: "", allocatedAmount: "" });
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to allocate");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteGrant = async (id: number) => {
    if (!confirm("Delete this grant?")) return;
    try { await api.grants.delete(id); await load(); }
    catch (e: unknown) { setError(e instanceof Error ? e.message : "Failed to delete"); }
  };

  if (loading) return <Spinner />;

  const totalGranted = grants.reduce((s, g) => s + g.totalAmount, 0);
  const totalAllocated = hallGrants.reduce((s, hg) => s + hg.allocatedAmount, 0);

  return (
    <div>
      <PageHeader
        title="Grant Management"
        subtitle="Create and distribute annual grants to halls"
        action={
          <div className="flex gap-2">
            <Btn variant="secondary" onClick={() => setShowAllocModal(true)}>
              <ArrowRight className="w-4 h-4" /> Allocate to Hall
            </Btn>
            <Btn onClick={() => setShowGrantModal(true)}>
              <Plus className="w-4 h-4" /> New Grant
            </Btn>
          </div>
        }
      />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-3 gap-4 mb-6">
        <StatCard label="Total Grants" value={grants.length} color="blue" />
        <StatCard label="Total Grant Amount" value={formatCurrency(totalGranted)} color="emerald" />
        <StatCard label="Total Allocated" value={formatCurrency(totalAllocated)} color="amber" />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Grants */}
        <Card>
          <div className="px-4 py-3 border-b border-slate-100 flex justify-between items-center">
            <h3 className="font-semibold text-slate-700">Grants</h3>
            <Btn size="sm" onClick={() => setShowGrantModal(true)}><Plus className="w-3 h-3" /> Add</Btn>
          </div>
          {grants.length === 0 ? (
            <EmptyState title="No grants yet" action={<Btn size="sm" onClick={() => setShowGrantModal(true)}><Plus className="w-3 h-3" /> Add Grant</Btn>} />
          ) : (
            <Table headers={["Grant Name", "Year", "Total Amount", ""]}>
              {grants.map((g) => (
                <Tr key={g.id}>
                  <Td className="font-medium">{g.grantName ?? `Grant #${g.id}`}</Td>
                  <Td className="text-slate-500">{g.year ?? "—"}</Td>
                  <Td className="font-semibold text-emerald-600">{formatCurrency(g.totalAmount)}</Td>
                  <Td><Btn size="sm" variant="danger" onClick={() => handleDeleteGrant(g.id)}>Delete</Btn></Td>
                </Tr>
              ))}
            </Table>
          )}
        </Card>

        {/* Hall Allocations */}
        <Card>
          <div className="px-4 py-3 border-b border-slate-100 flex justify-between items-center">
            <h3 className="font-semibold text-slate-700">Hall Allocations</h3>
            <Btn size="sm" onClick={() => setShowAllocModal(true)}><Plus className="w-3 h-3" /> Allocate</Btn>
          </div>
          {hallGrants.length === 0 ? (
            <EmptyState title="No allocations yet" action={<Btn size="sm" onClick={() => setShowAllocModal(true)}><Plus className="w-3 h-3" /> Allocate</Btn>} />
          ) : (
            <Table headers={["Hall", "Grant", "Allocated", "Spent"]}>
              {hallGrants.map((hg) => {
                const hall = halls.find((h) => h.id === hg.hallId);
                const grant = grants.find((g) => g.id === hg.grantId);
                return (
                  <Tr key={hg.id}>
                    <Td className="font-medium">{hall?.name ?? `#${hg.hallId}`}</Td>
                    <Td className="text-slate-500 text-xs">{grant?.grantName ?? `#${hg.grantId}`}</Td>
                    <Td className="font-semibold text-emerald-600">{formatCurrency(hg.allocatedAmount)}</Td>
                    <Td className="text-rose-600">{hg.spentAmount != null ? formatCurrency(hg.spentAmount) : "—"}</Td>
                  </Tr>
                );
              })}
            </Table>
          )}
        </Card>
      </div>

      {showGrantModal && (
        <Modal title="Create New Grant" onClose={() => setShowGrantModal(false)}>
          <form onSubmit={handleCreateGrant} className="space-y-4">
            <FormField label="Grant Name" required>
              <Input value={grantForm.grantName} onChange={(e) => setGrantForm({ ...grantForm, grantName: e.target.value })} placeholder="e.g. Annual Infrastructure Grant" required />
            </FormField>
            <FormField label="Year" required>
              <Input type="number" value={grantForm.year} onChange={(e) => setGrantForm({ ...grantForm, year: e.target.value })} required />
            </FormField>
            <FormField label="Total Amount (₹)" required>
              <Input type="number" value={grantForm.totalAmount} onChange={(e) => setGrantForm({ ...grantForm, totalAmount: e.target.value })} min="1" required />
            </FormField>
            <FormField label="Description">
              <Textarea value={grantForm.description} onChange={(e) => setGrantForm({ ...grantForm, description: e.target.value })} placeholder="Purpose of the grant" />
            </FormField>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowGrantModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>{submitting ? "Creating…" : "Create Grant"}</Btn>
            </div>
          </form>
        </Modal>
      )}

      {showAllocModal && (
        <Modal title="Allocate Grant to Hall" onClose={() => setShowAllocModal(false)}>
          <form onSubmit={handleAllocate} className="space-y-4">
            <FormField label="Grant" required>
              <Select value={allocForm.grantId} onChange={(e) => setAllocForm({ ...allocForm, grantId: e.target.value })} required>
                <option value="">Select grant…</option>
                {grants.map((g) => <option key={g.id} value={g.id}>{g.grantName ?? `Grant #${g.id}`} — {formatCurrency(g.totalAmount)}</option>)}
              </Select>
            </FormField>
            <FormField label="Hall" required>
              <Select value={allocForm.hallId} onChange={(e) => setAllocForm({ ...allocForm, hallId: e.target.value })} required>
                <option value="">Select hall…</option>
                {halls.map((h) => <option key={h.id} value={h.id}>{h.name}</option>)}
              </Select>
            </FormField>
            <FormField label="Allocated Amount (₹)" required>
              <Input type="number" value={allocForm.allocatedAmount} onChange={(e) => setAllocForm({ ...allocForm, allocatedAmount: e.target.value })} min="1" required />
            </FormField>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowAllocModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>{submitting ? "Allocating…" : "Allocate"}</Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
