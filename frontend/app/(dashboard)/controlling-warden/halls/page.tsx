"use client";

import { useEffect, useState } from "react";
import { api } from "../../../lib/api";
import { formatCurrency } from "../../../lib/utils";
import { PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState } from "../../../components/ui";
import type { Hall, Warden } from "../../../lib/types";

export default function AllHallsPage() {
  const [halls, setHalls] = useState<Hall[]>([]);
  const [wardens, setWardens] = useState<Warden[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const [h, w] = await Promise.all([api.halls.getAll(), api.wardens.getAll()]);
        setHalls(h);
        setWardens(w);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <Spinner />;

  return (
    <div>
      <PageHeader title="All Halls" subtitle="Overview of all halls and their wardens" />
      {error && <ErrorMsg message={error} />}

      <Card>
        {halls.length === 0 ? (
          <EmptyState title="No halls found" description="No halls have been created yet." />
        ) : (
          <Table headers={["#", "Hall Name", "Type", "Amenity Charge", "Warden", "Contact"]}>
            {halls.map((h, i) => {
              const warden = wardens.find((w) => w.hallId === h.id);
              // Backend boolean `isNew` is serialized as JSON property `new`.
              // Support both shapes to avoid "New/Old" classification bugs.
              const isNew = (h as unknown as { isNew?: boolean; new?: boolean }).isNew ??
                (h as unknown as { new?: boolean }).new;
              return (
                <Tr key={h.id}>
                  <Td>{i + 1}</Td>
                  <Td className="font-semibold">{h.name}</Td>
                  <Td>
                    <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${isNew ? "bg-emerald-50 text-emerald-700" : "bg-slate-100 text-slate-600"}`}>
                      {isNew ? "New" : "Old"}
                    </span>
                  </Td>
                  <Td>{formatCurrency(h.amenityCharge)}</Td>
                  <Td className="text-slate-700">{warden ? warden.name : <span className="text-slate-400">Not assigned</span>}</Td>
                  <Td className="text-slate-500 text-sm">{warden?.phone ?? "+01-1234567890"}</Td>
                </Tr>
              );
            })}
          </Table>
        )}
      </Card>
    </div>
  );
}
