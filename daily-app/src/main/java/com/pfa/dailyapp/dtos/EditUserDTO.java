package com.pfa.dailyapp.dtos;

public record EditUserDTO(Long userId, String firstname, String lastname,
                          String email, String username, String jobTitle) {}
