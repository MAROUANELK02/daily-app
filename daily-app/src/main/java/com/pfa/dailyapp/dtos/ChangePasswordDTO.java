package com.pfa.dailyapp.dtos;

public record ChangePasswordDTO(String oldPassword, String newPassword, String confirmedPassword) {
}
