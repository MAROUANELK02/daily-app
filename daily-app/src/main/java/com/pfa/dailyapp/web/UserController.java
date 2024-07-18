package com.pfa.dailyapp.web;

import com.pfa.dailyapp.dtos.TaskDTO;
import com.pfa.dailyapp.dtos.UserDTO;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.exceptions.TaskNotFoundException;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import com.pfa.dailyapp.services.TaskService;
import com.pfa.dailyapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('USER')")
public class UserController {
    private UserService userService;
    private TaskService taskService;

    @GetMapping("/{userId}/tasks/inProgress")
    public ResponseEntity<?> getInProgressTasksByUserId(@PathVariable("userId") Long userId,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<TaskDTO> tasks = taskService.getTasksByUserId(userId, TaskStatus.IN_PROGRESS, page, size);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{userId}/tasks/completed")
    public ResponseEntity<?> getCompletedTasksByUserId(@PathVariable("userId") Long userId,
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<TaskDTO> tasks = taskService.getTasksByUserId(userId, TaskStatus.DONE, page, size);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        try {
            UserDTO userDTO = userService.getUserById(userId);
            return ResponseEntity.ok(userDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      @RequestParam(name = "query", required = false) String query) {
        if(query != null) {
            Page<UserDTO> users = userService.searchUsers(query, page, size);
            return ResponseEntity.ok(users);
        }
        Page<UserDTO> users = userService.getUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable("taskId") Long taskId) {
        try {
            TaskDTO taskDTO = taskService.getTaskById(taskId);
            return ResponseEntity.ok(taskDTO);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/image/{userId}")
    public ResponseEntity<?> getUserImage(@PathVariable("userId") Long userId) {
        try {
            byte[] imageBytes = userService.getImage(userId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/task/{userId}")
    public ResponseEntity<?> saveTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO savedTask = taskService.saveTask(taskDTO);
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping("/image/{userId}")
    public ResponseEntity<?> addImage(@PathVariable("userId") Long userId,
                                      @RequestBody MultipartFile image) {
        try {
            UserDTO updatedUser = userService.addImage(userId, image);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/image/{userId}")
    public ResponseEntity<?> updateImage(@PathVariable("userId") Long userId,
                                         @RequestBody MultipartFile image) {
        try {
            UserDTO updatedUser = userService.updateImage(userId, image);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/task/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO taskDTO) {
        try {
            TaskDTO updatedTask = taskService.updateTask(taskDTO);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/task/{taskId}/status")
    public ResponseEntity<?> changeTaskStatus(@PathVariable("taskId") Long taskId,
                                              @RequestParam("status") TaskStatus status) {
        try {
            TaskDTO updatedTask = taskService.changeTaskStatus(status, taskId);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/task/{taskId}/priority")
    public ResponseEntity<?> changeTaskPriority(@PathVariable("taskId") Long taskId,
                                                @RequestParam("priority") TaskPriority priority) {
        try {
            TaskDTO updatedTask = taskService.changeTaskPriority(priority, taskId);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

}
