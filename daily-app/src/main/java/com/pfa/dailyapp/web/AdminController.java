package com.pfa.dailyapp.web;

import com.pfa.dailyapp.dtos.UserDTO;
import com.pfa.dailyapp.services.UserService;
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
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {
        UserDTO saveUser = userService.saveUser(userDTO);
        return ResponseEntity.ok(saveUser);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
