package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.EditUserDTO;
import com.pfa.dailyapp.dtos.UserDTORequest;
import com.pfa.dailyapp.dtos.UserDTOResponse;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Page<UserDTOResponse> getUsers(int page, int size);
    UserDTOResponse getUserById(Long id) throws UserNotFoundException;
    void saveUser(UserDTORequest user);
    void updateUser(EditUserDTO user) throws UserNotFoundException;
    Page<UserDTOResponse> searchUsers(String query, int page, int size);
    void deleteUser(Long id) throws UserNotFoundException;
    void addImage(Long userId, MultipartFile image) throws UserNotFoundException;
    void updateImage(Long userId, MultipartFile file) throws UserNotFoundException;
    void deleteImage(Long userId) throws UserNotFoundException;
    byte[] getImage(Long userId) throws UserNotFoundException;
    void incrementTasksCount(Long userId) throws UserNotFoundException;
    void decrementTasksCount(Long userId) throws UserNotFoundException;
    Boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void resetPassword(String email, String confirmedPassword) throws UserNotFoundException;
    void changePassword(Long userId, String oldPassword, String newPassword) throws UserNotFoundException;
}
