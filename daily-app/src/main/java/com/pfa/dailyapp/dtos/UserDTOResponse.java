package com.pfa.dailyapp.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTOResponse {
    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String jobTitle;
    private int tasksCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    List<RoleDTO> roleDTOS;
}