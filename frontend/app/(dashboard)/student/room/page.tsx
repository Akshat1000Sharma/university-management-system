"use client";

import { useEffect, useMemo, useState } from "react";
import { Home } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api, authApi } from "../../../lib/api";
import { formatCurrency, formatRoomType } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState, Btn,
} from "../../../components/ui";
import type { AuthUser, Hall, Room, Role, Student } from "../../../lib/types";

type TypeFilter = "ALL" | "SINGLE" | "TWIN_SHARING";

export default function StudentRoomPage() {
  const { user, login } = useAuth();
  const [student, setStudent] = useState<Student | null>(null);
  const [halls, setHalls] = useState<Hall[]>([]);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [submittingId, setSubmittingId] = useState<number | null>(null);
  const [typeFilter, setTypeFilter] = useState<TypeFilter>("ALL");
  const [hallFilter, setHallFilter] = useState<number | "ALL">("ALL");

  const load = async () => {
    if (!user?.studentId) {
      setLoading(false);
      return;
    }
    setError("");
    try {
      const [s, h, r] = await Promise.all([
        api.students.getById(user.studentId),
        api.halls.getAll(),
        api.rooms.getAll(),
      ]);
      setStudent(s);
      setHalls(h);
      setRooms(r);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [user?.studentId]);

  const hallName = useMemo(() => {
    const map = new Map(halls.map((x) => [x.id, x.name]));
    return (id: number) => map.get(id) ?? `Hall #${id}`;
  }, [halls]);

  const currentRoom = useMemo(() => {
    if (!student?.roomId) return null;
    return rooms.find((r) => r.id === student.roomId) ?? null;
  }, [student?.roomId, rooms]);

  const availableRooms = useMemo(() => {
    return rooms.filter((room) => {
      if (room.occupied) return false;
      if (typeFilter !== "ALL" && room.roomType !== typeFilter) return false;
      if (hallFilter !== "ALL" && room.hallId !== hallFilter) return false;
      return true;
    });
  }, [rooms, typeFilter, hallFilter]);

  const refreshSession = async () => {
    const me = await authApi.me();
    const next: AuthUser = {
      role: me.role as Role,
      name: me.name,
      email: me.email,
      hallId: me.hallId,
      hallName: me.hallName,
      studentId: me.studentId,
      registrationNumber: me.registrationNumber,
    };
    login(next);
  };

  const handleSelect = async (roomId: number) => {
    if (!user?.studentId) return;
    setSubmittingId(roomId);
    setError("");
    try {
      const updated = await api.students.selectRoom(user.studentId, roomId);
      setStudent(updated);
      await refreshSession();
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Could not select room");
    } finally {
      setSubmittingId(null);
    }
  };

  if (loading) return <Spinner />;

  if (!user?.studentId) {
    return (
      <div>
        <PageHeader title="Room selection" subtitle="Choose your room" />
        <ErrorMsg message="Your account is not linked to a student record. Contact the clerk." />
      </div>
    );
  }

  return (
    <div>
      <PageHeader
        title="Room selection"
        subtitle="Browse vacant rooms in every hall. Picking a room in another hostel updates your hall assignment. Single vs twin sharing is a category and rent label; each room still has one student in this system."
      />

      {error && <ErrorMsg message={error} />}

      {currentRoom && (
        <Card className="p-5 mb-6 border-emerald-200 bg-emerald-50/40">
          <div className="flex items-start gap-3">
            <div className="rounded-lg bg-emerald-100 p-2">
              <Home className="w-5 h-5 text-emerald-700" />
            </div>
            <div>
              <p className="text-sm font-semibold text-emerald-900">Your current room</p>
              <p className="text-lg font-bold text-slate-900 mt-1">
                {hallName(currentRoom.hallId)} · Room {currentRoom.roomNumber}
                <span className="ml-2 text-sm font-normal text-slate-600">
                  · {formatRoomType(currentRoom.roomType)} · {formatCurrency(currentRoom.rent)}/mo
                </span>
              </p>
              <p className="text-xs text-slate-500 mt-1">
                Choose another vacant room below to move; your previous room will be released.
              </p>
            </div>
          </div>
        </Card>
      )}

      <div className="flex flex-col sm:flex-row sm:flex-wrap sm:items-end gap-4 mb-4">
        <div>
          <span className="text-xs font-medium text-slate-500 block mb-1">Hall</span>
          <select
            value={hallFilter === "ALL" ? "" : String(hallFilter)}
            onChange={(e) => setHallFilter(e.target.value === "" ? "ALL" : Number(e.target.value))}
            className="border border-slate-200 rounded-lg px-3 py-2 text-sm bg-white text-slate-800 min-w-[200px]"
          >
            <option value="">All halls</option>
            {halls.map((h) => (
              <option key={h.id} value={h.id}>
                {h.name}
              </option>
            ))}
          </select>
        </div>
        <div className="flex flex-wrap items-center gap-2">
          <span className="text-sm text-slate-500">Type:</span>
          {(["ALL", "SINGLE", "TWIN_SHARING"] as const).map((f) => (
            <button
              key={f}
              type="button"
              onClick={() => setTypeFilter(f)}
              className={`text-xs px-3 py-1.5 rounded-full font-medium transition-colors ${
                typeFilter === f
                  ? "bg-indigo-600 text-white"
                  : "bg-slate-100 text-slate-600 hover:bg-slate-200"
              }`}
            >
              {f === "ALL" ? "All types" : formatRoomType(f)}
            </button>
          ))}
        </div>
      </div>

      <Card>
        {availableRooms.length === 0 ? (
          <EmptyState
            title="No vacant rooms match"
            description={
              hallFilter !== "ALL" || typeFilter !== "ALL"
                ? "Try setting Hall to “All halls” or Type to “All types”."
                : "There are no free rooms in the system right now."
            }
          />
        ) : (
          <Table headers={["Hall", "Room", "Type", "Rent / month", "Action"]}>
              {availableRooms.map((room) => (
                <Tr key={room.id}>
                  <Td className="font-medium text-slate-800 whitespace-nowrap">{hallName(room.hallId)}</Td>
                  <Td className="font-semibold whitespace-nowrap">{room.roomNumber}</Td>
                  <Td>
                    <span
                      className={`text-xs px-2 py-0.5 rounded-full whitespace-nowrap ${
                        room.roomType === "SINGLE"
                          ? "bg-blue-50 text-blue-700"
                          : "bg-purple-50 text-purple-700"
                      }`}
                    >
                      {formatRoomType(room.roomType)}
                    </span>
                  </Td>
                  <Td className="whitespace-nowrap">{formatCurrency(room.rent)}</Td>
                  <Td className="text-right whitespace-nowrap min-w-[140px]">
                    <Btn
                      size="sm"
                      disabled={submittingId === room.id}
                      onClick={() => handleSelect(room.id)}
                    >
                      {submittingId === room.id ? "Saving…" : "Select"}
                    </Btn>
                  </Td>
                </Tr>
              ))}
          </Table>
        )}
      </Card>
    </div>
  );
}
