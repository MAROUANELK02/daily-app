package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.TaskDTORequest;
import com.pfa.dailyapp.dtos.TaskDTOResponse;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.exceptions.TaskNotFoundException;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;

public interface TaskService {
    Page<TaskDTOResponse> getTasksByUserId(Long userId, TaskStatus status, int page, int size);
    TaskDTOResponse getTaskById(Long id) throws TaskNotFoundException;
    TaskDTOResponse saveTask(TaskDTORequest task, Long userId);
    TaskDTOResponse updateTask(TaskDTOResponse task) throws TaskNotFoundException;
    TaskDTOResponse changeTaskStatus(TaskStatus status, Long id) throws TaskNotFoundException, UserNotFoundException;
    TaskDTOResponse changeTaskPriority(TaskPriority priority, Long id) throws TaskNotFoundException;
    void deleteTask(Long id) throws TaskNotFoundException, UserNotFoundException;
}
