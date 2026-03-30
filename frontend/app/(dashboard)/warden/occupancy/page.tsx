"use client";

import { useEffect, useState } from "react";
import { Home } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
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

  const occupiedRoomIds = new Set(students.map((s) => s.roomId).filter(Boolean));

  return (
    <div>
      <PageHeader title="Room Occupancy" subtitle={user?.hallName} />
      {error && <ErrorMsg message={error} />}

      {occupancy && (
        <>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <StatCard label="Total Rooms" value={occupancy.totalRooms} icon={<Home className="w-5 h-5" />} color="blue" />
            <StatCard label="Occupied" value={occupancy.occupiedRooms} color="emerald" />
            <StatCard label="Empty" value={occupancy.emptyRooms} color="amber" />
            <StatCard label="Occupancy Rate" value={`${occupancy.occupancyPercentage.toFixed(1)}%`} color="indigo" />
          </div>

          <Card className="p-5 mb-6">
            <p className="text-sm font-semibold text-slate-600 mb-2">Occupancy Rate</p>
            <OccupancyBar percentage={occupancy.occupancyPercentage} />
          </Card>
        </>
      )}

      <Card>
        <div className="px-4 py-3 border-b border-slate-100">
          <h3 className="font-semibold text-slate-700">Room List</h3>
        </div>
        {rooms.length === 0 ? (
          <div className="text-center py-10 text-slate-400 text-sm">No rooms found</div>
        ) : (
          <Table headers={["Room No.", "Type", "Rent", "Status", "Student"]}>
            {rooms.map((room) => {
              const student = students.find((s) => s.roomId === room.id);
              const occupied = occupiedRoomIds.has(room.id) || room.isOccupied;
              return (
                <Tr key={room.id}>
                  <Td className="font-semibold">{room.roomNumber}</Td>
                  <Td>
                    <span className={`text-xs px-2 py-0.5 rounded-full ${room.roomType === "SINGLE" ? "bg-blue-50 text-blue-700" : "bg-purple-50 text-purple-700"}`}>
                      {room.roomType.replace("_", " ")}
                    </span>
                  </Td>
                  <Td className="text-slate-600">₹{room.rentAmount?.toLocaleString("en-IN")}</Td>
                  <Td>
                    <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${occupied ? "bg-emerald-50 text-emerald-700" : "bg-slate-100 text-slate-500"}`}>
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
