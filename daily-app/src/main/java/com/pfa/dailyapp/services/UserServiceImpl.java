package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.UserDTO;
import com.pfa.dailyapp.entities.User;
import com.pfa.dailyapp.enums.ERole;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import com.pfa.dailyapp.mappers.RoleMapper;
import com.pfa.dailyapp.mappers.UserMapper;
import com.pfa.dailyapp.repositories.RoleRepository;
import com.pfa.dailyapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleMapper roleMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<UserDTO> getUsers(int page, int size) {
        log.info("Getting all users");
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<User> users = userRepository.findAllByDeletedFalse(pageable);
        return users.map(userMapper::toUserDTO);
    }

    @Override
    public UserDTO getUserById(Long id) throws UserNotFoundException {
        log.info("Getting user by id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO saveUser(UserDTO user) {
        log.info("Saving user: {}", user);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRoleDTOS(List.of(roleMapper.toRoleDTO(roleRepository.findByRoleName(ERole.ROLE_USER))));
        User savedUser = userRepository.save(userMapper.toUser(user));
        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO user) throws UserNotFoundException {
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
        if (!user1.getPassword().equals(user.getPassword()))
            user1.setPassword(user.getPassword());
        if (!user1.getJobTitle().equals(user.getJobTitle()))
            user1.setJobTitle(user.getJobTitle());
        user1.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserDTO(user1);
    }

    @Override
    public Page<UserDTO> searchUsers(String query, int page, int size) {
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
    public UserDTO addImage(Long userId, MultipartFile file) throws UserNotFoundException {
        log.info("Adding image to user with id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileName = "user_" + userId + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath, fileName);

        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user.setImage(path.toString());
        user.setUpdatedAt(LocalDateTime.now());
        User save = userRepository.save(user);
        return userMapper.toUserDTO(save);
    }

    @Override
    public UserDTO updateImage(Long userId, MultipartFile file) throws UserNotFoundException {
        log.info("Updating image for user with id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Delete the existing image if it exists
        if (user.getImage() != null) {
            Path existingImagePath = Paths.get(user.getImage());
            try {
                Files.deleteIfExists(existingImagePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not delete existing image", e);
            }
        }

        // Save the new image
        String fileName = "user_" + userId + "_" + file.getOriginalFilename();
        Path newPath = Paths.get(uploadPath, fileName);
        try {
            Files.write(newPath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not save new image", e);
        }

        // Update user with new image path
        user.setImage(newPath.toString());
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return userMapper.toUserDTO(updatedUser);
    }

    @Override
    public byte[] getImage(Long userId) throws UserNotFoundException {
        log.info("Getting image for user with id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getImage() == null || user.getImage().isEmpty()) {
            throw new RuntimeException("Image not found for user with id: " + userId);
        }

        Path path = Paths.get(user.getImage());
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
