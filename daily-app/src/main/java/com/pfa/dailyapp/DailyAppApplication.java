package com.pfa.dailyapp;

import com.pfa.dailyapp.entities.Role;
import com.pfa.dailyapp.entities.Task;
import com.pfa.dailyapp.entities.User;
import com.pfa.dailyapp.enums.ERole;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.repositories.RoleRepository;
import com.pfa.dailyapp.repositories.TaskRepository;
import com.pfa.dailyapp.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DailyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyAppApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, TaskRepository taskRepository,
                                        RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            List<Role> roles = List.of(Role.builder().roleName(ERole.ROLE_ADMIN).build(),
                    Role.builder().roleName(ERole.ROLE_USER).build());
            roleRepository.saveAll(roles);

            User user1 = User.builder()
                    .firstname("Mehdi")
                    .lastname("chef")
                    .email("mehdichef@example.com")
                    .username("mehdichef")
                    .password(passwordEncoder.encode("Pass@word1"))
                    .jobTitle("Chef projet")
                    .tasksCount(1)
                    .roles(List.of(roleRepository.findByRoleName(ERole.ROLE_ADMIN),
                            roleRepository.findByRoleName(ERole.ROLE_USER)))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User user2 = User.builder()
                    .firstname("sara")
                    .lastname("sales")
                    .email("sarasales@example.com")
                    .username("sarasales")
                    .password(passwordEncoder.encode("Pass@word1"))
                    .jobTitle("Sales manager")
                    .tasksCount(1)
                    .roles(List.of(roleRepository.findByRoleName(ERole.ROLE_USER)))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User user3 = User.builder()
                    .firstname("nassim")
                    .lastname("ghelab")
                    .email("nassimghelab@example.com")
                    .username("nassimghelab")
                    .password(passwordEncoder.encode("Pass@word1"))
                    .jobTitle("Software engineer")
                    .tasksCount(1)
                    .roles(List.of(roleRepository.findByRoleName(ERole.ROLE_USER)))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.saveAll(List.of(user1, user2, user3));

            Task task1 = Task.builder()
                    .title("Task 1")
                    .description("Task 1 description")
                    .status(TaskStatus.IN_PROGRESS)
                    .priority(TaskPriority.HIGH)
                    .user(userRepository.findByUsernameOrEmail("mehdichef", "mehdichef"))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Task task2 = Task.builder()
                    .title("Task 2")
                    .description("Task 2 description")
                    .status(TaskStatus.IN_PROGRESS)
                    .priority(TaskPriority.MEDIUM)
                    .user(userRepository.findByUsernameOrEmail("sarasales@example.com", "sarasales@example.com"))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Task task3 = Task.builder()
                    .title("Task 3")
                    .description("Task 3 description")
                    .status(TaskStatus.IN_PROGRESS)
                    .priority(TaskPriority.LOW)
                    .user(userRepository.findByUsernameOrEmail("nassimghelab", "nassimghelab"))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            taskRepository.saveAll(List.of(task1, task2, task3));
        };
    }
}
