"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { Utensils, DollarSign, Users, Phone, MapPin } from "lucide-react";
import { useAuth } from "../../lib/auth";
import { api } from "../../lib/api";
import { formatCurrency, currentMonth, currentYear } from "../../lib/utils";
import { PageHeader, StatCard, Spinner, ErrorMsg, Card, Table, Tr, Td } from "../../components/ui";
import type { MessPaymentSheet, Student } from "../../lib/types";

export default function MessManagerDashboard() {
  const { user } = useAuth();
  const [paymentSheet, setPaymentSheet] = useState<MessPaymentSheet | null>(null);
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [chargeCount, setChargeCount] = useState<number | null>(null);

  useEffect(() => {
    if (!user?.hallId) { setLoading(false); return; }
    (async () => {
      try {
        const [ps, stu, charges] = await Promise.allSettled([
          api.business.getMessPayment(user.hallId!, currentMonth(), currentYear()),
          api.students.getByHall(user.hallId!),
          api.messCharges.getByHallMonthYear(user.hallId!, currentMonth(), currentYear()),
        ]);
        if (ps.status === "fulfilled") setPaymentSheet(ps.value);
        if (stu.status === "fulfilled") setStudents(stu.value);
        if (charges.status === "fulfilled") setChargeCount(charges.value.length);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load");
      } finally {
        setLoading(false);
      }
    })();
  }, [user]);

  if (loading) return <Spinner />;

  const perStudentCharge =
    paymentSheet?.monthlyCharge != null && Number.isFinite(paymentSheet.monthlyCharge)
      ? paymentSheet.monthlyCharge
      : chargeCount != null && chargeCount > 0 && paymentSheet
        ? paymentSheet.totalAmount / chargeCount
        : undefined;

  return (
    <div>
      <PageHeader title="Mess Manager Dashboard" subtitle={`${user?.hallName} — ${user?.name}`} />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mb-6">
        <StatCard label="Students in Hall" value={students.length} icon={<Users className="w-5 h-5" />} color="blue" />
        <StatCard
          label="Total Due This Month"
          value={paymentSheet ? formatCurrency(paymentSheet.totalAmount) : "—"}
          icon={<DollarSign className="w-5 h-5" />}
          color="emerald"
        />
        <StatCard
          label="Per Student Charge"
          value={perStudentCharge != null ? formatCurrency(perStudentCharge) : "—"}
          icon={<Utensils className="w-5 h-5" />}
          color="amber"
        />
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
        <Link href="/mess-manager/charges" className="bg-white border border-slate-200 rounded-xl p-5 hover:border-indigo-300 hover:shadow-sm transition-all group">
          <Utensils className="w-6 h-6 text-orange-600 mb-3" />
          <p className="font-semibold text-slate-900 group-hover:text-indigo-700">Mess Charges</p>
          <p className="text-sm text-slate-500 mt-1">Enter monthly mess charges for students</p>
        </Link>
        <Link href="/mess-manager/payment-sheet" className="bg-white border border-slate-200 rounded-xl p-5 hover:border-emerald-300 hover:shadow-sm transition-all group">
          <DollarSign className="w-6 h-6 text-emerald-600 mb-3" />
          <p className="font-semibold text-slate-900 group-hover:text-emerald-700">Payment Sheet</p>
          <p className="text-sm text-slate-500 mt-1">View monthly payment owed to you</p>
        </Link>
      </div>

      {students.length > 0 && (
        <Card>
          <div className="px-4 py-3 border-b border-slate-100 flex items-center gap-2">
            <Users className="w-4 h-4 text-slate-400" />
            <h3 className="font-semibold text-slate-700">Students in Hall</h3>
            <span className="ml-auto text-xs text-slate-400 bg-slate-100 px-2 py-0.5 rounded-full">{students.length}</span>
          </div>
          <Table headers={["#", "Name", "Room", "Phone", "Address"]}>
            {students.map((s, i) => (
              <Tr key={s.id}>
                <Td className="text-slate-400">{i + 1}</Td>
                <Td className="font-medium text-slate-900">{s.name}</Td>
                <Td>{s.roomId ? `Room #${s.roomId}` : <span className="text-slate-400">—</span>}</Td>
                <Td>
                  <span className="flex items-center gap-1 text-slate-600">
                    <Phone className="w-3 h-3" /> {s.phone}
                  </span>
                </Td>
                <Td className="text-slate-500 max-w-xs truncate">
                  <span className="flex items-center gap-1">
                    <MapPin className="w-3 h-3 shrink-0" /> {s.address ?? "—"}
                  </span>
                </Td>
              </Tr>
            ))}
          </Table>
        </Card>
      )}
    </div>
  );
}
