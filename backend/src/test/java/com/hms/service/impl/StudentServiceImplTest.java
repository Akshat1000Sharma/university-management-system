package com.hms.service.impl;

import com.hms.entity.Hall;
import com.hms.entity.Room;
import com.hms.entity.Student;
import com.hms.entity.User;
import com.hms.enums.RoomType;
import com.hms.enums.UserRole;
import com.hms.exception.ResourceNotFoundException;
import com.hms.repository.HallRepository;
import com.hms.repository.RoomRepository;
import com.hms.repository.StudentRepository;
import com.hms.repository.UserRepository;
import com.hms.service.RoomOccupancyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock private StudentRepository repo;
    @Mock private RoomRepository roomRepo;
    @Mock private UserRepository userRepository;
    @Mock private HallRepository hallRepository;
    @Mock private RoomOccupancyService roomOccupancyService;
    @InjectMocks private StudentServiceImpl service;

    private Student makeStudent(Long id, String name) {
        return new Student(id, name, "Addr", "9999", null, null, null, null, 1L, 1L);
    }

    @Test
    @DisplayName("create: saves and returns student")
    void create_success() {
        Student s = makeStudent(null, "Alice");
        Student saved = makeStudent(1L, "Alice");
        when(repo.save(s)).thenReturn(saved);

        Student result = service.create(s);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getName());
    }

    @Test
    @DisplayName("getAll: returns all students")
    void getAll_returnsList() {
        when(repo.findAll()).thenReturn(List.of(makeStudent(1L, "A"), makeStudent(2L, "B")));
        List<Student> result = service.getAll();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getById: existing ID returns student")
    void getById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeStudent(1L, "Alice")));
        Student result = service.getById(1L);
        assertEquals("Alice", result.getName());
    }

    @Test
    @DisplayName("getById: missing ID throws ResourceNotFoundException")
    void getById_notFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    @DisplayName("update: updates all fields and saves")
    void update_success() {
        Student existing = makeStudent(1L, "Alice");
        Student updated = makeStudent(1L, "Alice Updated");
        updated.setPhone("1111");
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(updated);

        Student result = service.update(1L, updated);
        assertEquals("Alice Updated", result.getName());
        verify(repo).save(existing);
    }

    @Test
    @DisplayName("delete: calls deleteById and syncs room occupancy")
    void delete_success() {
        when(repo.findById(1L)).thenReturn(Optional.of(makeStudent(1L, "Alice")));
        service.delete(1L);
        verify(repo).deleteById(1L);
        verify(roomOccupancyService).syncOccupiedFlag(1L);
    }

    @Test
    @DisplayName("getByHallId: returns students for given hall")
    void getByHallId_returnsList() {
        when(repo.findByHallId(1L)).thenReturn(List.of(makeStudent(1L, "A")));
        List<Student> result = service.getByHallId(1L);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("selectRoom: warden frees old room and occupies new")
    void selectRoom_wardenMovesStudent() {
        User warden = User.builder().role(UserRole.WARDEN).hallId(1L).build();
        Student s = new Student(1L, "A", "Addr", "123", null, null, null, null, 1L, 2L);
        Room oldRoom = new Room(2L, "101", RoomType.SINGLE, 1000, 1L, true);
        Room newRoom = new Room(3L, "102", RoomType.TWIN_SHARING, 800, 1L, false);

        when(repo.findById(1L)).thenReturn(Optional.of(s));
        when(roomRepo.findById(3L)).thenReturn(Optional.of(newRoom));
        when(roomOccupancyService.hasVacancy(any(Room.class))).thenReturn(true);
        when(repo.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student out = service.selectRoom(1L, 3L, warden);
        assertEquals(3L, out.getRoomId());
        verify(roomOccupancyService).syncOccupiedFlag(2L);
        verify(roomOccupancyService).syncOccupiedFlag(3L);
    }

    @Test
    @DisplayName("selectRoom: student may pick a room in another hall and hallId syncs")
    void selectRoom_studentCrossHall() {
        User studentUser = User.builder().role(UserRole.STUDENT).studentId(1L).email("stu@test.edu").build();
        Student s = new Student(1L, "A", "Addr", "123", null, null, null, null, 1L, 2L);
        Room oldRoom = new Room(2L, "101", RoomType.SINGLE, 1000, 1L, true);
        Room newRoom = new Room(5L, "S201", RoomType.SINGLE, 2000, 2L, false);
        Hall south = new Hall(2L, "South Hall", false, 4000.0);
        User dbUser = User.builder().email("stu@test.edu").build();

        when(repo.findById(1L)).thenReturn(Optional.of(s));
        when(roomRepo.findById(5L)).thenReturn(Optional.of(newRoom));
        when(roomOccupancyService.hasVacancy(any(Room.class))).thenReturn(true);
        when(repo.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.findByEmail("stu@test.edu")).thenReturn(Optional.of(dbUser));
        when(hallRepository.findById(2L)).thenReturn(Optional.of(south));

        Student out = service.selectRoom(1L, 5L, studentUser);
        assertEquals(5L, out.getRoomId());
        assertEquals(2L, out.getHallId());
        verify(roomOccupancyService).syncOccupiedFlag(2L);
        verify(roomOccupancyService).syncOccupiedFlag(5L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("selectRoom: student may only select own record")
    void selectRoom_studentWrongIdDenied() {
        User studentUser = User.builder().role(UserRole.STUDENT).studentId(1L).build();
        Student s = new Student(2L, "A", "Addr", "123", null, null, null, null, 1L, null);
        when(repo.findById(2L)).thenReturn(Optional.of(s));
        assertThrows(AccessDeniedException.class, () -> service.selectRoom(2L, 3L, studentUser));
    }
}
