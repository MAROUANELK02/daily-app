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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class DailyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            if(roleRepository.findByRoleName(ERole.ROLE_ADMIN) == null) {
                Role roleAdmin = Role.builder().roleName(ERole.ROLE_ADMIN).build();
                roleRepository.save(roleAdmin);
            }
            if(roleRepository.findByRoleName(ERole.ROLE_USER) == null) {
                Role roleUser = Role.builder().roleName(ERole.ROLE_USER).build();
                roleRepository.save(roleUser);
            }
            if(!userRepository.existsByUsername("Mehdi-OUADOU")) {
                User user = User.builder()
                        .firstname("Mehdi")
                        .lastname("OUADOU")
                        .email("mehdi.ouadou@hps-worldwide.com")
                        .username("Mehdi-OUADOU")
                        .password(passwordEncoder.encode("Pass@word1"))
                        .jobTitle("Head of Operations")
                        .tasksCount(0)
                        .roles(List.of(roleRepository.findByRoleName(ERole.ROLE_ADMIN),
                                roleRepository.findByRoleName(ERole.ROLE_USER)))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                userRepository.save(user);
            }
        };
    }
}