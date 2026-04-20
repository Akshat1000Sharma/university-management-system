package com.hms.service.impl;

import com.hms.entity.Hall;
import com.hms.entity.Room;
import com.hms.entity.Student;
import com.hms.entity.User;
import com.hms.enums.UserRole;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.HallRepository;
import com.hms.repository.RoomRepository;
import com.hms.repository.StudentRepository;
import com.hms.repository.UserRepository;
import com.hms.service.RoomOccupancyService;
import com.hms.service.StudentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;
    private final RoomRepository roomRepo;
    private final UserRepository userRepository;
    private final HallRepository hallRepository;
    private final RoomOccupancyService roomOccupancyService;

    public StudentServiceImpl(StudentRepository repo,
                              RoomRepository roomRepo,
                              UserRepository userRepository,
                              HallRepository hallRepository,
                              RoomOccupancyService roomOccupancyService) {
        this.repo = repo;
        this.roomRepo = roomRepo;
        this.userRepository = userRepository;
        this.hallRepository = hallRepository;
        this.roomOccupancyService = roomOccupancyService;
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
    public void delete(Long id) {
        Student s = getById(id);
        Long roomId = s.getRoomId();
        repo.deleteById(id);
        if (roomId != null) {
            roomOccupancyService.syncOccupiedFlag(roomId);
        }
    }
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

        validateRoomForRole(currentUser, student, room);

        Long oldRoomId = student.getRoomId();
        if (roomId.equals(oldRoomId)) {
            return student;
        }

        if (!roomOccupancyService.hasVacancy(room)) {
            throw new IllegalArgumentException("Room has no vacant beds");
        }

        student.setHallId(room.getHallId());
        student.setRoomId(roomId);
        Student saved = repo.save(student);
        if (oldRoomId != null && !oldRoomId.equals(roomId)) {
            roomOccupancyService.syncOccupiedFlag(oldRoomId);
        }
        roomOccupancyService.syncOccupiedFlag(roomId);
        syncUserHallWithStudentRecord(saved, currentUser);
        return saved;
    }

    /**
     * Wardens may only assign rooms in their hall to students in their hall. Students and HMC may use any hall.
     */
    private static void validateRoomForRole(User currentUser, Student student, Room room) {
        if (currentUser.getRole() == UserRole.WARDEN) {
            if (currentUser.getHallId() == null
                    || !currentUser.getHallId().equals(room.getHallId())
                    || !currentUser.getHallId().equals(student.getHallId())) {
                throw new IllegalArgumentException("Room and student must be in your hall");
            }
        }
    }

    private void syncUserHallWithStudentRecord(Student student, User currentUser) {
        Optional<User> account;
        if (currentUser.getRole() == UserRole.STUDENT) {
            account = userRepository.findByEmail(currentUser.getEmail());
        } else {
            account = userRepository.findByStudentId(student.getId());
        }
        account.ifPresent(u -> {
            u.setHallId(student.getHallId());
            Optional<Hall> hall = hallRepository.findById(student.getHallId());
            u.setHallName(hall.map(Hall::getName).orElse(null));
            userRepository.save(u);
        });
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
