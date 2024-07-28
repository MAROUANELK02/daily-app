package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.UserDTORequest;
import com.pfa.dailyapp.dtos.UserDTOResponse;
import com.pfa.dailyapp.entities.User;
import com.pfa.dailyapp.enums.ERole;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import com.pfa.dailyapp.mappers.RoleMapper;
import com.pfa.dailyapp.mappers.UserMapper;
import com.pfa.dailyapp.repositories.RoleRepository;
import com.pfa.dailyapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.path}")
    private String uploadPath;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserDTOResponse> getUsers(int page, int size) {
        log.info("Getting all users");
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<User> users = userRepository.findAllByDeletedFalse(pageable);
        return users.map(userMapper::toUserDTO);
    }

    @Override
    public UserDTOResponse getUserById(Long id) throws UserNotFoundException {
        log.info("Getting user by id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTOResponse saveUser(UserDTORequest userDTORequest) {
        log.info("Saving user: {}", userDTORequest);
        User user = userMapper.toUser(userDTORequest);
        user.setPassword(passwordEncoder.encode(userDTORequest.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRoles(List.of(roleRepository.findByRoleName(ERole.ROLE_USER)));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public UserDTOResponse updateUser(UserDTOResponse user) throws UserNotFoundException {
        log.info("Updating user: {}", user);
        User user1 = userRepository.findById(user.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(!user1.getFirstname().equals(user.getFirstname()))
            user1.setFirstname(user.getFirstname());
        if (!user1.getLastname().equals(user.getLastname()))
            user1.setLastname(user.getLastname());
        if (!user1.getEmail().equals(user.getEmail()))
            user1.setEmail(user.getEmail());
        if (!user1.getUsername().equals(user.getUsername()))
            user1.setUsername(user.getUsername());
        if (!user1.getJobTitle().equals(user.getJobTitle()))
            user1.setJobTitle(user.getJobTitle());
        user1.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserDTO(user1);
    }

    @Override
    public Page<UserDTOResponse> searchUsers(String query, int page, int size) {
        log.info("Searching users by query: {}", query);
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<User> users = userRepository.findAllByFirstnameContainsOrLastnameContains(query, query, pageable);
        return users.map(userMapper::toUserDTO);
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        log.info("Deleting user by id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setDeleted(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public UserDTOResponse addImage(Long userId, MultipartFile file) throws UserNotFoundException {
        log.info("Adding image to user with id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (file.isEmpty()) {
            log.info("File is empty");
            throw new IllegalArgumentException("File is empty");
        }

        String fileName = "user_" + userId + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath, fileName);

        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        user.setImage(path.toString());
        user.setUpdatedAt(LocalDateTime.now());
        User save = userRepository.save(user);
        log.info("Image added successfully");
        return userMapper.toUserDTO(save);
    }

    @Override
    public UserDTOResponse updateImage(Long userId, MultipartFile file) throws UserNotFoundException {
        log.info("Updating image for user with id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (file.isEmpty()) {
            log.info("File is empty");
            throw new IllegalArgumentException("File is empty");
        }

        // Delete the existing image if it exists
        if (user.getImage() != null) {
            Path existingImagePath = Paths.get(user.getImage());
            try {
                Files.deleteIfExists(existingImagePath);
            } catch (IOException e) {
                log.info("Could not delete existing image");
                throw new RuntimeException("Could not delete existing image", e);
            }
        }

        // Save the new image
        String fileName = "user_" + userId + "_" + file.getOriginalFilename();
        Path newPath = Paths.get(uploadPath, fileName);
        try {
            Files.write(newPath, file.getBytes());
        } catch (IOException e) {
            log.info("Could not save new image");
            throw new RuntimeException("Could not save new image", e);
        }

        // Update user with new image path
        user.setImage(newPath.toString());
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        log.info("Image updated successfully");
        return userMapper.toUserDTO(updatedUser);
    }

    @Override
    public byte[] getImage(Long userId) throws UserNotFoundException {
        log.info("Getting image for user with id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getImage() == null || user.getImage().isEmpty()) {
            log.info("Image not found for user with id: {}", userId);
            throw new RuntimeException("Image not found for user with id: " + userId);
        }

        Path path = Paths.get(user.getImage());
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.info("Could not read image");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void incrementTasksCount(Long userId) throws UserNotFoundException {
        log.info("incrémentation du nombre de tâches pour l'utilisateur avec l'id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setTasksCount(user.getTasksCount() + 1);
        userRepository.save(user);
        log.info("Nombre de tâches incrémenté avec succès");
    }

    @Override
    public void decrementTasksCount(Long userId) throws UserNotFoundException {
        log.info("Décrémentation du nombre de tâches pour l'utilisateur avec l'id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setTasksCount(user.getTasksCount() - 1);
        userRepository.save(user);
        log.info("Nombre de tâches décrémenté avec succès");
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void resetPassword(String email, String confirmedPassword) throws UserNotFoundException {
        if(userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email);
            user.setPassword(passwordEncoder.encode(confirmedPassword));
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found !");
        }
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Old password is incorrect");
        }
    }


}
