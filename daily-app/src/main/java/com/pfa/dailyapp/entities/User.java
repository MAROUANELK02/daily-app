package com.pfa.dailyapp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Size(min = 4, max = 30, message = "Le prénom doit contenir au moins 4 lettres")
    @NotBlank(message = "Le prénom ne doit pas être vide")
    private String firstname;

    @Size(min = 4, max = 30, message = "message = Le nom doit contenir au moins 4 lettres")
    @NotBlank(message = "Le nom ne doit pas être vide")
    private String lastname;

    @Email(message = "Please enter a valid email")
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    @Size(min = 8, max = 30, message = "Username must be between 8 and 30 characters long")
    @NotBlank
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character (@#$%^&+=), and must not contain any whitespace.")
    private String password;

    @Size(min = 4, max = 50, message = "Le rôle doit contenir au moins 4 lettres")
    private String jobTitle;

    @Value("")
    private String image;

    private int tasksCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @ManyToMany
    private List<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Task> tasks;
}
