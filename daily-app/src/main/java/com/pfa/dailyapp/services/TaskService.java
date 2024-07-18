package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.TaskDTO;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.exceptions.TaskNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Page<TaskDTO> getTasksByUserId(Long userId, TaskStatus status, int page, int size);
    TaskDTO getTaskById(Long id) throws TaskNotFoundException;
    TaskDTO saveTask(TaskDTO task);
    TaskDTO updateTask(TaskDTO task) throws TaskNotFoundException;
    TaskDTO changeTaskStatus(TaskStatus status, Long id) throws TaskNotFoundException;
    TaskDTO changeTaskPriority(TaskPriority priority, Long id) throws TaskNotFoundException;
    void deleteTask(Long id);
}
