"use client";

import { useEffect, useState } from "react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { PageHeader, Card, StatCard, Spinner, ErrorMsg, OccupancyBar, Table, Tr, Td } from "../../../components/ui";
import type { HallOccupancy } from "../../../lib/types";

export default function OverallOccupancyPage() {
  const [occupancies, setOccupancies] = useState<HallOccupancy[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const data = await api.business.getOverallOccupancy();
        setOccupancies(data);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load occupancy data");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <Spinner />;

  const totalRooms = occupancies.reduce((s, o) => s + o.totalRooms, 0);
  const totalOccupied = occupancies.reduce((s, o) => s + o.occupiedRooms, 0);
  const totalEmpty = occupancies.reduce((s, o) => s + o.vacantRooms, 0);
  const overallPct = totalRooms > 0 ? (totalOccupied / totalRooms) * 100 : 0;

  return (
    <div>
      <PageHeader title="Overall Room Occupancy" subtitle="System-wide occupancy across all halls" />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <StatCard label="Total Halls" value={occupancies.length} color="blue" />
        <StatCard label="Total Rooms" value={totalRooms} color="indigo" />
        <StatCard label="Occupied" value={totalOccupied} color="emerald" />
        <StatCard label="Vacant" value={totalEmpty} color="amber" />
      </div>

      <Card className="p-5 mb-6">
        <p className="font-semibold text-slate-700 mb-2">System-Wide Occupancy Rate</p>
        <p className="text-4xl font-bold text-slate-900 mb-3">{overallPct.toFixed(1)}%</p>
        <OccupancyBar percentage={overallPct} />
      </Card>

      <Card>
        <div className="px-4 py-3 border-b border-slate-100">
          <h3 className="font-semibold text-slate-700">Per-Hall Breakdown</h3>
        </div>
        {occupancies.length === 0 ? (
          <div className="text-center py-10 text-slate-400 text-sm">No halls found</div>
        ) : (
          <>
            <Table headers={["Hall", "Total Rooms", "Occupied", "Vacant", "Occupancy %"]}>
              {occupancies.map((o) => (
                <Tr key={o.hallId}>
                  <Td className="font-semibold">{o.hallName}</Td>
                  <Td>{o.totalRooms}</Td>
                  <Td>
                    <span className="text-emerald-700 font-medium">{o.occupiedRooms}</span>
                  </Td>
                  <Td>
                    <span className="text-amber-700 font-medium">{o.vacantRooms}</span>
                  </Td>
                  <Td>
                    <div className="flex items-center gap-3 min-w-40">
                      <OccupancyBar percentage={o.occupancyPercent} />
                    </div>
                  </Td>
                </Tr>
              ))}
            </Table>
            <div className="border-t border-slate-100 p-4 space-y-3">
              {occupancies.map((o) => (
                <div key={o.hallId}>
                  <div className="flex justify-between text-sm mb-1">
                    <span className="font-medium text-slate-700">{o.hallName}</span>
                    <span className="text-slate-500">{o.occupiedRooms}/{o.totalRooms} — {o.occupancyPercent.toFixed(1)}%</span>
                  </div>
                  <OccupancyBar percentage={o.occupancyPercent} />
                </div>
              ))}
            </div>
          </>
        )}
      </Card>
    </div>
  );
}
