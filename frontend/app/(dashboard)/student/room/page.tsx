"use client";

import { useEffect, useMemo, useState } from "react";
import { Home } from "lucide-react";
import { useAuth } from "../../../lib/auth";
import { api } from "../../../lib/api";
import { formatCurrency, formatRoomType } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState, Btn,
} from "../../../components/ui";
import type { Room, Student } from "../../../lib/types";

type TypeFilter = "ALL" | "SINGLE" | "TWIN_SHARING";

export default function StudentRoomPage() {
  const { user } = useAuth();
  const [student, setStudent] = useState<Student | null>(null);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [submittingId, setSubmittingId] = useState<number | null>(null);
  const [typeFilter, setTypeFilter] = useState<TypeFilter>("ALL");

  const load = async () => {
    if (!user?.studentId || !user.hallId) {
      setLoading(false);
      return;
    }
    setError("");
    try {
      const [s, r] = await Promise.all([
        api.students.getById(user.studentId),
        api.rooms.getByHall(user.hallId),
      ]);
      setStudent(s);
      setRooms(r);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [user?.studentId, user?.hallId]);

  const currentRoom = useMemo(() => {
    if (!student?.roomId) return null;
    return rooms.find((r) => r.id === student.roomId) ?? null;
  }, [student?.roomId, rooms]);

  const availableRooms = useMemo(() => {
    return rooms.filter((room) => {
      if (room.occupied) return false;
      if (typeFilter !== "ALL" && room.roomType !== typeFilter) return false;
      return true;
    });
  }, [rooms, typeFilter]);

  const handleSelect = async (roomId: number) => {
    if (!user?.studentId) return;
    setSubmittingId(roomId);
    setError("");
    try {
      const updated = await api.students.selectRoom(user.studentId, roomId);
      setStudent(updated);
      await load();
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Could not select room");
    } finally {
      setSubmittingId(null);
    }
  };

  if (loading) return <Spinner />;

  if (!user?.studentId || !user.hallId) {
    return (
      <div>
        <PageHeader title="Room selection" subtitle="Choose your room" />
        <ErrorMsg message="Your account is not linked to a hall or student record. Contact the clerk." />
      </div>
    );
  }

  return (
    <div>
      <PageHeader
        title="Room selection"
        subtitle={`${user.hallName ?? "Your hall"} — pick a vacant single or twin-sharing room. Each room is one allotment per student; twin sharing is a category and rent label, not two students in one room in this system.`}
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
                Room {currentRoom.roomNumber}
                <span className="ml-2 text-sm font-normal text-slate-600">
                  · {formatRoomType(currentRoom.roomType)} · {formatCurrency(currentRoom.rent)}/mo
                </span>
              </p>
              <p className="text-xs text-slate-500 mt-1">
                You can change to another vacant room below; your previous room will be released.
              </p>
            </div>
          </div>
        </Card>
      )}

      <div className="flex flex-wrap items-center gap-2 mb-4">
        <span className="text-sm text-slate-500">Show:</span>
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

      <Card>
        {availableRooms.length === 0 ? (
          <EmptyState
            title="No vacant rooms"
            description={
              typeFilter === "ALL"
                ? "There are no free rooms in your hall right now. Check again later or ask your warden."
                : `No vacant ${formatRoomType(typeFilter).toLowerCase()} rooms. Try another filter.`
            }
          />
        ) : (
          <Table headers={["Room No.", "Type", "Rent / month", ""]}>
            {availableRooms.map((room) => (
              <Tr key={room.id}>
                <Td className="font-semibold">{room.roomNumber}</Td>
                <Td>
                  <span
                    className={`text-xs px-2 py-0.5 rounded-full ${
                      room.roomType === "SINGLE"
                        ? "bg-blue-50 text-blue-700"
                        : "bg-purple-50 text-purple-700"
                    }`}
                  >
                    {formatRoomType(room.roomType)}
                  </span>
                </Td>
                <Td>{formatCurrency(room.rent)}</Td>
                <Td className="text-right">
                  <Btn
                    size="sm"
                    disabled={submittingId === room.id}
                    onClick={() => handleSelect(room.id)}
                  >
                    {submittingId === room.id ? "Saving…" : "Select this room"}
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
