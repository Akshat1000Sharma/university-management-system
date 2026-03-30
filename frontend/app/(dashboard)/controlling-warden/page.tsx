"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { BarChart3, Building2, MessageSquare } from "lucide-react";
import { useAuth } from "../../lib/auth";
import { api } from "../../lib/api";
import { PageHeader, StatCard, Card, Spinner, ErrorMsg, OccupancyBar } from "../../components/ui";
import type { HallOccupancy, Hall, Complaint } from "../../lib/types";

export default function ControllingWardenDashboard() {
  const { user } = useAuth();
  const [occupancies, setOccupancies] = useState<HallOccupancy[]>([]);
  const [halls, setHalls] = useState<Hall[]>([]);
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const [occ, h, comp] = await Promise.all([
          api.business.getOverallOccupancy(),
          api.halls.getAll(),
          api.complaints.getAll(),
        ]);
        setOccupancies(occ);
        setHalls(h);
        setComplaints(comp);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <Spinner />;

  const totalRooms = occupancies.reduce((s, o) => s + o.totalRooms, 0);
  const totalOccupied = occupancies.reduce((s, o) => s + o.occupiedRooms, 0);
  const overallPct = totalRooms > 0 ? (totalOccupied / totalRooms) * 100 : 0;
  const pending = complaints.filter((c) => (c.complaintStatus ?? c.status) === "PENDING").length;

  return (
    <div>
      <PageHeader title="Controlling Warden" subtitle="System-wide overview of all halls" />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <StatCard label="Total Halls" value={halls.length} icon={<Building2 className="w-5 h-5" />} color="blue" />
        <StatCard label="Total Rooms" value={totalRooms} color="indigo" />
        <StatCard label="Occupied Rooms" value={totalOccupied} color="emerald" />
        <StatCard label="Pending Complaints" value={pending} icon={<MessageSquare className="w-5 h-5" />} color="amber" />
      </div>

      <Card className="p-5 mb-6">
        <p className="font-semibold text-slate-700 mb-1">Overall Occupancy</p>
        <p className="text-3xl font-bold text-slate-900 mb-3">{overallPct.toFixed(1)}%</p>
        <OccupancyBar percentage={overallPct} />
      </Card>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
        <Link href="/controlling-warden/occupancy" className="bg-white border border-slate-200 rounded-xl p-5 hover:border-purple-300 hover:shadow-sm transition-all group">
          <BarChart3 className="w-6 h-6 text-purple-600 mb-3" />
          <p className="font-semibold text-slate-900 group-hover:text-purple-700">Overall Occupancy</p>
          <p className="text-sm text-slate-500 mt-1">Detailed per-hall breakdown</p>
        </Link>
        <Link href="/controlling-warden/complaints" className="bg-white border border-slate-200 rounded-xl p-5 hover:border-amber-300 hover:shadow-sm transition-all group">
          <MessageSquare className="w-6 h-6 text-amber-600 mb-3" />
          <p className="font-semibold text-slate-900 group-hover:text-amber-700">All Complaints</p>
          <p className="text-sm text-slate-500 mt-1">View complaints across all halls</p>
        </Link>
        <Link href="/controlling-warden/halls" className="bg-white border border-slate-200 rounded-xl p-5 hover:border-blue-300 hover:shadow-sm transition-all group">
          <Building2 className="w-6 h-6 text-blue-600 mb-3" />
          <p className="font-semibold text-slate-900 group-hover:text-blue-700">All Halls</p>
          <p className="text-sm text-slate-500 mt-1">View hall details and wardens</p>
        </Link>
      </div>

      {occupancies.length > 0 && (
        <Card>
          <div className="px-4 py-3 border-b border-slate-100">
            <h3 className="font-semibold text-slate-700">Occupancy by Hall</h3>
          </div>
          <div className="p-4 space-y-4">
            {occupancies.map((o) => (
              <div key={o.hallId}>
                <div className="flex justify-between text-sm mb-1">
                  <span className="font-medium text-slate-700">{o.hallName}</span>
                  <span className="text-slate-500">{o.occupiedRooms}/{o.totalRooms} rooms</span>
                </div>
                <OccupancyBar percentage={o.occupancyPercentage} />
              </div>
            ))}
          </div>
        </Card>
      )}
    </div>
  );
}
