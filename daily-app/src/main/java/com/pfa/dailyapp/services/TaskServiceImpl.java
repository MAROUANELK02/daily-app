package com.pfa.dailyapp.services;

import com.pfa.dailyapp.dtos.TaskDTO;
import com.pfa.dailyapp.entities.Task;
import com.pfa.dailyapp.enums.TaskPriority;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.exceptions.TaskNotFoundException;
import com.pfa.dailyapp.mappers.TaskMapper;
import com.pfa.dailyapp.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    @Override
    public Page<TaskDTO> getTasksByUserId(Long userId, TaskStatus status, int page, int size) {
        log.info("Fetching tasks for user with id: {}", userId);
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Task> tasks = taskRepository.findByUserUserIdAndStatusOrderByPriority(userId, status, pageable);
        return tasks.map(taskMapper::toTaskDTO);
    }

    @Override
    public TaskDTO getTaskById(Long id) throws TaskNotFoundException {
        log.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.toTaskDTO(task);
    }

    @Override
    public TaskDTO saveTask(TaskDTO task) {
        log.info("Saving task: {}", task);
        Task task1 = taskMapper.toTask(task);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setCreatedAt(LocalDateTime.now());
        task1.setUpdatedAt(LocalDateTime.now());
        Task save = taskRepository.save(task1);
        return taskMapper.toTaskDTO(save);
    }

    @Override
    public TaskDTO updateTask(TaskDTO task) throws TaskNotFoundException {
        log.info("Updating task: {}", task);
        Task task1 = taskRepository.findById(task.getTaskId()).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!task1.getTitle().equals(task.getTitle()))
            task1.setTitle(task.getTitle());
        if (!task1.getDescription().equals(task.getDescription()))
            task1.setDescription(task.getDescription());
        task1.setUpdatedAt(LocalDateTime.now());
        Task save = taskRepository.save(task1);
        return taskMapper.toTaskDTO(save);
    }

    @Override
    public TaskDTO changeTaskStatus(TaskStatus status, Long id) throws TaskNotFoundException {
        log.info("Changing task status to: {}", status);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        Task save = taskRepository.save(task);
        return taskMapper.toTaskDTO(save);
    }

    @Override
    public TaskDTO changeTaskPriority(TaskPriority priority, Long id) throws TaskNotFoundException {
        log.info("Changing task priority to: {}", priority);
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setPriority(priority);
        task.setUpdatedAt(LocalDateTime.now());
        Task save = taskRepository.save(task);
        return taskMapper.toTaskDTO(save);
    }

    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        taskRepository.deleteById(id);
    }
}
