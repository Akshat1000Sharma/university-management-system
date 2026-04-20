package com.hms.service.impl;

import com.hms.entity.Room;
import com.hms.entity.Student;
import com.hms.entity.User;
import com.hms.enums.UserRole;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.RoomRepository;
import com.hms.repository.StudentRepository;
import com.hms.service.StudentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;
    private final RoomRepository roomRepo;

    public StudentServiceImpl(StudentRepository repo, RoomRepository roomRepo) {
        this.repo = repo;
        this.roomRepo = roomRepo;
    }

    public Student create(Student student) { return repo.save(student); }
    public List<Student> getAll() { return repo.findAll(); }
    public Student getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }
    public Student update(Long id, Student student) {
        Student existing = getById(id);
        existing.setName(student.getName());
        existing.setAddress(student.getAddress());
        existing.setPhone(student.getPhone());
        existing.setPhoto(student.getPhoto());
        existing.setEmail(student.getEmail());
        existing.setRegistrationNumber(student.getRegistrationNumber());
        existing.setAdmissionDate(student.getAdmissionDate());
        existing.setHallId(student.getHallId());
        existing.setRoomId(student.getRoomId());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
    public List<Student> getByHallId(Long hallId) { return repo.findByHallId(hallId); }

    @Override
    @Transactional
    public Student selectRoom(Long studentId, Long roomId, User currentUser) {
        if (roomId == null) {
            throw new IllegalArgumentException("roomId is required");
        }
        Student student = getById(studentId);
        authorizeRoomSelection(studentId, student, currentUser);

        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (!room.getHallId().equals(student.getHallId())) {
            throw new IllegalArgumentException("Room is not in this student's hall");
        }

        Long oldRoomId = student.getRoomId();
        if (roomId.equals(oldRoomId)) {
            return student;
        }

        if (room.isOccupied()) {
            throw new IllegalArgumentException("Room is already occupied");
        }

        if (oldRoomId != null) {
            roomRepo.findById(oldRoomId).ifPresent(r -> {
                r.setOccupied(false);
                roomRepo.save(r);
            });
        }

        student.setRoomId(roomId);
        room.setOccupied(true);
        roomRepo.save(room);
        return repo.save(student);
    }

    private static void authorizeRoomSelection(Long studentId, Student student, User currentUser) {
        UserRole role = currentUser.getRole();
        if (role == UserRole.STUDENT) {
            if (currentUser.getStudentId() == null || !currentUser.getStudentId().equals(studentId)) {
                throw new AccessDeniedException("You can only select a room for your own account");
            }
            return;
        }
        if (role == UserRole.WARDEN) {
            if (currentUser.getHallId() == null || !currentUser.getHallId().equals(student.getHallId())) {
                throw new AccessDeniedException("You can only assign rooms for students in your hall");
            }
            return;
        }
        if (role == UserRole.HMC_CHAIRMAN) {
            return;
        }
        throw new AccessDeniedException("Your role cannot assign rooms");
    }
}
