"use client";

import { useEffect, useState } from "react";
import { Plus, Trash2, ChevronDown, ChevronRight } from "lucide-react";
import { api } from "../../../lib/api";
import { formatCurrency } from "../../../lib/utils";
import {
  PageHeader, Card, Table, Tr, Td, Spinner, ErrorMsg, EmptyState,
  Modal, FormField, Input, Select, Btn, StatCard
} from "../../../components/ui";
import type { Hall, Room } from "../../../lib/types";

export default function HmcHallsPage() {
  const [halls, setHalls] = useState<Hall[]>([]);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [expandedHall, setExpandedHall] = useState<number | null>(null);

  const [showHallModal, setShowHallModal] = useState(false);
  const [showRoomModal, setShowRoomModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const [hallForm, setHallForm] = useState({ name: "", isNew: "true", amenityCharge: "" });
  const [roomForm, setRoomForm] = useState({ hallId: "", roomNumber: "", roomType: "SINGLE", rent: "" });

  const load = async () => {
    try {
      const [h, r] = await Promise.all([api.halls.getAll(), api.rooms.getAll()]);
      setHalls(h);
      setRooms(r);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Failed to load");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleCreateHall = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await api.halls.create({ name: hallForm.name, isNew: hallForm.isNew === "true", amenityCharge: Number(hallForm.amenityCharge) });
      setShowHallModal(false);
      setHallForm({ name: "", isNew: "true", amenityCharge: "" });
      await load();
    } catch (e: unknown) { setError(e instanceof Error ? e.message : "Failed"); }
    finally { setSubmitting(false); }
  };

  const handleCreateRoom = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await api.rooms.create({ roomNumber: roomForm.roomNumber, roomType: roomForm.roomType as Room["roomType"], hallId: Number(roomForm.hallId), rent: Number(roomForm.rent) });
      setShowRoomModal(false);
      setRoomForm({ hallId: "", roomNumber: "", roomType: "SINGLE", rent: "" });
      await load();
    } catch (e: unknown) { setError(e instanceof Error ? e.message : "Failed"); }
    finally { setSubmitting(false); }
  };

  const handleDeleteHall = async (id: number) => {
    if (!confirm("Delete this hall? All associated data will be removed.")) return;
    try { await api.halls.delete(id); await load(); }
    catch (e: unknown) { setError(e instanceof Error ? e.message : "Failed"); }
  };

  const handleDeleteRoom = async (id: number) => {
    if (!confirm("Delete this room?")) return;
    try { await api.rooms.delete(id); await load(); }
    catch (e: unknown) { setError(e instanceof Error ? e.message : "Failed"); }
  };

  if (loading) return <Spinner />;

  return (
    <div>
      <PageHeader
        title="Halls & Rooms"
        subtitle="Manage halls and their rooms"
        action={
          <div className="flex gap-2">
            <Btn variant="secondary" onClick={() => setShowRoomModal(true)}><Plus className="w-4 h-4" /> Add Room</Btn>
            <Btn onClick={() => setShowHallModal(true)}><Plus className="w-4 h-4" /> Add Hall</Btn>
          </div>
        }
      />
      {error && <ErrorMsg message={error} />}

      <div className="grid grid-cols-3 gap-4 mb-6">
        <StatCard label="Total Halls" value={halls.length} color="blue" />
        <StatCard label="Total Rooms" value={rooms.length} color="indigo" />
        <StatCard label="Occupied Rooms" value={rooms.filter((r) => r.occupied).length} color="emerald" />
      </div>

      {halls.length === 0 ? (
        <EmptyState title="No halls yet" action={<Btn onClick={() => setShowHallModal(true)}><Plus className="w-4 h-4" /> Add Hall</Btn>} />
      ) : (
        <div className="space-y-3">
          {halls.map((hall) => {
              const isNew = (hall as unknown as { isNew?: boolean; new?: boolean }).isNew ??
                (hall as unknown as { new?: boolean }).new;
            const hallRooms = rooms.filter((r) => r.hallId === hall.id);
            const isExpanded = expandedHall === hall.id;
            return (
              <Card key={hall.id}>
                <div
                  className="flex items-center justify-between px-4 py-3 cursor-pointer hover:bg-slate-50 transition-colors"
                  onClick={() => setExpandedHall(isExpanded ? null : hall.id)}
                >
                  <div className="flex items-center gap-3">
                    {isExpanded ? <ChevronDown className="w-4 h-4 text-slate-400" /> : <ChevronRight className="w-4 h-4 text-slate-400" />}
                    <div>
                    <p className="font-semibold text-slate-900">{hall.name}</p>
                    <p className="text-xs text-slate-400">
                      {isNew ? "New Hall" : "Old Hall"} · Amenity: {formatCurrency(hall.amenityCharge)} · {hallRooms.length} rooms
                    </p>
                    </div>
                  </div>
                  <Btn size="sm" variant="danger" onClick={() => handleDeleteHall(hall.id)}>
                    <Trash2 className="w-3 h-3" />
                  </Btn>
                </div>

                {isExpanded && (
                  <div className="border-t border-slate-100">
                    {hallRooms.length === 0 ? (
                      <div className="text-center py-6 text-slate-400 text-sm">No rooms in this hall</div>
                    ) : (
                      <Table headers={["Room No.", "Type", "Rent", "Status", ""]}>
                        {hallRooms.map((r) => (
                          <Tr key={r.id}>
                            <Td className="font-medium">{r.roomNumber}</Td>
                            <Td>
                              <span className={`text-xs px-2 py-0.5 rounded-full ${r.roomType === "SINGLE" ? "bg-blue-50 text-blue-700" : "bg-purple-50 text-purple-700"}`}>
                                {r.roomType.replace("_", " ")}
                              </span>
                            </Td>
                            <Td>{formatCurrency(r.rent)}</Td>
                            <Td>
                              <span className={`text-xs px-2 py-0.5 rounded-full ${r.occupied ? "bg-emerald-50 text-emerald-700" : "bg-slate-100 text-slate-500"}`}>
                                {r.occupied ? "Occupied" : "Vacant"}
                              </span>
                            </Td>
                            <Td>
                              <Btn size="sm" variant="danger" onClick={() => handleDeleteRoom(r.id)}>
                                <Trash2 className="w-3 h-3" />
                              </Btn>
                            </Td>
                          </Tr>
                        ))}
                      </Table>
                    )}
                  </div>
                )}
              </Card>
            );
          })}
        </div>
      )}

      {showHallModal && (
        <Modal title="Add New Hall" onClose={() => setShowHallModal(false)}>
          <form onSubmit={handleCreateHall} className="space-y-4">
            <FormField label="Hall Name" required>
              <Input value={hallForm.name} onChange={(e) => setHallForm({ ...hallForm, name: e.target.value })} placeholder="e.g. North Hall" required />
            </FormField>
            <FormField label="Construction Type" required>
              <Select value={hallForm.isNew} onChange={(e) => setHallForm({ ...hallForm, isNew: e.target.value })}>
                <option value="true">New (Higher Rent)</option>
                <option value="false">Old (Lower Rent)</option>
              </Select>
            </FormField>
            <FormField label="Amenity Charge (₹ per student/month)" required>
              <Input type="number" value={hallForm.amenityCharge} onChange={(e) => setHallForm({ ...hallForm, amenityCharge: e.target.value })} min="0" required placeholder="e.g. 5000" />
            </FormField>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowHallModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>{submitting ? "Creating…" : "Create Hall"}</Btn>
            </div>
          </form>
        </Modal>
      )}

      {showRoomModal && (
        <Modal title="Add New Room" onClose={() => setShowRoomModal(false)}>
          <form onSubmit={handleCreateRoom} className="space-y-4">
            <FormField label="Hall" required>
              <Select value={roomForm.hallId} onChange={(e) => setRoomForm({ ...roomForm, hallId: e.target.value })} required>
                <option value="">Select hall…</option>
                {halls.map((h) => <option key={h.id} value={h.id}>{h.name}</option>)}
              </Select>
            </FormField>
            <FormField label="Room Number" required>
              <Input value={roomForm.roomNumber} onChange={(e) => setRoomForm({ ...roomForm, roomNumber: e.target.value })} placeholder="e.g. 101" required />
            </FormField>
            <FormField label="Room Type" required>
              <Select value={roomForm.roomType} onChange={(e) => setRoomForm({ ...roomForm, roomType: e.target.value })}>
                <option value="SINGLE">Single Room</option>
                <option value="TWIN_SHARING">Twin Sharing</option>
              </Select>
            </FormField>
            <FormField label="Rent Amount (₹/month)" required>
              <Input type="number" value={roomForm.rent} onChange={(e) => setRoomForm({ ...roomForm, rent: e.target.value })} min="1" required placeholder="e.g. 15000" />
            </FormField>
            <div className="flex justify-end gap-2 pt-2">
              <Btn variant="secondary" onClick={() => setShowRoomModal(false)}>Cancel</Btn>
              <Btn type="submit" disabled={submitting}>{submitting ? "Adding…" : "Add Room"}</Btn>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
