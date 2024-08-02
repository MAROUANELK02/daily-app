package com.pfa.dailyapp.web;

import com.pfa.dailyapp.dtos.*;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.exceptions.TaskNotFoundException;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import com.pfa.dailyapp.security.jwt.JwtUtils;
import com.pfa.dailyapp.services.TaskService;
import com.pfa.dailyapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("/tasksHistory")
    public ResponseEntity<?> getTasksHistory(@RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                             @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<TaskDTOResponse> tasks = taskService.getTasksHistory(page, size, keyword);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        try {
            UserDTOResponse userDTOResponse = userService.getUserById(userId);
            return ResponseEntity.ok(userDTOResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
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
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/image/{userId}")
    public ResponseEntity<?> getUserImage(@PathVariable("userId") Long userId) {
        try {
            byte[] imageBytes = userService.getImage(userId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/task/{userId}")
    public ResponseEntity<?> saveTask(@PathVariable Long userId,
                                      @RequestBody TaskDTORequest taskDTORequest,
                                      HttpServletRequest request) {
        Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
        if (!userId.equals(tokenUserId)) {
            return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
        }
        taskService.saveTask(taskDTORequest, userId);
        return ResponseEntity.ok(new SuccessResponse("Tâche ajoutée avec succès"));
    }

    @PostMapping("/image/{userId}")
    public ResponseEntity<?> addImage(@PathVariable("userId") Long userId,
                                      @RequestBody MultipartFile image,
                                      HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            userService.addImage(userId, image);
            return ResponseEntity.ok(new SuccessResponse("Image ajoutée avec succès"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/image/{userId}")
    public ResponseEntity<?> updateImage(@PathVariable("userId") Long userId,
                                         @RequestBody MultipartFile image,
                                         HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            userService.updateImage(userId, image);
            return ResponseEntity.ok(new SuccessResponse("Image mise à jour avec succès"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTOResponse userDTOResponse,
                                        HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            Long userId = userDTOResponse.getUserId();
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            userService.updateUser(userDTOResponse);
            return ResponseEntity.ok(new SuccessResponse("Mise à jour de l'utilisateur réussie"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/task/update/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable(name = "taskId") Long taskId,
                                        @RequestBody TaskDTORequest taskDTORequest,
                                        HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            Long userId = taskService.getUserIdByTaskId(taskId);
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            taskService.updateTask(taskDTORequest, taskId);
            return ResponseEntity.ok(new SuccessResponse("Tâche mise à jour avec succès"));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/task/{taskId}/status")
    public ResponseEntity<?> changeTaskStatus(@PathVariable("taskId") Long taskId,
                                              @RequestParam("status") TaskStatus status,
                                              HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            Long userId = taskService.getUserIdByTaskId(taskId);
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            taskService.changeTaskStatus(status, taskId);
            return ResponseEntity.ok(new SuccessResponse("Le statut de la tâche a été modifié avec succès"));
        } catch (TaskNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/task/{taskId}/priority")
    public ResponseEntity<?> changeTaskPriority(@PathVariable("taskId") Long taskId,
                                                @RequestParam("priority") TaskPriority priority,
                                                HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            Long userId = taskService.getUserIdByTaskId(taskId);
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            taskService.changeTaskPriority(priority, taskId);
            return ResponseEntity.ok(new SuccessResponse("La priorité de la tâche a été modifiée avec succès"));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{userId}/changePassword")
    public ResponseEntity<?> changePassword(@PathVariable("userId") Long userId,
                                            @Valid @RequestBody ChangePasswordDTO changePasswordDTO,
                                            HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            if(changePasswordDTO.newPassword().equals(changePasswordDTO.confirmedPassword())) {
                userService.changePassword(userId, changePasswordDTO.oldPassword(), changePasswordDTO.confirmedPassword());
                return ResponseEntity.ok(new SuccessResponse("Réinitialisation du mot de passe réussie"));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("Les mots de passe ne correspondent pas"));
            }
        } catch (UserNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") Long taskId,
                                        HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            Long userId = taskService.getUserIdByTaskId(taskId);
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            taskService.deleteTask(taskId);
            return ResponseEntity.ok(new SuccessResponse("Tâche supprimée avec succès"));
        } catch (TaskNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/image/{userId}")
    public ResponseEntity<?> deleteImage(@PathVariable("userId") Long userId,
                                         HttpServletRequest request) {
        try {
            Long tokenUserId = JwtUtils.getUserIdFromRequest(request);
            if (!userId.equals(tokenUserId)) {
                return ResponseEntity.status(403).body(new ErrorResponse("Non Autorisé"));
            }
            userService.deleteImage(userId);
            return ResponseEntity.ok(new SuccessResponse("Image supprimée avec succès"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
