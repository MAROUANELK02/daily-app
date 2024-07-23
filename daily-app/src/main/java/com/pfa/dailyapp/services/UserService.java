package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.UserDTORequest;
import com.pfa.dailyapp.dtos.UserDTOResponse;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Page<UserDTOResponse> getUsers(int page, int size);
    UserDTOResponse getUserById(Long id) throws UserNotFoundException;
    UserDTOResponse saveUser(UserDTORequest user);
    UserDTOResponse updateUser(UserDTOResponse user) throws UserNotFoundException;
    Page<UserDTOResponse> searchUsers(String query, int page, int size);
    void deleteUser(Long id) throws UserNotFoundException;
    UserDTOResponse addImage(Long userId, MultipartFile image) throws UserNotFoundException;
    UserDTOResponse updateImage(Long userId, MultipartFile file) throws UserNotFoundException;
    byte[] getImage(Long userId) throws UserNotFoundException;
    void incrementTasksCount(Long userId) throws UserNotFoundException;
    void decrementTasksCount(Long userId) throws UserNotFoundException;
    Boolean existsByUsername(String username);
}
