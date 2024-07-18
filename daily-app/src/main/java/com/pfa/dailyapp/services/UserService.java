package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.UserDTO;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Page<UserDTO> getUsers(int page, int size);
    UserDTO getUserById(Long id) throws UserNotFoundException;
    UserDTO saveUser(UserDTO user);
    UserDTO updateUser(UserDTO user) throws UserNotFoundException;
    Page<UserDTO> searchUsers(String query, int page, int size);
    void deleteUser(Long id) throws UserNotFoundException;
    UserDTO addImage(Long userId, MultipartFile image) throws UserNotFoundException;
    UserDTO updateImage(Long userId, MultipartFile file) throws UserNotFoundException;
    byte[] getImage(Long userId) throws UserNotFoundException;
}
