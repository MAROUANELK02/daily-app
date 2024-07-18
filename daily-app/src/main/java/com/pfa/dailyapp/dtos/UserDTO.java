package com.pfa.dailyapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {
    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String jobTitle;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    List<RoleDTO> roleDTOS;
}
