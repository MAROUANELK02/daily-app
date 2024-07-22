package com.pfa.dailyapp.web;

import com.pfa.dailyapp.dtos.TaskDTORequest;
import com.pfa.dailyapp.dtos.TaskDTOResponse;
import com.pfa.dailyapp.dtos.UserDTOResponse;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.exceptions.TaskNotFoundException;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import com.pfa.dailyapp.services.TaskService;
import com.pfa.dailyapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        Page<TaskDTOResponse> tasks = taskService.getTasksByUserId(userId, TaskStatus.IN_PROGRESS, page, size);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{userId}/tasks/completed")
    public ResponseEntity<?> getCompletedTasksByUserId(@PathVariable("userId") Long userId,
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<TaskDTOResponse> tasks = taskService.getTasksByUserId(userId, TaskStatus.DONE, page, size);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        try {
            UserDTOResponse userDTOResponse = userService.getUserById(userId);
            return ResponseEntity.ok(userDTOResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      @RequestParam(name = "query", required = false) String query) {
        if(query != null) {
            Page<UserDTOResponse> users = userService.searchUsers(query, page, size);
            return ResponseEntity.ok(users);
        }
        Page<UserDTOResponse> users = userService.getUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable("taskId") Long taskId) {
        try {
            TaskDTOResponse taskDTOResponse = taskService.getTaskById(taskId);
            return ResponseEntity.ok(taskDTOResponse);
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
    public ResponseEntity<?> saveTask(@PathVariable Long userId,
            @RequestBody TaskDTORequest taskDTORequest) {
        TaskDTOResponse savedTask = taskService.saveTask(taskDTORequest, userId);
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping("/image/{userId}")
    public ResponseEntity<?> addImage(@PathVariable("userId") Long userId,
                                      @RequestBody MultipartFile image) {
        try {
            UserDTOResponse updatedUser = userService.addImage(userId, image);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/image/{userId}")
    public ResponseEntity<?> updateImage(@PathVariable("userId") Long userId,
                                         @RequestBody MultipartFile image) {
        try {
            UserDTOResponse updatedUser = userService.updateImage(userId, image);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTOResponse userDTOResponse) {
        try {
            UserDTOResponse updatedUser = userService.updateUser(userDTOResponse);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/task/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTOResponse taskDTOResponse) {
        try {
            TaskDTOResponse updatedTask = taskService.updateTask(taskDTOResponse);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/task/{taskId}/status")
    public ResponseEntity<?> changeTaskStatus(@PathVariable("taskId") Long taskId,
                                              @RequestParam("status") TaskStatus status) {
        try {
            TaskDTOResponse updatedTask = taskService.changeTaskStatus(status, taskId);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/task/{taskId}/priority")
    public ResponseEntity<?> changeTaskPriority(@PathVariable("taskId") Long taskId,
                                                @RequestParam("priority") TaskPriority priority) {
        try {
            TaskDTOResponse updatedTask = taskService.changeTaskPriority(priority, taskId);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") Long taskId) {
        try {
            taskService.deleteTask(taskId);
        } catch (TaskNotFoundException | UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Task deleted successfully");
    }

}
