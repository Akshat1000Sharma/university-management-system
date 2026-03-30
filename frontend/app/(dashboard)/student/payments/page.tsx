"use client";

import { useEffect, useState } from "react";
import { CreditCard, CheckCircle } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency, formatDate, currentMonth, currentYear } from "../../../lib/utils";
import {
  PageHeader, Card, Badge, Table, Tr, Td, Spinner, ErrorMsg,
  EmptyState, Modal, FormField, Input, Btn, StatCard
} from "../../../components/ui";
import type { Payment, StudentDues } from "../../../lib/types";

export default function StudentPaymentsPage() {
  const { user } = useAuth();
  const [payments, setPayments] = useState<Payment[]>([]);
  const [dues, setDues] = useState<StudentDues | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showPayModal, setShowPayModal] = useState(false);
  const [payAmount, setPayAmount] = useState("");
  const [paying, setPaying] = useState(false);
  const [paySuccess, setPaySuccess] = useState(false);

  const load = async () => {
    if (!user?.studentId) { setLoading(false); return; }
    try {
      const [p, d] = await Promise.all([
        api.payments.getByStudent(user.studentId),
        api.business.getStudentDues(user.studentId, currentMonth(), currentYear()),
      ]);
      setPayments(p);
      setDues(d);
      if (d) setPayAmount(String(d.totalDue));
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load payments");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, [user]);

  const handlePay = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user?.studentId || !user?.hallId) return;
    setPaying(true);
    try {
      await api.payments.create({
        paymentAmount: Number(payAmount),
        paymentDate: new Date().toISOString().split("T")[0],
        paymentStatus: "COMPLETED",
        studentId: user.studentId,
        hallId: user.hallId,
      });
      setPaySuccess(true);
      setShowPayModal(false);
      await load();
      setTimeout(() => setPaySuccess(false), 3000);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Payment failed");
    } finally {
      setPaying(false);
    }
  };

  const totalPaid = payments.reduce((s, p) => s + (p.paymentAmount ?? p.amount ?? 0), 0);

  if (loading) return <Spinner />;

  return (
    <div>
      <PageHeader
        title="Payments"
        subtitle="Your payment history and outstanding dues"
        action={
          <Btn onClick={() => setShowPayModal(true)}>
            <CreditCard className="w-4 h-4" />
            Pay Dues
          </Btn>
        }
      />

      {error && <ErrorMsg message={error} />}

      {paySuccess && (
        <div className="bg-emerald-50 border border-emerald-200 text-emerald-700 rounded-lg px-4 py-3 mb-4 flex items-center gap-2 text-sm">
          <CheckCircle className="w-4 h-4" />
          Payment recorded successfully!
        </div>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
        <StatCard
          label="Total Due (This Month)"
          value={dues ? formatCurrency(dues.totalDue) : "—"}
          color="rose"
        />
        <StatCard
          label="Total Paid (All Time)"
          value={formatCurrency(totalPaid)}
          color="emerald"
        />
        <StatCard
          label="Total Transactions"
          value={payments.length}
          color="blue"
        />
      </div>

      <Card>
        {payments.length === 0 ? (
          <EmptyState title="No payment records" description="Your payment history will appear here." />
        ) : (
          <Table headers={["#", "Amount", "Date", "Status"]}>
            {payments.map((p, i) => (
              <Tr key={p.id}>
                <Td>{i + 1}</Td>
                <Td className="font-semibold">{formatCurrency(p.paymentAmount ?? p.amount ?? 0)}</Td>
                <Td className="text-slate-500 text-sm">{formatDate(p.paymentDate ?? p.date ?? "")}</Td>
                <Td><Badge status={p.paymentStatus ?? "COMPLETED"} /></Td>
              </Tr>
            ))}
          </Table>
        )}
      </Card>

      {showPayModal && (
        <Modal title="Pay Dues" onClose={() => setShowPayModal(false)}>
          <form onSubmit={handlePay} className="space-y-4">
            {dues && (
              <div className="bg-slate-50 rounded-lg p-4 text-sm space-y-1.5">
                <div className="flex justify-between">
                  <span className="text-slate-500">Mess Charges</span>
                  <span>{formatCurrency(dues.messCharge)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500">Room Rent</span>
                  <span>{formatCurrency(dues.roomRent)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500">Amenity Charges</span>
                  <span>{formatCurrency(dues.amenityCharge)}</span>
                </div>
                <div className="flex justify-between font-bold border-t border-slate-200 pt-1.5 mt-1.5">
                  <span>Total Due</span>
                  <span className="text-rose-600">{formatCurrency(dues.totalDue)}</span>
                </div>
              </div>
            )}

            <FormField label="Payment Amount (₹)" required>
              <Input
                type="number"
                value={payAmount}
                onChange={(e) => setPayAmount(e.target.value)}
                min="1"
                required
              />
            </FormField>

            <p className="text-xs text-slate-400">
              This is a demo payment. No real transaction will be processed.
            </p>

            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowPayModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={paying}>
                <CreditCard className="w-4 h-4" />
                {paying ? "Processing…" : "Confirm Payment"}
              </Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
