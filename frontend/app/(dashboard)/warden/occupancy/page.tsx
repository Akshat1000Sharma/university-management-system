"use client";

import { useEffect, useState } from "react";
import { Home, BedDouble, DoorOpen, TrendingUp } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency } from "../../../lib/utils";
import {
  PageHeader, Card, StatCard, Spinner, ErrorMsg, OccupancyBar,
  Table, Tr, Td
} from "../../../components/ui";
import type { HallOccupancy, Room, Student } from "../../../lib/types";

export default function WardenOccupancyPage() {
  const { user } = useAuth();
  const [occupancy, setOccupancy] = useState<HallOccupancy | null>(null);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user?.hallId) { setLoading(false); return; }
    (async () => {
      try {
        const [occ, r, s] = await Promise.all([
          api.business.getHallOccupancy(user.hallId!),
          api.rooms.getByHall(user.hallId!),
          api.students.getByHall(user.hallId!),
        ]);
        setOccupancy(occ);
        setRooms(r);
        setStudents(s);
      } catch (e: unknown) {
        setError(e instanceof Error ? e.message : "Failed to load occupancy");
      } finally {
        setLoading(false);
      }
    })();
  }, [user]);

  if (loading) return <Spinner />;

  const pct = occupancy?.occupancyPercent ?? 0;

  return (
    <div>
      <PageHeader title="Room Occupancy" subtitle={user?.hallName} />
      {error && <ErrorMsg message={error} />}

      {occupancy && (
        <>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <StatCard
              label="Total Rooms"
              value={occupancy.totalRooms}
              icon={<Home className="w-5 h-5" />}
              color="blue"
            />
            <StatCard
              label="Occupied"
              value={occupancy.occupiedRooms}
              icon={<BedDouble className="w-5 h-5" />}
              color="emerald"
            />
            <StatCard
              label="Vacant"
              value={occupancy.vacantRooms}
              icon={<DoorOpen className="w-5 h-5" />}
              color="amber"
            />
            <StatCard
              label="Occupancy Rate"
              value={`${pct.toFixed(1)}%`}
              icon={<TrendingUp className="w-5 h-5" />}
              color="indigo"
            />
          </div>

          <Card className="p-5 mb-6">
            <div className="flex items-center justify-between mb-3">
              <p className="text-sm font-semibold text-slate-600">Occupancy Rate</p>
              <span className="text-lg font-bold text-indigo-600">{pct.toFixed(1)}%</span>
            </div>
            <OccupancyBar percentage={pct} />
            <div className="flex justify-between text-xs text-slate-400 mt-2">
              <span>{occupancy.occupiedRooms} occupied</span>
              <span>{occupancy.vacantRooms} vacant</span>
            </div>
          </Card>
        </>
      )}

      <Card>
        <div className="px-4 py-3 border-b border-slate-100 flex items-center justify-between">
          <h3 className="font-semibold text-slate-700">Room List</h3>
          <span className="text-xs text-slate-400">{rooms.length} rooms total</span>
        </div>
        {rooms.length === 0 ? (
          <div className="text-center py-10 text-slate-400 text-sm">No rooms found</div>
        ) : (
          <Table headers={["Room No.", "Type", "Rent / month", "Status", "Student"]}>
            {rooms.map((room) => {
              const student = students.find((s) => s.roomId === room.id);
              const occupied = room.occupied ?? false;
              return (
                <Tr key={room.id}>
                  <Td className="font-semibold">{room.roomNumber}</Td>
                  <Td>
                    <span className={`text-xs px-2 py-0.5 rounded-full ${room.roomType === "SINGLE" ? "bg-blue-50 text-blue-700" : "bg-purple-50 text-purple-700"}`}>
                      {room.roomType.replace("_", " ")}
                    </span>
                  </Td>
                  <Td className="text-slate-700 font-medium">{formatCurrency(room.rent)}</Td>
                  <Td>
                    <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${occupied ? "bg-emerald-50 text-emerald-700" : "bg-amber-50 text-amber-700"}`}>
                      {occupied ? "Occupied" : "Vacant"}
                    </span>
                  </Td>
                  <Td className="text-slate-500 text-sm">{student ? student.name : "—"}</Td>
                </Tr>
              );
            })}
          </Table>
        )}
      </Card>
    </div>
  );
}
