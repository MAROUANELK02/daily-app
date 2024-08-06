package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.TaskDTORequest;
import com.pfa.dailyapp.dtos.TaskDTOResponse;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.exceptions.TaskNotFoundException;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TaskService {
    Page<TaskDTOResponse> getTasksByUserId(Long userId, TaskStatus status, int page, int size);
    Page<TaskDTOResponse> getCompletedTasksByUserId(Long userId, TaskStatus status, int page, int size);
    TaskDTOResponse getTaskById(Long id) throws TaskNotFoundException;
    Long getUserIdByTaskId(Long taskId) throws TaskNotFoundException;
    Page<TaskDTOResponse> getTasksHistory(int page, int size, String keyword);
    TaskDTOResponse saveTask(TaskDTORequest task, Long userId);
    TaskDTOResponse updateTask(TaskDTORequest task, Long taskId) throws TaskNotFoundException;
    TaskDTOResponse changeTaskStatus(TaskStatus status, Long id) throws TaskNotFoundException, UserNotFoundException;
    TaskDTOResponse changeTaskPriority(TaskPriority priority, Long id) throws TaskNotFoundException;
    void deleteTask(Long id) throws TaskNotFoundException, UserNotFoundException;
    Map<String, Long> getCompletedTasksCountPerDayByUserId(Long userId, int days);
    Map<String, Long> getInProgressTasksCountPerDayByUserId(Long userId, int days);
}
