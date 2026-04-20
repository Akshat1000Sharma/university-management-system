package com.hms.config;

import com.hms.entity.Permission;
import com.hms.entity.User;
import com.hms.enums.UserRole;
import com.hms.repository.PermissionRepository;
import com.hms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PermissionRepository permissionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Seeding default users...");
            seedUsers();
        }
        if (permissionRepository.count() == 0) {
            log.info("Seeding role permissions...");
            seedPermissions();
        }
    }

    private void seedUsers() {
        List<User> users = List.of(
            User.builder()
                .email("student@hms.edu")
                .password(passwordEncoder.encode("student123"))
                .name("Raj Kumar")
                .role(UserRole.STUDENT)
                .hallId(1L).hallName("North Hall")
                .studentId(1L).registrationNumber("REG001")
                .build(),

            User.builder()
                .email("warden@hms.edu")
                .password(passwordEncoder.encode("warden123"))
                .name("Dr. Kumar")
                .role(UserRole.WARDEN)
                .hallId(1L).hallName("North Hall")
                .build(),

            User.builder()
                .email("cwarden@hms.edu")
                .password(passwordEncoder.encode("cwarden123"))
                .name("Chief Warden")
                .role(UserRole.CONTROLLING_WARDEN)
                .build(),

            User.builder()
                .email("mess@hms.edu")
                .password(passwordEncoder.encode("mess123"))
                .name("Rajesh Kumar")
                .role(UserRole.MESS_MANAGER)
                .hallId(1L).hallName("North Hall")
                .build(),

            User.builder()
                .email("clerk@hms.edu")
                .password(passwordEncoder.encode("clerk123"))
                .name("Hall Clerk")
                .role(UserRole.CLERK)
                .hallId(1L).hallName("North Hall")
                .build(),

            User.builder()
                .email("hmc@hms.edu")
                .password(passwordEncoder.encode("hmc123"))
                .name("HMC Chairman")
                .role(UserRole.HMC_CHAIRMAN)
                .build(),

            // Additional users for other halls
            User.builder()
                .email("student2@hms.edu")
                .password(passwordEncoder.encode("student123"))
                .name("Priya Singh")
                .role(UserRole.STUDENT)
                .hallId(2L).hallName("South Hall")
                .studentId(6L).registrationNumber("REG006")
                .build(),

            User.builder()
                .email("warden2@hms.edu")
                .password(passwordEncoder.encode("warden123"))
                .name("Prof. Sharma")
                .role(UserRole.WARDEN)
                .hallId(2L).hallName("South Hall")
                .build(),

            User.builder()
                .email("student3@hms.edu")
                .password(passwordEncoder.encode("student123"))
                .name("Anjali Rao")
                .role(UserRole.STUDENT)
                .hallId(3L).hallName("East Hall")
                .studentId(11L).registrationNumber("REG011")
                .build(),

            User.builder()
                .email("warden3@hms.edu")
                .password(passwordEncoder.encode("warden123"))
                .name("Mr. Patel")
                .role(UserRole.WARDEN)
                .hallId(3L).hallName("East Hall")
                .build(),

            User.builder()
                .email("student4@hms.edu")
                .password(passwordEncoder.encode("student123"))
                .name("Neha Sharma")
                .role(UserRole.STUDENT)
                .hallId(4L).hallName("West Hall")
                .studentId(16L).registrationNumber("REG016")
                .build(),

            User.builder()
                .email("warden4@hms.edu")
                .password(passwordEncoder.encode("warden123"))
                .name("Mrs. Singh")
                .role(UserRole.WARDEN)
                .hallId(4L).hallName("West Hall")
                .build(),

            User.builder()
                .email("student5@hms.edu")
                .password(passwordEncoder.encode("student123"))
                .name("Aarav Mehta")
                .role(UserRole.STUDENT)
                .hallId(1L).hallName("North Hall")
                .studentId(21L).registrationNumber("REG021")
                .build(),

            User.builder()
                .email("student6@hms.edu")
                .password(passwordEncoder.encode("student123"))
                .name("Isha Kapoor")
                .role(UserRole.STUDENT)
                .hallId(1L).hallName("North Hall")
                .studentId(22L).registrationNumber("REG022")
                .build(),

            User.builder()
                .email("student7@hms.edu")
                .password(passwordEncoder.encode("student123"))
                .name("Vihaan Chawla")
                .role(UserRole.STUDENT)
                .hallId(1L).hallName("North Hall")
                .studentId(23L).registrationNumber("REG023")
                .build()
        );

        userRepository.saveAll(users);
        log.info("Seeded {} users", users.size());
    }

    private void seedPermissions() {
        List<Permission> permissions = List.of(
            // Student permissions
            perm(UserRole.STUDENT, "VIEW_OWN_DUES", "View own dues and charges"),
            perm(UserRole.STUDENT, "VIEW_OWN_COMPLAINTS", "View own complaints"),
            perm(UserRole.STUDENT, "CREATE_COMPLAINT", "File a new complaint"),
            perm(UserRole.STUDENT, "VIEW_OWN_PAYMENTS", "View own payment history"),

            // Warden permissions
            perm(UserRole.WARDEN, "VIEW_HALL_STUDENTS", "View students in assigned hall"),
            perm(UserRole.WARDEN, "VIEW_HALL_COMPLAINTS", "View complaints for assigned hall"),
            perm(UserRole.WARDEN, "POST_ATR", "Post action taken report on complaints"),
            perm(UserRole.WARDEN, "VIEW_HALL_OCCUPANCY", "View room occupancy for assigned hall"),
            perm(UserRole.WARDEN, "MANAGE_HALL_STAFF", "Manage staff in assigned hall"),
            perm(UserRole.WARDEN, "VIEW_SALARY_SHEET", "View salary sheet for assigned hall"),
            perm(UserRole.WARDEN, "MANAGE_EXPENDITURES", "Manage expenditures for assigned hall"),
            perm(UserRole.WARDEN, "VIEW_ANNUAL_STATEMENT", "View annual financial statement"),
            perm(UserRole.WARDEN, "ADMIT_STUDENT", "Admit a new student to assigned hall"),

            // Controlling Warden permissions
            perm(UserRole.CONTROLLING_WARDEN, "VIEW_ALL_HALLS", "View all halls and their details"),
            perm(UserRole.CONTROLLING_WARDEN, "VIEW_ALL_COMPLAINTS", "View complaints across all halls"),
            perm(UserRole.CONTROLLING_WARDEN, "VIEW_OVERALL_OCCUPANCY", "View occupancy across all halls"),

            // Mess Manager permissions
            perm(UserRole.MESS_MANAGER, "MANAGE_MESS_CHARGES", "Create and manage monthly mess charges"),
            perm(UserRole.MESS_MANAGER, "VIEW_PAYMENT_SHEET", "View mess payment sheet"),

            // Clerk permissions
            perm(UserRole.CLERK, "MANAGE_STAFF_LEAVES", "Record and manage staff leaves"),
            perm(UserRole.CLERK, "VIEW_SALARY_SHEET", "View and generate salary sheets"),

            // HMC Chairman permissions
            perm(UserRole.HMC_CHAIRMAN, "MANAGE_GRANTS", "Create and manage annual grants"),
            perm(UserRole.HMC_CHAIRMAN, "MANAGE_HALLS", "Manage halls and rooms"),
            perm(UserRole.HMC_CHAIRMAN, "VIEW_ALL_EXPENDITURES", "View expenditures across all halls"),
            perm(UserRole.HMC_CHAIRMAN, "VIEW_ALL_STUDENTS", "View all students across halls"),
            perm(UserRole.HMC_CHAIRMAN, "ALLOCATE_GRANTS", "Allocate grants to halls")
        );

        permissionRepository.saveAll(permissions);
        log.info("Seeded {} permissions", permissions.size());
    }

    private Permission perm(UserRole role, String permission, String description) {
        return Permission.builder()
                .role(role)
                .permission(permission)
                .description(description)
                .build();
    }
}
