package com.pfa.dailyapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
