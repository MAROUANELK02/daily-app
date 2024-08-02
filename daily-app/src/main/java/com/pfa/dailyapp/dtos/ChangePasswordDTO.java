package com.pfa.dailyapp.dtos;

import jakarta.validation.constraints.Pattern;

public record ChangePasswordDTO(String oldPassword, @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Le mot de passe doit contenir au moins un chiffre, une lettre minuscule, une lettre " +
                "majuscule, un caractère spécial et ne doit contenir aucun espace.") String newPassword,
                                String confirmedPassword) {
}
