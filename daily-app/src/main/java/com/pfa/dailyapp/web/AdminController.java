package com.pfa.dailyapp.web;

import com.pfa.dailyapp.dtos.ErrorResponse;
import com.pfa.dailyapp.dtos.SuccessResponse;
import com.pfa.dailyapp.dtos.UserDTORequest;
import com.pfa.dailyapp.dtos.UserDTOResponse;
import com.pfa.dailyapp.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private UserService userService;

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsers(page, size));
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTORequest userDTORequest) {
        try {
            userService.saveUser(userDTORequest);
            return ResponseEntity.ok(new SuccessResponse("L'utilisateur a été enregistré avec succès"));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new SuccessResponse("L'utilisateur a été supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/existsByUsername/{username}")
    public ResponseEntity<?> existsByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }
}