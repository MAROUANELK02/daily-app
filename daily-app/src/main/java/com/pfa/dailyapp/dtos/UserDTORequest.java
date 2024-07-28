package com.pfa.dailyapp.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTORequest {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Le mot de passe doit contenir au moins un chiffre, une lettre minuscule, une lettre " +
                    "majuscule, un caractère spécial et ne doit contenir aucun espace.")
    private String password;
    private String jobTitle;
}
